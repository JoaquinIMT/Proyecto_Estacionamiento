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
            val valor: String = makeJson(jsonThing.text.toString().toInt())
            val intent = Intent(this,MainActivityReal::class.java)
            intent.putExtra("json",valor)
            startActivity(intent)
            finishAffinity()

        }
    }

    fun makeJson(valor: Int): String {

        val json: String =  if(valor == 0){
            """{
    "parkingName": "Luis Estacionamiento",
    "workerName": "Joaquin",
    "typeOfParking": 0,
    "parkingFee": [1.0,2.0],
    "slotsNumber": 40
}""".trimIndent()
        } else if (valor == 1){
            """{
    "parkingName": "Luis Estacionamiento",
    "workerName": "Joaquin",
    "typeOfParking": 1,
    "parkingFee": [1.0,2.0],
    "slotsNumber": 20
}""".trimIndent()
        }else{
            """{
    "parkingName": "Luis Estacionamiento",
    "workerName": "Joaquin",
    "typeOfParking": 2,
    "parkingFee": [1.0,2.0],
    "slotsNumber": 30
}""".trimIndent()
        }

        return json
    }

}
