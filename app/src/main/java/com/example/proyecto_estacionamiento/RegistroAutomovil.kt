package com.example.proyecto_estacionamiento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast

class RegistroAutomovil : AppCompatActivity() {

    lateinit var tiempo : ProgressBar
    lateinit var registro : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_automovil)

        val estacionamiento = intent.getIntExtra("Estacionamiento, lugares", Int.MAX_VALUE)
        //val esta = intent.getParcelableArrayExtra("Estacionamiento, cosa")

        registro = findViewById(R.id.nuevo_registro)

        registro.setOnClickListener{

            Toast.makeText(this,estacionamiento.toString(),Toast.LENGTH_SHORT).show()

            /*val intent = Intent(applicationContext, MainActivity::class.java)

            intent.putExtra("estacionamiento", arrayListOf(automoviles))
*/
        }

    }
}
