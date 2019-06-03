package com.example.proyecto_estacionamiento

import android.os.Bundle
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivityReal : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {


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

        estacionamiento = if (prueba != null){
            prueba
        } else{
            /*val automoviles = mutableListOf<Automovil>(Automovil(matricula = "ASW0M3",marca = "Toyota",modelo = "Corolla"
                ,horaEntrada = "11", horaSalida = "14")
                ,Automovil(matricula = "JOLUQFER",marca = "Nissan",modelo = "Versa",horaEntrada = "10", horaSalida = "15"))
            */
            Estacionamiento(lugares, null)
        }

        pasado =  if (prueba2 != null){
            prueba2
        } else{
            /*val automoviles = mutableListOf<Automovil>(Automovil(matricula = "ASW0M3",marca = "Toyota",modelo = "Corolla"
                ,horaEntrada = "11", horaSalida = "14")
                ,Automovil(matricula = "JOLUQFER",marca = "Nissan",modelo = "Versa",horaEntrada = "10", horaSalida = "15"))
            */
            Pasado(mutableListOf())
        }

        val adapter = FragmentAdapter(supportFragmentManager)

        adapter.newFragment(PrimerFragmento(estacionamiento, pasado))
        adapter.newFragment(FragmentoBusqueda(estacionamiento, pasado))
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

}
