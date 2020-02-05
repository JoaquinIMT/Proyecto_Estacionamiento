package com.example.proyecto_estacionamiento

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.app.ActivityCompat.finishAffinity

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


import java.text.SimpleDateFormat
import java.util.*


class FragmentoBusqueda(var estacionamiento: Estacionamiento, val dbHandler: MindOrksDBOpenHelper,
                        val type: Int, val activity: MainActivityReal) : Fragment(), CustomViewHolder.interfazLoca {

    var carros = estacionamiento.carros
    var bye : MutableList<Automovil> = mutableListOf()
    lateinit var salirCarro :Button
    lateinit var reciclerView: RecyclerView
    lateinit var adapter: MainAdapter


    /*override fun position(position: Int) {
        lugar = position //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(context, lugar.toString(),Toast.LENGTH_LONG).show()
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_busqueda, container, false)
        val dbHandler = MindOrksDBOpenHelper(view.context, null)
        salirCarro = view.findViewById(R.id.salida)
        // Inflate the layout for this fragment
        reciclerView = view.findViewById(R.id.recyclerview_carros)
        reciclerView.layoutManager = LinearLayoutManager(context?.applicationContext)


        adapter = MainAdapter(estacionamiento,this, type, activity)

        reciclerView.adapter = adapter
        if(!dbHandler.checkWorkerActive()) {
            salirCarro.visibility = View.INVISIBLE
        }

        salirCarro.setOnClickListener {

            if (bye.size > 0){
                //implement Dialog text

                val help = bye

                val horaSalida = getHoraActual("HH:mm")

                for(i in help){
                    exitParking(i,horaSalida)
                }
                val intent = Intent(view.context,MainActivityReal::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                finishAffinity(MainActivityReal())
                //exitParking(estacionamiento,estacionamiento.carros!![lugar], getHoraActual("HH:mm"))


            }else{
                Toast.makeText(context, "Seleccione el automovil a salir", Toast.LENGTH_LONG).show()
            }

        }

        return view

    }


    override fun onCheckedBox(automovil: Automovil, state:Boolean) {

        if(state){
            addToBye(automovil)
        }else{
            removeFromBye(automovil)
        }

    }

    private fun addToBye(item: Automovil) {

        val sdf = SimpleDateFormat("yyy-MM-dd HH:mm:ss.SSS") //FORMAT USE IN THE TIME
        item.realTimeOut = sdf.format(Date())
        bye.add(item)
    }

    private fun removeFromBye(item: Automovil){
            bye.remove(item)
    }

    fun exitParking(index: Automovil?, horaSalida: String){

        index?.horaSalida = horaSalida

        dbHandler.dropElement(index!!) //retiramos el automovil de entradas

        dbHandler.addFields(index,false) //ponemos el automovil en salida

    }

    fun getHoraActual(strFormato: String): String {
        val objCalendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(strFormato)
        return simpleDateFormat.format(objCalendar.time)

    }
}
