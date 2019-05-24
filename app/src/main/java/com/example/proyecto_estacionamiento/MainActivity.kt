package com.example.proyecto_estacionamiento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.INotificationSideChannel
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var entraCarro: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lugares = 21

        var estacionamiento: Estacionamiento? =  null

        val prueba: Estacionamiento? = intent.getParcelableExtra("Estacionamiento")

        estacionamiento = if (prueba != null){
            prueba
        } else{
            /*val automoviles = mutableListOf<Automovil>(Automovil(matricula = "ASW0M3",marca = "Toyota",modelo = "Corolla"
                ,horaEntrada = "11", horaSalida = "14")
                ,Automovil(matricula = "JOLUQFER",marca = "Nissan",modelo = "Versa",horaEntrada = "10", horaSalida = "15"))
            */
            Estacionamiento(lugares, null)
        }






        entraCarro = findViewById<Button>(R.id.nuevo_registro)

        val adapter = FragmentAdapter(supportFragmentManager)

        adapter.newFragment(PrimerFragmento(estacionamiento.lugares))
        adapter.newFragment(FragmentoBusqueda(estacionamiento!!))

        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)?.setIcon(R.drawable.ic_home_negra)
        tabs.getTabAt(1)?.setIcon(R.drawable.ic_search_negra)

        entraCarro.setOnClickListener {

            val intent = Intent(applicationContext,RegistroAutomovil::class.java)
            intent.putExtra("estado","Registro")
            intent.putExtra("Estacionamiento",estacionamiento)

            startActivity(intent)

        }



    }

    class FragmentAdapter(manager: FragmentManager): FragmentPagerAdapter(manager) {

        private val listaFragmentos : MutableList<Fragment> = ArrayList()


        override fun getItem(position: Int): Fragment {
             return listaFragmentos[position]
        }

        override fun getCount(): Int {
             return listaFragmentos.size
        }

        fun newFragment(fragmento: Fragment){

            listaFragmentos.add(fragmento)

        }

    }
}
