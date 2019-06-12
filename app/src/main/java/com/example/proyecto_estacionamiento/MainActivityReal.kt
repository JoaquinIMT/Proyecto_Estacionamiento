package com.example.proyecto_estacionamiento

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
import android.widget.Toast
import kotlinx.android.synthetic.main.content_main_activity_real.*

class MainActivityReal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    var numeroDeSQLite: Int = 0

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

        var estacionamiento: Estacionamiento? =  null
        var pasado: Pasado? =  null

        val prueba: Estacionamiento? = intent.getParcelableExtra("Estacionamiento")
        val prueba2: Pasado? = intent.getParcelableExtra("Pasado")
        val prueba3: Int = intent.getIntExtra("tipoE",Int.MAX_VALUE)


        estacionamiento = if (prueba != null){
            prueba
        } else{

            val dataBaseNew: MutableList<Automovil>? = getSQLITE(true)


            /*val automoviles = mutableListOf<Automovil>(Automovil(matricula = "ASW0M3",marca = "Toyota",modelo = "Corolla"
                ,horaEntrada = "11", horaSalida = "14")
                ,Automovil(matricula = "JOLUQFER",marca = "Nissan",modelo = "Versa",horaEntrada = "10", horaSalida = "15"))
            */
            if(dataBaseNew != null){
                val lugaresDisponibles = lugares - dataBaseNew.size
                Estacionamiento(lugaresDisponibles,dataBaseNew)

            }else{
                Estacionamiento(lugares, null)
            }

        }

        pasado =  if (prueba2 != null){
            prueba2
        } else{

            val past = getSQLITE(false) //esta variable nos dice si al sacar las variables del sqlite hay automoviles fuera

            if( past != null ){
                Pasado(past)
            }else{
                Pasado(mutableListOf())
            }

        }

        val adapter = FragmentAdapter(supportFragmentManager)

        adapter.newFragment(PrimerFragmento(estacionamiento, pasado))
        adapter.newFragment(FragmentoBusqueda(estacionamiento, pasado, numeroDeSQLite))
        adapter.newFragment(FragmentoSalidas(pasado))

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
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main_activity_real, menu)
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {

            R.id.share -> {
                Toast.makeText(this,"Compartiendo",Toast.LENGTH_SHORT).show()
            }

            R.id.ic_power -> {
                Toast.makeText(this,"Ya saliste de sesión",Toast.LENGTH_SHORT).show()
            }
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun getSQLITE(tipo: Boolean): MutableList<Automovil>? {

        val dbHandler = MindOrksDBOpenHelper(this, null)

        val autmoviles = mutableListOf<Automovil>()

        val cursor = dbHandler.getAllName(tipo)
        cursor!!.moveToFirst()

        if(cursor.count > 0){

            var mat: String = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_MATRICULA))
            var mar: String = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_MARCA))
            var mod: String = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_MODELO))
            var he: String = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_HORAE))
            var hs: String = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_HORASA))
            var _id: Int = cursor.getInt(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_ID))


            var cosa1 = Automovil(mat,mar,mod,he,hs,_id)

            autmoviles.add(cosa1)

            while (cursor.moveToNext()) {

                mat = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_MATRICULA))
                mar = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_MARCA))
                mod = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_MODELO))
                he = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_HORAE))
                hs = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_HORASA))
                _id = cursor.getInt(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_ID))

                cosa1 = Automovil(mat,mar,mod,he,hs,_id)

                autmoviles.add(cosa1)
            }
            numeroDeSQLite = _id

        }


        cursor.close()

        return autmoviles
    }

  /*  fun takePass(estacionamiento: Estacionamiento): MutableList<Automovil>?{

        var automoviles : MutableList<Automovil>? = mutableListOf<Automovil>()

        if(estacionamiento.carros != null){
            for(i in estacionamiento.carros!!){

                if( i.horaSalida!="" ){
                    automoviles?.add(i)
                }

            }

        }else{
            automoviles = null
        }

        return automoviles

    }
*/
}
