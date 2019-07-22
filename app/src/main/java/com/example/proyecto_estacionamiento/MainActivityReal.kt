package com.example.proyecto_estacionamiento

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.content_main_activity_real.*
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class MainActivityReal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var searchView: SearchView
    val dbHandler = MindOrksDBOpenHelper(this, null)
    lateinit var fragmentoSalidas: FragmentoSalidas
    lateinit var fragmentoBusqueda: FragmentoBusqueda
    lateinit var primerFragmento: PrimerFragmento
    lateinit var estacionamiento: Estacionamiento
    lateinit var datos: DatosIniciales
    lateinit var workerNameTV: TextView
    lateinit var pakingName: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_real)

        /*val workerName: TextView = findViewById(R.id.nombre_trabajador)
        workerName.text = "holi"*/

        /*tracker = SelectionTracker.Builder<String>(
            "mySelection",
            recyclerView,
            StableIdKeyProvider(recyclerView),
            MyItemDetailsLookup(recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()



        */

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

        datos = getDatosIniciales()

        /*
        drawerLayout.nombre_trabajador.text = datos.workerName
        drawerLayout.nombre_estacionamiento.text = datos.parkingName
*/
        val lugares: Int = datos.slotsNumber!!


        //var estacionamiento: Estacionamiento? =  null
        var pasado: Pasado? =  null

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

        val generalEspecial: List<Int> =   if(estacionamiento.carros == null){
            listOf(0,0)
        }else{
            var especial : Int = 0
            var general : Int = 0
            for(i in estacionamiento.carros!!){
                if(i.folio[0].toString() == ",") ++especial
                else ++general
            }
            listOf(general,especial)
        }



        fragmentoSalidas = FragmentoSalidas(pasado,datos.typeOfParking!!)
        fragmentoBusqueda = FragmentoBusqueda(estacionamiento, dbHandler,datos.typeOfParking!!, this)
        primerFragmento = PrimerFragmento(estacionamiento.lugares,lugares.toFloat(), codigoActual,generalEspecial)

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

        } else if(fragmentoBusqueda.bye.size > 0){

            fragmentoBusqueda.bye = mutableListOf()
            val ft = supportFragmentManager.beginTransaction()
            ft.detach(fragmentoBusqueda)
            ft.attach(fragmentoBusqueda)
            ft.commit()
            for(i in 0 until fragmentoBusqueda.adapter.filterList.size-1){
                fragmentoBusqueda.adapter.filterList[i].checked = false
            }
            fragmentoBusqueda.adapter.estacionamiento.oneSelected = 0
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
                val intent:Intent = Intent(applicationContext, QR::class.java)
                intent.putExtra("estacionamiento",estacionamiento)
                intent.putExtra("folio",getFolio())
                intent.putExtra("type",datos)
                startActivity(intent)
                /*Toast.makeText(this,"Se borraron las entradas al pasar la información",Toast.LENGTH_SHORT).show()
                dbHandler.dropTable(true) //Mandamos false para eliminar la tabla de entradas de la base de datos
*/
                //intentToMainActivityReal()


                //intentToMainActivityReal()

            }

            R.id.ic_power -> {
                if(hasInternetAccess()){
                    Toast.makeText(this, "Se borraron las salidas al salir de sesión", Toast.LENGTH_SHORT).show()
                    dbHandler.dropTable(false) //Mandamos false para eliminar la tabla de salidas de la base de datos
                    intentToMainActivityReal()
                }
            }
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun getDatosIniciales(): DatosIniciales{

        val cursor = dbHandler.getType()

        cursor!!.moveToFirst()

        //datos.parkingFee = listOf(cursor_fee.getFloat(cursor_fee.getColumnIndex(MindOrksDBOpenHelper.COLUMN_FOLIO)),cursor_fee.getFloat(cursor_fee.getColumnIndex(MindOrksDBOpenHelper.COLUMN_FOLIO)))

        val parkingName = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_PARKING_NAME))
        val slotsNumber = cursor.getInt(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_SLOTS_NUMBER))
        val workerName = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_WORKER_NAME))
        val typeOfParking = cursor.getInt(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_TIPO))

        cursor.close()
        return DatosIniciales(parkingName, workerName, typeOfParking, arrayOf(Fee(arrayOf(),0.0)), slotsNumber)
    }

    fun getFolio(): String {
        val cursor = dbHandler.getFolio()

        cursor!!.moveToFirst()
        var folio = ""
        if(cursor.count > 0){
            folio = cursor.getString(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_FOLIO))
        }
        cursor.close()
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

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE)

        return if (connectivityManager is ConnectivityManager) {
            val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
            networkInfo?.isConnected ?: false
        } else false
    }

    fun hasInternetAccess(): Boolean {
        if (isNetworkAvailable()) {
            try {
                val urlc = URL("http://clients3.google.com/generate_204").openConnection() as HttpURLConnection
                urlc.setRequestProperty("User-Agent", "Android")
                urlc.setRequestProperty("Connection", "close")
                urlc.connectTimeout = 1500
                //Con este if logramos hacer la conexión de otro modo crashea
                if (android.os.Build.VERSION.SDK_INT > 9) {
                    val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
                    StrictMode.setThreadPolicy(policy)
                }
                urlc.connect()
                return urlc.responseCode == 204 && urlc.contentLength == 0
            } catch (e: IOException) {
                Toast.makeText(this,"Error al verificar la conexión a internet",Toast.LENGTH_LONG).show()
                //Log.e(FragmentActivity.TAG, "Error checking internet connection", e)
            }

        } else {
            Toast.makeText(this,"No hay redes disponibles",Toast.LENGTH_SHORT).show()
            //Log.d(FragmentActivity.TAG, "No network available!")
        }
        return false
    }

}
