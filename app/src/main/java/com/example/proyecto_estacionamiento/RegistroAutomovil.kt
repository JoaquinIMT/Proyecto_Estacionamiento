package com.example.proyecto_estacionamiento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
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
    //lateinit var array : ArrayList<Automovil>
    lateinit var array : Automovil //Lista con los datos del automovil para agregar al array mayor
    lateinit var estacionamiento: Estacionamiento
    lateinit var registro : Button //Boton abajo de la pantalla para concluir cambios
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


        estacionamiento = intent.getParcelableExtra("Estacionamiento")


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
                    //array.add(Automovil(mat, mar, mod, horaEntrada, " ")).toString()

                    //checamos que cuando mandemos llamar la lista con automoviles esta ya tenga registrado a un automovil
                    // de otra forma este arreglo se define como el primero

                    if (estacionamiento.carros != null){

                        addList(mat, mar, mod, horaEntrada)

                    }else{

                        createList(mat, mar, mod, horaEntrada)

                    }

                    intent(estacionamiento)
                    /*matricula?.text = ""
                    marca?.text = ""
                    modelo?.text = ""
                    imprimirArray(array!!)*/

                }
            }

        }else{

            var automovil = intent.getParcelableExtra<Automovil>("Auto")

            if(automovil.horaSalida != ""){
                saHora.text = automovil.horaSalida
                registro.visibility = View.GONE

            }else saHora.text = "--:--"

            registro.background = ContextCompat.getDrawable(this,R.drawable.bg_boton_redondo_rojo)
            registro.text = "Salida"

            matricula.text = automovil.matricula
            marca.text = automovil.marca
            modelo.text = automovil.modelo
            enHora.text = automovil.horaEntrada


            registro.setOnClickListener {
                val mat = matricula?.text.toString()
                val mar = marca?.text.toString()
                val mod = modelo?.text.toString()

                hora = Date()

                val horaSalida = getHoraActual("HH:mm")

                entradaMili = hora?.time

                exitParking(automovil,horaSalida)

                intent(estacionamiento)

            }

        }

    }

    fun getHoraActual(strFormato: String): String {
        val objCalendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(strFormato)
        return simpleDateFormat.format(objCalendar.time)

    }

    fun imprimirArray(array : ArrayList<Automovil>){

        for (elemento in array){
            //Toast.makeText(this@RegistroAutomovil, elemento.getMatricula()+elemento.getMarca()+elemento.getModelo()+elemento.getHoraEntrada()+elemento.getHoraSalida(), Toast.LENGTH_SHORT).show()
            Toast.makeText(this@RegistroAutomovil, elemento.matricula+elemento.marca+elemento.modelo+elemento.horaEntrada+elemento.horaSalida, Toast.LENGTH_SHORT).show()
        }

    }

    fun intent(estacionamiento1: Estacionamiento){

        val intent = Intent(applicationContext,MainActivity::class.java)
        intent.putExtra("Estacionamiento",estacionamiento1)
        startActivity(intent)
    }

    fun createList( mat:String , mar: String, mod: String, horaEntrada: String ){

        array = Automovil(mat, mar, mod, horaEntrada, "")
        estacionamiento.carros = mutableListOf(array)
        estacionamiento.lugares -= 1

    }

    fun addList(mat:String , mar: String, mod: String, horaEntrada: String ){
        /*if (estacionamiento.carros?.size == 2){
            estacionamiento.carros?.reverse()
        }*/

        array = Automovil(mat, mar, mod, horaEntrada, "")
        estacionamiento.carros?.reverse()
        estacionamiento.carros?.add(array)
        estacionamiento.carros?.reverse()
        estacionamiento.lugares -= 1
    }

    fun exitParking(automovil:Automovil, horaSalida: String){
        val index = intent.getIntExtra("index", Int.MAX_VALUE)

        estacionamiento.carros?.removeAt(index)

        automovil.horaSalida = horaSalida

        estacionamiento.carros?.add(automovil)

        //estacionamiento.carros?.set(index!!,automovil)

        estacionamiento.lugares += 1

    }

}
