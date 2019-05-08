package com.example.proyecto_estacionamiento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import kotlin.toULong as toULong1
import android.widget.Chronometer.OnChronometerTickListener



class buscar_salidas : AppCompatActivity() {

    var tiempo: Chronometer? = null
    var comenzar: Button? = null
    var pausar: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_salidas)

        tiempo = findViewById(R.id.cronometro) as Chronometer
        comenzar = findViewById(R.id.empezar) as Button
        pausar = findViewById(R.id.pausar) as Button

        comenzar?.isEnabled = true
        pausar?.isEnabled = false

        tiempo?.setOnChronometerTickListener{ chronometer ->
            val time = SystemClock.elapsedRealtime() - chronometer.base
            val h = (time / 3600000).toInt()
            val m = (time - h * 3600000).toInt() / 60000
            val s = (time - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
            val t =
                (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
            chronometer.text = t
        }
        tiempo?.base=SystemClock.elapsedRealtime()
        tiempo?.text=("00:00:00")

        comenzar?.setOnClickListener{
            comenzar?.isEnabled = false
            pausar?.isEnabled = true
            tiempo?.base = SystemClock.elapsedRealtime()
            tiempo?.start()
        }

        pausar?.setOnClickListener{
            comenzar?.isEnabled = true
            pausar?.isEnabled = false
            tiempo?.stop()
        }

    }
}

