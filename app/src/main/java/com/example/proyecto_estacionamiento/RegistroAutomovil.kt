package com.example.proyecto_estacionamiento

import android.content.ContentValues
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
    var registro : Button? = null
    var hora : Date? = null
    var entradaMili : Long? = null
    var cont : Int =0
    //var sqlLite = com.example.proyecto_estacionamiento.sqlLite()
    //var tabla: utilidades = utilidades()
    var sql : sqlLite = sqlLite(this,"AUTOMOVIL",null,1)

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
                sql.agregarAutomovil(marca?.text.toString(),modelo?.text.toString(),matricula?.text.toString(),horaEntrada)
                Toast.makeText(this, "Se cargaron los datos del artículo", Toast.LENGTH_SHORT).show()
                matricula?.text = ""
                marca?.text = ""
                modelo?.text = ""
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }



    }
    fun getHoraActual(strFormato: String): String {
        val objCalendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(strFormato)
        return simpleDateFormat.format(objCalendar.time)

    }
    /*
    fun imprimirArray(array : ArrayList<registro>){

        for (elemento in array){
            Toast.makeText(this@RegistroAutomovil, elemento.getMatricula()+elemento.getMarca()+elemento.getModelo()+elemento.getHoraEntrada()+elemento.getHoraSalida(), Toast.LENGTH_SHORT).show()
        }


    }*/
/*
    private fun registrarUsuarios() {
        val conn = ConexionSQLiteHelper(this, "bd_usuarios", null, 1)

        val db = conn.writableDatabase

        val values = ContentValues()
        //values.put(tabla.CAMPO_ID, campoId.getText().toString())
        values.put(tabla.CAMPO_MATRICULA, matricula?.getText().toString())
        values.put(tabla.CAMPO_MARCA, marca?.getText().toString())

        val idResultante = db.insert(Utilidades.TABLA_USUARIO, Utilidades.CAMPO_ID, values)

        Toast.makeText(applicationContext, "Id Registro: $idResultante", Toast.LENGTH_SHORT).show()
        db.close()
    }*/


}
