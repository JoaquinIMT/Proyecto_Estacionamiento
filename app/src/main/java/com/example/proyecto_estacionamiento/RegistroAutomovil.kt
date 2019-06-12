package com.example.proyecto_estacionamiento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.strictmode.SqliteObjectLeakedViolation
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
    lateinit var pasado : Pasado

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
        pasado = intent.getParcelableExtra("Pasado")

        var numeroDeSQLite : Int? = intent.getIntExtra("Numero_de_SQLite",Int.MAX_VALUE)
        if(numeroDeSQLite != null){
            numeroDeSQLite += 1
        }

        val entrada = intent.getStringExtra("estado")

        val dbHandler = MindOrksDBOpenHelper(this, null)


        val actionBar = supportActionBar //Declaramos la barrra superior para su uso
        actionBar?.setDisplayHomeAsUpEnabled(true) //Activamos el icono de regreso de actividad


        if(entrada == "Registro") { //Al entrar significa que se hará un registro


            //Ocultamos el reloj de salida, dado que solo se pondra la hora actual
            saHora.visibility = View.GONE
            reloj2.visibility = View.GONE

            //Se pone la hora actual en el textView
            enHora.text = getHoraActual("HH:mm")

            //Se ajusta el nombre del boton
            actionBar?.title = "Registro de automovil"


            registro.setOnClickListener {

                //Obtenemos el texto de los EditText
                val mat = matricula.text.toString()
                val mar = marca.text.toString()
                val mod = modelo.text.toString()

                //Toast.makeText(this, mat + "Added to database", Toast.LENGTH_LONG).show()


                if (mat.equals("") && mar.equals("") && mod.equals("")) {


                    Toast.makeText(this@RegistroAutomovil, "Faltan Campos por completar", Toast.LENGTH_SHORT).show()


                } else {

                    hora = Date()
                    var horaEntrada = getHoraActual("HH:mm")
                    entradaMili = hora?.time

                    val automovil = Automovil(mat,mar,mod,horaEntrada,"", numeroDeSQLite!!)
                    dbHandler.addFields(automovil,true)

                    //checamos que cuando mandemos llamar la lista con automoviles esta ya tenga registrado a un automovil
                    // de otra forma este arreglo se define como el primero

                    if (estacionamiento.carros != null){

                        addList(mat, mar, mod, horaEntrada, numeroDeSQLite)

                    }else{

                        createList(mat, mar, mod, horaEntrada)

                    }

                    intent(estacionamiento)

                }
            }

        }else{ //Si entra aquí tendrá una salida o esta checando información

            var automovil = intent.getParcelableExtra<Automovil>("Auto")

            if(automovil.horaSalida != ""){
                saHora.text = automovil.horaSalida
                registro.visibility = View.GONE

                actionBar?.title = "Detalles de automovil"

            }else{

                saHora.text = "--:--"
                registro.background = ContextCompat.getDrawable(this,R.drawable.bg_boton_redondo_rojo)
                registro.text = "Salida"

                actionBar?.title = "Salida de automovil"

            }


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

                exitParking(automovil,horaSalida,dbHandler)

                intent(estacionamiento)

            }

        }

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun getHoraActual(strFormato: String): String {
        val objCalendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(strFormato)
        return simpleDateFormat.format(objCalendar.time)

    }

    fun imprimirArray(array : ArrayList<Automovil>){

        for (elemento in array){
            Toast.makeText(this@RegistroAutomovil, elemento.matricula+elemento.marca+elemento.modelo+elemento.horaEntrada+elemento.horaSalida, Toast.LENGTH_SHORT).show()
        }

    }

    fun intent(estacionamiento1: Estacionamiento){

        val intent = Intent(applicationContext,MainActivityReal::class.java)
        intent.putExtra("Estacionamiento",estacionamiento1)
        intent.putExtra("Pasado",pasado)
        startActivity(intent)
        finishAffinity()
    }

    fun createList( mat:String , mar: String, mod: String, horaEntrada: String ){

        array = Automovil(mat, mar, mod, horaEntrada, "",0)
        estacionamiento.carros = mutableListOf(array)
        estacionamiento.lugares -= 1

    }

    fun addList(mat:String , mar: String, mod: String, horaEntrada: String, numeroDeSQLite: Int ){

        array = Automovil(mat, mar, mod, horaEntrada, "", numeroDeSQLite )
        estacionamiento.carros?.add(array)
        estacionamiento.lugares -= 1
    }

    fun exitParking(automovil:Automovil, horaSalida: String, dbHandler: MindOrksDBOpenHelper){

        val index = intent.getIntExtra("index", Int.MAX_VALUE)
        //val numeroIDSQLite = intent.getIntExtra("NumeroDeSQLite",Int.MAX_VALUE)

        estacionamiento.carros?.removeAt(index)

        dbHandler.dropElement(automovil) //retiramos el automovil de entradas

        automovil.horaSalida = horaSalida

        dbHandler.addFields(automovil,false) //ponemos el automovil en salida

        pasado.carros?.add(automovil)

        estacionamiento.lugares += 1

    }

}
