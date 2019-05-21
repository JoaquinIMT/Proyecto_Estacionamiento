package com.example.proyecto_estacionamiento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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

        val automoviles = mutableListOf<Automovil>(Automovil(matricula = "ASW0M3",marca = "Toyota",modelo = "Corolla",enHora = 11, saHora = 14),Automovil(matricula = "JOLUQFER",marca = "Nissan",modelo = "Versa",enHora = 10, saHora = 15))

        val estacionamiento: Estacionamiento = Estacionamiento(21, automoviles)



        entraCarro = findViewById<Button>(R.id.nuevo_registro)

        val adapter = FragmentAdapter(supportFragmentManager)

        adapter.newFragment(PrimerFragmento())
        adapter.newFragment(FragmentoBusqueda(estacionamiento))

        viewPager.adapter = adapter

        tabs.setupWithViewPager(viewPager)

        tabs.getTabAt(0)?.setIcon(R.drawable.ic_home_negra)
        tabs.getTabAt(1)?.setIcon(R.drawable.ic_search_negra)

        entraCarro.setOnClickListener {

            val intent = Intent(applicationContext,RegistroAutomovil::class.java)
            intent.putExtra("estado","Registro")

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
