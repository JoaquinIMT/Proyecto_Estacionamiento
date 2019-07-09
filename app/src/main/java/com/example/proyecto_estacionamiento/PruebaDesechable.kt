package com.example.proyecto_estacionamiento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.google.gson.GsonBuilder
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class PruebaDesechable : AppCompatActivity() {

    lateinit var jsonThing: TextView
    lateinit var passActivity: Button
    val dbHandler = MindOrksDBOpenHelper(this,null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_desechable)


        jsonThing = findViewById(R.id.jsonthing)
        passActivity = findViewById(R.id.send)

        passActivity.setOnClickListener {

            val valor: String = makeJson(jsonThing.text.toString().toInt())

            val datos : DatosIniciales = fetchJson(0,valor)

            saveJson(datos)

            val intent = Intent(this,MainActivityReal::class.java)
            startActivity(intent)
            finishAffinity()

        }
    }


    fun fetchJson(type: Int = 0, body: String /*Retirar parametros cuando se tenga url*/): DatosIniciales {

        var datos: DatosIniciales? = null

        if(type == 0){

            val gson = GsonBuilder().create()

            datos = gson.fromJson(body, DatosIniciales::class.java)

        }else{

            val url = ""

            val request = Request.Builder().url(url).build()

            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {

                override fun onResponse(call: okhttp3.Call, response: Response) {

                    val bodyOfJson = response.body?.string()

                    val gson = GsonBuilder().create()

                    datos = gson.fromJson(bodyOfJson, DatosIniciales::class.java)

                }

                override fun onFailure(call: okhttp3.Call, e: IOException) {
                    println("Failure")
                }

            })
        }

        return datos!!

    }

    private fun saveJson(datos: DatosIniciales){
        dbHandler.newType(datos)
    }

    private fun makeJson(valor: Int): String {

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
    "parkingName": "Fernando Estacionamiento",
    "workerName": "Raquel",
    "typeOfParking": 1,
    "parkingFee": [3.0,4.0],
    "slotsNumber": 20
}""".trimIndent()
        }else{
            """{
    "parkingName": "Raquel Estacionamiento",
    "workerName": "Fernando",
    "typeOfParking": 2,
    "parkingFee": [5.0,6.0],
    "slotsNumber": 30
}""".trimIndent()
        }

        return json
    }

}
