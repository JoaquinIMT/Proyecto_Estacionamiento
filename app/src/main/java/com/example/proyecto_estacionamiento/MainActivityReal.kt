package com.example.proyecto_estacionamiento

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.SearchView
import android.widget.TabHost
import android.widget.Toast
import kotlinx.android.synthetic.main.content_main_activity_real.*

class MainActivityReal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var searchView: SearchView
    val dbHandler = MindOrksDBOpenHelper(this, null)
    lateinit var fragmentoSalidas: FragmentoSalidas
    lateinit var fragmentoBusqueda: FragmentoBusqueda
    lateinit var primerFragmento: PrimerFragmento
    lateinit var estacionamiento: Estacionamiento
    lateinit var pasado: Pasado


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_real)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)

        val lugares = 21

        val dataBaseNew: MutableList<Automovil> = getSQLITE(true)

        val past = getSQLITE(false) //esta variable nos dice si al sacar las variables del sqlite hay automoviles fuera

        estacionamiento = if(dataBaseNew.size > 0){
            val lugaresDisponibles = lugares - dataBaseNew.size
            Estacionamiento(lugaresDisponibles,dataBaseNew)

            }else{
                Estacionamiento(lugares, null)
            }


        pasado = if( past.size > 0 ){

            Pasado(past)

            }else{
                Pasado(mutableListOf())
            }


        val adapter = FragmentAdapter(supportFragmentManager)

        val codigoActual = getFolio()


        fragmentoSalidas = FragmentoSalidas(pasado)
        fragmentoBusqueda = FragmentoBusqueda(estacionamiento, pasado, dbHandler)
        primerFragmento = PrimerFragmento(estacionamiento.lugares, codigoActual)

        adapter.newFragment(primerFragmento)
        adapter.newFragment(fragmentoBusqueda)
        adapter.newFragment(fragmentoSalidas)

        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)?.setIcon(R.drawable.ic_home_negra)
        tabs.getTabAt(1)?.setIcon(R.drawable.ic_search_negra)
        tabs.getTabAt(2)?.setIcon(R.drawable.ic_exit)

    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

            drawerLayout.closeDrawer(GravityCompat.START)

        } else if(fragmentoBusqueda.bye.size > 1){

            fragmentoBusqueda.bye = mutableListOf(0)
            val ft = supportFragmentManager.beginTransaction()
            ft.detach(fragmentoBusqueda)
            ft.attach(fragmentoBusqueda)
            ft.commit()
            for(i in 0 until fragmentoBusqueda.adapter.filterList.size-1){
                fragmentoBusqueda.adapter.filterList[i].checked = false
            }
            fragmentoBusqueda.adapter.notifyDataSetChanged()

        } else{
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity_real, menu)

        val searchManager: SearchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        searchView = menu.findItem(R.id.action_search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.maxWidth = Int.MAX_VALUE

        searchView.setOnSearchClickListener {

            if(tabs.selectedTabPosition != 1 ){

                tabs.setScrollPosition(1,0F,false)

            }

            viewPager.currentItem = 1

        }

        searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{

            override fun onQueryTextSubmit(query: String?): Boolean {
                fragmentoBusqueda.adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                fragmentoBusqueda.adapter.filter.filter(newText)
                return false
            }

        })


        return true
    }

    /*override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }*/

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.share -> {
                Toast.makeText(this,"Se borraron las entradas al pasar la información",Toast.LENGTH_SHORT).show()
                dbHandler.dropTable(true) //Mandamos false para eliminar la tabla de entradas de la base de datos
                intentToMainActivityReal()
            }

            R.id.ic_power -> {
                Toast.makeText(this,"Se borraron las salidas al salir de sesión",Toast.LENGTH_SHORT).show()
                dbHandler.dropTable(false) //Mandamos false para eliminar la tabla de salidas de la base de datos
                intentToMainActivityReal()


            }
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun getFolio(): String {
        val cursor = dbHandler.getFolio()

        cursor!!.moveToFirst()
        var folio = ""
        if(cursor.count > 0){
            folio = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_FOLIO))
        }
        return folio
    }

    fun getSQLITE(tipo: Boolean): MutableList<Automovil> {

        val autmoviles = mutableListOf<Automovil>()

        val cursor = dbHandler.getAllName(tipo)

        cursor!!.moveToFirst()

        if(cursor.count > 0){

            var mat: String = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_MATRICULA))
            var mar: String = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_MARCA))
            var mod: String = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_MODELO))
            var he: String = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_HORAE))
            var hs: String = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_HORASA))
            var color: String = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_COLOR))
            var cam: Boolean = cursor.getInt(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_TIPO)) != 0
            var folio: String = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_FOLIO))

            var cosa1 = Automovil(mat,mar,mod,he,hs,color,cam,folio)

            autmoviles.add(cosa1)

            while (cursor.moveToNext()) {

                mat = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_MATRICULA))
                mar = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_MARCA))
                mod = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_MODELO))
                he = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_HORAE))
                hs = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_HORASA))
                color = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_COLOR))
                cam = cursor.getInt(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_TIPO)) != 0
                folio = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_FOLIO))
                cosa1 = Automovil(mat,mar,mod,he,hs,color,cam,folio)

                autmoviles.add(cosa1)
            }

        }


        cursor.close()

        return autmoviles
    }

    fun intentToMainActivityReal(){

        val intent = Intent(applicationContext,MainActivityReal::class.java)
        startActivity(intent)
        finishAffinity()
    }

}
