package com.example.proyecto_estacionamiento

import android.content.Context
import android.media.midi.MidiDevice
import android.widget.Toast

class CalculateExitValues(val rti: Long, val rto: Long, context:Context) {

    val dbHandler = MindOrksDBOpenHelper(context,null)

    fun getFee(): List<Fee> {

        val cursor = dbHandler.getFees()

        cursor!!.moveToFirst()

        val feeList : MutableList<Fee> = mutableListOf()

        if(cursor.count > 0){
            var block: Int = cursor.getInt(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_BLOQUE))
            var cost: Int = cursor.getInt(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_PRICE))
            var repeat: Int = cursor.getInt(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_REPEAT))

            var fee = Fee(block, cost.toFloat(), repeat)
            feeList.add(fee)


            while (cursor.moveToNext()) {
                block = cursor.getInt(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_BLOQUE))
                cost = cursor.getInt(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_PRICE))
                repeat = cursor.getInt(cursor.getColumnIndex(MindOrksDBOpenHelper.COLUMN_REPEAT))

                fee = Fee(block, cost.toFloat(), repeat)
                feeList.add(fee)

            }

        }


        cursor.close()

        return feeList
    }

    fun calculateTime(): List<String>{

        val timeInParking: Long = rto - rti

        val segundos = (timeInParking/1000)
        var min = (segundos/60)
        val horas = (min/60)
        val dias = horas/24

        val s = segundos % 60
        var m = min % 60
        var mn = min % 60
        val h = horas % 24

        val fee = getFee()
        //val timeList = fee[0]
//        val feeList = fee[1]
        var money = 0

        for (i in fee ){

            if (min < 0) break

            val cost = i.cost
            val time = i.time
            val repeat = i.repeats

            if(repeat != 0){

                for(j in 0 until repeat){

                    money += cost.toInt()
                    min -= time
                    if (min < 0) break
                }

            }else{
                while(true){
                    money += cost.toInt()
                    min -= time
                    if (min < 0) break
                }
            }


        }

        /*for(i in 0 until fee.size){

            if(timeList[i] == -1){
                money += feeList[i]
                break
            }
            money += feeList[i]
            min -= timeList[i]

            if(min < 0){
                break
            }

        }*/

        val timeArray = arrayListOf<Long>(s,mn,h,dias)

        val stringArray = timeArray.map {
            if(it < 10){
                return@map "0$it"
            }else{
                return@map it.toString()
            }

        }

        val time: String = "${stringArray[3]}, ${stringArray[2]}:${stringArray[1]}:${stringArray[0]}"
        val price: String = money.toString()
        return listOf(time, price)

    }

}