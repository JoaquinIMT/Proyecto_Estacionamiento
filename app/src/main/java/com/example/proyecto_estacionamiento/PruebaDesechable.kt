package com.example.proyecto_estacionamiento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

class PruebaDesechable : AppCompatActivity() {

    lateinit var jsonThing: TextView
    lateinit var userPassword: TextView
    lateinit var passActivity: Button
    val dbHandler = MindOrksDBOpenHelper(this,null)

    val url = "https://estacionamientos-dev.herokuapp.com/signin/employee/test"
    var registerMade: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_prueba_desechable)




        jsonThing = findViewById(R.id.jsonthing)
        passActivity = findViewById(R.id.send)

        passActivity.setOnClickListener {
            sendData()
            /*val valor: String = makeJson(jsonThing.text.toString().toInt())

            val datos : DatosIniciales = fetchJson(0,valor)


            if(datos.register!!){
                saveJson(datos)
                val intent = Intent(this,MainActivityReal::class.java)
                startActivity(intent)
                finishAffinity()
            }else{
                Toast.makeText(this,"Cuenta o contraseña no validos",Toast.LENGTH_SHORT).show()
            }*/

        }
    }

    fun sendData(){

        var datos: DatosIniciales?

        val name : String = jsonThing.text.toString()
//        val password : String = userPassword.text.toString()

        /*val json : String = """{
            "workerName" : "$name",
            "password" : "$password"
            }""".trimIndent()*/

        val json: String = """{
            "enroll_id": "2019030505",
            "password": "Martinez2004"
            }""".trimIndent() //4

        val body = json.toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())

        val request = Request.Builder().url(url).post(body).build()

        val client2 = OkHttpClient()

        client2.newCall(request).enqueue(object: Callback {

            override fun onResponse(call: Call, response: Response) {

                val bodyOfJson = response.body?.string()

                val gson = GsonBuilder().create()

                datos = gson.fromJson(bodyOfJson, DatosIniciales::class.java)
                if (datos?.register != null){

                    if(datos?.register!!){
                        println("Register made")
                        saveJson(datos!!)
                        makeRegisterFalse(0)
                    }else{
                        makeRegisterFalse(2)
                    }
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Fallo al intentar acceso")
                makeRegisterFalse(1)

            }

        })


        Handler().postDelayed(
            {
                if (registerMade == 0){

                    Toast.makeText(this,"Acceso concedido",Toast.LENGTH_SHORT).show()

                    val intent = Intent(this,MainActivityReal::class.java)
                    startActivity(intent)
                    finishAffinity()

                } else if(registerMade == 1){
                    Toast.makeText(this,"Fallo al intentar acceso",Toast.LENGTH_SHORT).show()

                }else if(registerMade == 2){

                    Toast.makeText(this,"Usuario o contraseña invalido",Toast.LENGTH_SHORT).show()

                }

            }
            ,50
        )


    }

    private fun makeRegisterFalse(case: Int){
        registerMade = case
    }


    fun fetchJson(type: Int = 0, body: String /*Retirar parametros cuando se tenga url*/): DatosIniciales {

        var datos: DatosIniciales? = null

        if(type == 0){

            val gson = GsonBuilder().create()

            datos = gson.fromJson(body, DatosIniciales::class.java)

        }else{

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
    "token": "ANAKAzsncasdAS",    
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
    "token": "ANAKAzsncasdAS",
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
    "token": "ANAKAzsncasdAS",
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
    "token": null,
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
