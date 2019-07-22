package com.example.proyecto_estacionamiento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
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


            if(datos.register!!){
                saveJson(datos)
                val intent = Intent(this,MainActivityReal::class.java)
                startActivity(intent)
                finishAffinity()
            }else{
                Toast.makeText(this,"Cuenta o contraseña no validos",Toast.LENGTH_SHORT).show()
            }

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
    "register" : true,
    "parkingName": "Luis Estacionamiento",
    "workerName": "Joaquin",
    "typeOfParking": 0,
    "parkingFee":  [{
    "time":[0,60],"cost":10.0
    },{
    "time":[60,null],"cost":25.0
    }],
    "slotsNumber": 40
}""".trimIndent()
        } else if (valor == 1){
            """{
    "register" : true,
    "parkingName": "Fernando Estacionamiento",
    "workerName": "Raquel",
    "typeOfParking": 1,
    "parkingFee":  [{
    "time":[0,60],"cost":10.0
    },{
    "time":[60,null],"cost":25.0
    }],
    "slotsNumber": 20
}""".trimIndent()
        } else if (valor == 2){
            """{
    "register" : true,
    "parkingName": "Raquel Estacionamiento",
    "workerName": "Fernando",
    "typeOfParking": 2,
    "parkingFee": [{
    "time":[0,60],"cost":10.0
    },{
    "time":[60,null],"cost":25.0
    }],
    "slotsNumber": 30
}""".trimIndent()
        } else{
            """{
    "register" : false,
    "parkingName": null,
    "workerName": null,
    "typeOfParking": null,
    "parkingFee": null,
    "slotsNumber": null
}""".trimIndent()
        }

        return json
    }

}
