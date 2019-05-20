package com.example.proyecto_estacionamiento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
class RegistroAutomovil : AppCompatActivity() {

    lateinit var tiempo : ProgressBar

    var matricula: TextView? = null
    var marca: TextView? = null
    var modelo: TextView? = null
    var array : ArrayList<registro> = ArrayList ()
    var registro : Button? = null
    var hora : Date? = null
    var entradaMili : Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_automovil)

        matricula = findViewById(R.id.matricula2) as TextView
        marca = findViewById(R.id.marca) as TextView
        modelo = findViewById(R.id.modelo) as TextView
        registro = findViewById(R.id.nuevo_registro) as Button


        registro?.setOnClickListener{
            var mat = matricula?.text.toString()
            var mar = marca?.text.toString()
            var mod = modelo?.text.toString()

            if(mat.equals("") && mar.equals("") && mod.equals("")){


                Toast.makeText(this@RegistroAutomovil, "Faltan Campos por completar", Toast.LENGTH_SHORT).show()


            }
            else{
                hora = Date()
                var horaEntrada = getHoraActual("HH:mm")
                entradaMili = hora?.time
                array.add(registro(mat,mar,mod,horaEntrada," ")).toString()
                matricula?.text = ""
                marca?.text = ""
                modelo?.text = ""
                imprimirArray(array!!)
            }
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
