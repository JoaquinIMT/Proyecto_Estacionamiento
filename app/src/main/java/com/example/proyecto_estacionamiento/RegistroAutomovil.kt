package com.example.proyecto_estacionamiento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar

class RegistroAutomovil : AppCompatActivity() {

    lateinit var tiempo : ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registro_automovil)

    }
}
