package com.example.proyecto_estacionamiento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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


    fun exitParking(estacionamiento: Estacionamiento, automovil:Automovil, horaSalida: String): Estacionamiento{
        val index = intent.getIntExtra("index", Int.MAX_VALUE)

        estacionamiento.carros?.removeAt(index)

        automovil.horaSalida = horaSalida

        estacionamiento.carros?.add(automovil)

        //estacionamiento.carros?.set(index!!,automovil)

        estacionamiento.lugares += 1


        return  estacionamiento
    }

    fun getHoraActual(strFormato: String): String {
        val objCalendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(strFormato)
        return simpleDateFormat.format(objCalendar.time)

    }
}
