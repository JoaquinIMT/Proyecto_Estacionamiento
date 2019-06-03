package com.example.proyecto_estacionamiento


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_primer_fragmento.*

class PrimerFragmento(val estacionamiento: Estacionamiento, val pasado: Pasado) : Fragment() {

    lateinit var entraCarro: Button
    lateinit var espacios: TextView



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_primer_fragmento, container, false)
        entraCarro = view.findViewById(R.id.nuevo_registro)
        // Inflate the layout for this fragment
        espacios = view.findViewById(R.id.espacios)

        espacios.text = estacionamiento.lugares.toString()

        if (estacionamiento.lugares > 0){
            entraCarro.setOnClickListener {

                val intent = Intent(context,RegistroAutomovil::class.java)
                intent.putExtra("estado","Registro")
                intent.putExtra("Estacionamiento",estacionamiento)
                intent.putExtra("Pasado",pasado)

                startActivity(intent)

            }
        }else Toast.makeText(context,"Estacionamiento lleno", Toast.LENGTH_LONG).show()


        return view
    }


}
