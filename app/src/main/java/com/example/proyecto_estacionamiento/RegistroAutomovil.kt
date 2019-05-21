package com.example.proyecto_estacionamiento

import android.content.Context
import android.graphics.drawable.Drawable
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_registro_automovil.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
class RegistroAutomovil(): AppCompatActivity() {

    lateinit var tiempo : ProgressBar

    lateinit var matricula: TextView
    lateinit var marca: TextView
    lateinit var modelo: TextView
    lateinit var enHora: TextView
    lateinit var saHora: TextView
    lateinit var array : ArrayList<registro>
    lateinit var registro : Button
    lateinit var hora : Date
    var entradaMili : Long? = null



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_automovil)
        matricula = findViewById(R.id.matricula2)
        marca = findViewById(R.id.matricula)
        modelo = findViewById(R.id.modelo)
        registro = findViewById(R.id.nuevo_registro)
        enHora = findViewById(R.id.enhora)
        saHora = findViewById(R.id.sahora)

        val entrada = intent.getStringExtra("estado")

        if(entrada == "Registro") {

            enHora.text = getHoraActual("HH:mm")

            saHora.visibility = View.GONE
            reloj2.visibility = View.GONE

            registro?.setOnClickListener {
                var mat = matricula?.text.toString()
                var mar = marca?.text.toString()
                var mod = modelo?.text.toString()

                if (mat.equals("") && mar.equals("") && mod.equals("")) {


                    Toast.makeText(this@RegistroAutomovil, "Faltan Campos por completar", Toast.LENGTH_SHORT).show()


                } else {
                    hora = Date()
                    var horaEntrada = getHoraActual("HH:mm")
                    entradaMili = hora?.time
                    array.add(registro(mat, mar, mod, horaEntrada, " ")).toString()
                    matricula?.text = ""
                    marca?.text = ""
                    modelo?.text = ""
                    imprimirArray(array!!)
                }
            }

        }else{

            val valorMatricula = intent.getStringExtra("matricula")
            val valorMarca = intent.getStringExtra("marca")
            val valorModelo = intent.getStringExtra("modelo")



            registro.background = ContextCompat.getDrawable(this,R.drawable.bg_boton_redondo_rojo)
            registro.text = "Salida"

            matricula.text = valorMatricula
            marca.text = valorMarca
            modelo.text = valorModelo

        }


    }

    fun getHoraActual(strFormato: String): String {
        val objCalendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(strFormato)
        return simpleDateFormat.format(objCalendar.time)

    }
    fun imprimirArray(array : ArrayList<registro>){

        for (elemento in array){
            Toast.makeText(this@RegistroAutomovil, elemento.getMatricula()+elemento.getMarca()+elemento.getModelo()+elemento.getHoraEntrada()+elemento.getHoraSalida(), Toast.LENGTH_SHORT).show()
        }

    }


}
