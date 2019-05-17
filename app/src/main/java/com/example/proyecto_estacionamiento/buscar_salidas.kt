package com.example.proyecto_estacionamiento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.Button
import android.widget.Chronometer
import kotlin.toULong as toULong1
import android.widget.Toast
import java.util.*
import android.widget.TextView
import java.lang.Double.parseDouble
import java.text.SimpleDateFormat


class buscar_salidas : AppCompatActivity() {

    var tiempo: Chronometer? = null
    var comenzar: Button? = null
    var pausar: Button? = null
    var ha: Button? = null
    var hs: Button? = null
    var hora : Date? = null
    var vt : TextView? = null
    var mili : Long? = null
    var mili2 : Long? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buscar_salidas)

        tiempo = findViewById(R.id.cronometro) as Chronometer
        comenzar = findViewById(R.id.empezar) as Button
        pausar = findViewById(R.id.pausar) as Button
        ha = findViewById(R.id.ha) as Button
        hs = findViewById(R.id.hs) as Button
        vt = findViewById(R.id.vt) as TextView

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
        ha?.setOnClickListener{
            hora = Date()
            var gg = getHoraActual("HH:mm")
            mili = hora?.time
            ha?.text = gg

        }

        hs?.setOnClickListener{
            hora = Date()
            var gg = getHoraActual("HH:mm")
            mili2 = hora?.time
            hs?.text = gg
            horaFinal(mili,mili2)
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
            showElapsedTime()
        }

    }
    private fun showElapsedTime() {
        val time2 = SystemClock.elapsedRealtime() - tiempo!!.base
        val h = (time2 / 3600000).toInt()
        val m = (time2 - h * 3600000).toInt() / 60000
        val s = (time2 - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
        val t =
            (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s

        Toast.makeText(
            this@buscar_salidas, "Tiempo total: $t",
            Toast.LENGTH_SHORT
        ).show()
    }
    fun getHoraActual(strFormato: String): String {

        val objCalendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(strFormato)

        return simpleDateFormat.format(objCalendar.time)

    }



    fun horaFinal(entrada:Long?,salida:Long?){
        var  horaFinal = salida!!- entrada!!
        horaFinal = horaFinal.div(1000)
        horaFinal = horaFinal.div(60)
        vt?.text= horaFinal.toString()
    }



}

