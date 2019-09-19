package com.example.proyecto_estacionamiento


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.fragment_primer_fragmento.*

class PrimerFragmento(val lugares: Int, val total: Float, val codigo: String, val generalEspecial: List<Int>) : Fragment() {

    lateinit var numberOfGenerals: TextView
    lateinit var numberOfEspecials: TextView
    lateinit var textEspecial: TextView
    lateinit var entraCarro: Button
    lateinit var espacios: TextView
    var progressBar: ProgressBar? = null



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_primer_fragmento, container, false)
        entraCarro = view.findViewById(R.id.nuevo_registro)
        // Inflate the layout for this fragment
        espacios = view.findViewById(R.id.espacios)
        val dbHandler = MindOrksDBOpenHelper(view.context, null)

        numberOfGenerals = view.findViewById(R.id.number_generals)
        numberOfEspecials = view.findViewById(R.id.number_especials)
        textEspecial = view.findViewById(R.id.textespecial)


        if(generalEspecial[1] != 0){
            numberOfEspecials.visibility = View.VISIBLE
            textEspecial.visibility = View.VISIBLE

        }


        numberOfGenerals.text = generalEspecial[0].toString()
        numberOfEspecials.text = generalEspecial[1].toString()

        espacios.text = lugares.toString()




        val quitar: Float = 100/total
        progressBar = view.findViewById(R.id.progressBar)

        progressBar?.progress=(quitar*lugares).toInt()

        //espacios.text=(quitar*estacionamiento.lugares).toString()

        if(!dbHandler.checkWorkerActive()) {
            entraCarro.visibility = View.INVISIBLE
        }

        if (lugares > 0){
            entraCarro.setOnClickListener {

                val intent = Intent(context,RegistroAutomovil::class.java)
                intent.putExtra("estado","Registro")
                intent.putExtra("codigo",codigo)

                startActivity(intent)

            }

        }else Toast.makeText(context,"Estacionamiento lleno", Toast.LENGTH_LONG).show()


        return view
    }


}
