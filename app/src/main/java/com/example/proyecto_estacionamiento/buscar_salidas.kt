package com.example.proyecto_estacionamiento

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.widget.*
import kotlin.toULong as toULong1
import java.util.*
import java.lang.Double.parseDouble
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Calendar.DAY_OF_YEAR


class buscar_salidas : AppCompatActivity() {

    var tiempo: Chronometer? = null
    var comenzar: Button? = null
    var pausar: Button? = null
    var ha: Button? = null
    var hs: Button? = null
    var hora: Date? = null
    var vt: TextView? = null
    var mili: Long? = null
    var mili2: Long? = null


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

        tiempo?.setOnChronometerTickListener { chronometer ->
            val time = SystemClock.elapsedRealtime() - chronometer.base
            val h = (time / 3600000).toInt()
            val m = (time - h * 3600000).toInt() / 60000
            val s = (time - (h * 3600000).toLong() - (m * 60000).toLong()).toInt() / 1000
            val t =
                (if (h < 10) "0$h" else h).toString() + ":" + (if (m < 10) "0$m" else m) + ":" + if (s < 10) "0$s" else s
            chronometer.text = t
        }
        ha?.setOnClickListener {
            /*
            hora = Date()
            var gg = getHoraActual("HH:mm")
            mili = hora?.time
            ha?.text = gg
            */
            //hora = Date()
            //var fechaActual = android.text.format.DateFormat.format("dd-MM-yyyy",hora!!.time)
            //ha?.text= fechaActual.toString()
            ha?.text = precio("15-06-2018","15:00","16-05-2019", "16:01")

        }

        hs?.setOnClickListener {
            hora = Date()
            var gg = getHoraActual("HH:mm")
            mili2 = hora?.time
            hs?.text = gg
            horaFinal(mili, mili2)
        }






        tiempo?.base = SystemClock.elapsedRealtime()
        tiempo?.text = ("00:00:00")

        comenzar?.setOnClickListener {
            comenzar?.isEnabled = false
            pausar?.isEnabled = true
            tiempo?.base = SystemClock.elapsedRealtime()
            tiempo?.start()
        }

        pausar?.setOnClickListener {
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


    fun horaFinal(entrada: Long?, salida: Long?) {
        var horaFinal = salida!! - entrada!!
        horaFinal = horaFinal.div(1000)
        horaFinal = horaFinal.div(60)
        vt?.text = horaFinal.toString()
    }

    fun precio(fechaEntrada: String, horaEntrada: String, fechaSalida: String, horaSalida: String): String {
        var mesTotal:Int=0
        var fechaEntradaSep = fechaEntrada.split("-")
        var fechaSalidaSep = fechaSalida.split("-")
        var año = fechaSalidaSep.get(2).toInt() - fechaEntradaSep.get(2).toInt()
        var calAño:Int= fechaSalida.get(1).toInt()
        if(calAño>=2){
            calAño = fechaSalidaSep.get(2).toInt()
        }else{
            calAño = fechaSalidaSep.get(2).toInt()-1
        }
        var mes = 0
        //AÑO
        if (año > 1) {
            if (fechaSalidaSep.get(1).toInt() < fechaEntradaSep.get(1).toInt()) {
                año = año - 1
                año = año * 8760 //año en horas
            }
        }
        //MES
        if (fechaSalidaSep.get(1).toInt() < fechaEntradaSep.get(1).toInt()) {
            mes = 12 - (fechaEntradaSep.get(1).toInt() - fechaSalidaSep.get(1).toInt())
            mesTotal= diasMes(mes,fechaEntradaSep.get(1).toInt(),calAño)
            mesTotal = mes * 24 //mes en horas
        } else {
            mes = fechaSalidaSep.get(1).toInt() - fechaEntradaSep.get(1).toInt()
            mesTotal= diasMes(mes,fechaEntradaSep.get(1).toInt(),calAño)
            mesTotal = mes * 24 //mes en horas
        }
        //DIAS


        var horaEntradaSep = horaEntrada.split(":")
        var horaSalidaSep = horaSalida.split(":")
        var entNum: Int = horaEntradaSep.get(0).toInt()
        var salNum: Int = horaSalidaSep.get(0).toInt()
        var resHora = (salNum - entNum) * 60
        entNum = horaEntradaSep.get(1).toInt()
        salNum = horaSalidaSep.get(1).toInt()
        var resMin = 0
        if (salNum > entNum) {
            resMin = salNum - entNum
        } else {
            resMin = (60 - entNum) + salNum
            resHora = resHora - 60
        }
        var resultado1: Int = (resHora + resMin) / 60
        var aux: Int = resultado1
        var resultado2: Double = (resHora.toDouble() + resMin.toDouble()) / 60
        var costo: Int
        if (resultado2 == aux.toDouble()) {
            costo = (resultado1) * 20
            return costo.toString()
        }

        costo = (resultado1 + 1) * 20
        return costo.toString()
    }

    fun diasMes(cont: Int, mesAct: Int, año :Int): Int {
        var aux = mesAct
        var mesTotal: Int = 0
        for (num in 1..cont) {
            when (aux) {
                1 -> mesTotal=mesTotal+31
                2 -> mesTotal=mesTotal+esBisiesto(año)
                3 -> mesTotal=mesTotal+31
                4 -> mesTotal=mesTotal+30
                5 -> mesTotal=mesTotal+31
                6 -> mesTotal=mesTotal+30
                7 -> mesTotal=mesTotal+31
                8 -> mesTotal=mesTotal+31
                9 -> mesTotal=mesTotal+30
                10 -> mesTotal=mesTotal+31
                11 -> mesTotal=mesTotal+30
                12 -> mesTotal=mesTotal+31
            }
            aux++
            if(aux>12){
                aux=1
            }
        }
        return mesTotal
    }
    fun esBisiesto(año:Int):Int {
        var cal:Calendar = Calendar.getInstance()
        cal.set(Calendar.YEAR, año)
        if(cal.getActualMaximum(DAY_OF_YEAR) > 365){
            return 29
        }
        else{
            return 28
        }

    }
}


