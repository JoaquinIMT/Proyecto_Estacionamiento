package com.example.proyecto_estacionamiento

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.webkit.ConsoleMessage
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_iniciosecion.*
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.Console
import java.io.IOException
import java.lang.Exception

class iniciosecion : AppCompatActivity() {

    lateinit var userName: TextView
    lateinit var userPassword: TextView
    lateinit var passActivity: Button
    val dbHandler = MindOrksDBOpenHelper(this,null)

    val url = "http://159.89.95.102/api/car/login" //"https://estacionamientos-dev.herokuapp.com/signin/employee/test"
    //val url = "http://192.168.1.129:8000/api/car/login"
    var registerMade: Int = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciosecion)
        userName = findViewById(R.id.textoCorreo)
        userPassword = findViewById(R.id.textoContraseña)
        passActivity = findViewById(R.id.botonlogin)

        if(dbHandler.checkLogIn()){
            val intent = Intent(this,MainActivityReal::class.java)
            startActivity(intent)
            finishAffinity()
        }

        var valor = true

        botonOjo.setOnClickListener{
            if(valor == true){
                userPassword.setInputType(InputType.TYPE_CLASS_TEXT)
                botonOjo.setBackgroundResource(R.drawable.ojocontra)
                valor=false
            }
            else{
                userPassword.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                botonOjo.setBackgroundResource(R.drawable.ojocontradesac2)
                valor=true
            }
        }

        passActivity.setOnClickListener {
            if(hasText()){
                sendData()
                passActivity.isEnabled = true
            }else{
                Toast.makeText(this,"Llene ambos campos",Toast.LENGTH_SHORT).show()
            }
        }


    }

    private fun hasText(): Boolean{
        return (userName.text.isNotBlank() && userPassword.text.isNotEmpty())
    }

    private fun sendData(){

        passActivity.isEnabled = false

        var datos: DatosIniciales?

        val name : String = userName.text.toString()
        val password : String = userPassword.text.toString()

        val json : String = """{
            "name" : "$name",
            "password" : "$password"
            }""".trimIndent()

        /*val json: String = """{
            "enroll_id": "2019030505",
            "password": "Martinez2004"
            }""".trimIndent() //4*/

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
                        handleResponse(0,datos!!)
                    }else{
                        handleResponse(2,DatosIniciales(null,null,null,null,null))
                    }
                }else{
                    handleResponse(2,DatosIniciales(null,null,null,null,null))
                }

            }

            override fun onFailure(call: Call, e: IOException) {
                println("Fallo al intentar acceso 111")

                handleResponse(1, DatosIniciales(null,null,null,null,null), e)

            }

        })
        /*Handler().postDelayed(
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
        )*/

    }

    private fun handleResponse(case: Int, datos: DatosIniciales, exception: IOException? = null){

        runOnUiThread {
            if (case == 0){

                dbHandler.newType(datos,this)
                datos.parkingFee!!.forEachIndexed { index, fee ->  run{
                    if(index!=0){
                        dbHandler.insertMoreCosts(fee)
                    }
                } }
                dbHandler.changeLogStatus(true)
                dbHandler.limpiarRegistros()
                Toast.makeText(this,"Acceso concedido",Toast.LENGTH_SHORT).show()

                val intent = Intent(this,MainActivityReal::class.java)
                startActivity(intent)
                finishAffinity()

            } else if(case == 1){
                Toast.makeText(this, "Conexión no establecida" ,Toast.LENGTH_SHORT).show()
                passActivity.isEnabled = true
            }else if(case == 2){

                Toast.makeText(this,"Usuario o contraseña invalido",Toast.LENGTH_SHORT).show()
                passActivity.isEnabled = true
            }
        }

    }

    private fun saveJson(datos: DatosIniciales){

    }
}
