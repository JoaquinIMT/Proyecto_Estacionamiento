package com.example.proyecto_estacionamiento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView

class PruebaDesechable : AppCompatActivity() {

    lateinit var jsonThing: TextView
    lateinit var passActivity: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_desechable)


        jsonThing = findViewById(R.id.jsonthing)
        passActivity = findViewById(R.id.send)

        passActivity.setOnClickListener {
            val valor = jsonThing.text.toString().toInt()
            val intent = Intent(this,MainActivityReal::class.java)
            intent.putExtra("tipoE",valor)
            startActivity(intent)
            finishAffinity()

        }




    }
}
