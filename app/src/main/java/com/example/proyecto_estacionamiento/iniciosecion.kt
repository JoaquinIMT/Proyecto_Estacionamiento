package com.example.proyecto_estacionamiento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_iniciosecion.*

class iniciosecion : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_iniciosecion)

        val textoContraseña= findViewById(R.id.textoContraseña) as TextView
        var valor = true
        botonOjo.setOnClickListener{
            if(valor == true){
                textoContraseña.setInputType(InputType.TYPE_CLASS_TEXT)
                botonOjo.setBackgroundResource(R.drawable.ojocontra)
                valor=false
            }
            else{
                textoContraseña.setInputType(InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD)
                botonOjo.setBackgroundResource(R.drawable.ojocontradesac2)
                valor=true
            }
        }
    }
}
