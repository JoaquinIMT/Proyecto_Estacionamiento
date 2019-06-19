package com.example.proyecto_estacionamiento

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat.finishAffinity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_busqueda.*
import java.text.SimpleDateFormat
import java.util.*

class FragmentoBusqueda(var estacionamiento: Estacionamiento, var pasado: Pasado, val numeroDeSQLite: Int, val dbHandler: MindOrksDBOpenHelper) : Fragment(), CustomViewHolder.funcionloca {

    var lugar: Int = -1
    var carros = estacionamiento.carros
    var bye : MutableList<Int> = mutableListOf(0)
    lateinit var salirCarro :Button
    lateinit var reciclerView: RecyclerView

    /*override fun position(position: Int) {
        lugar = position //To change body of created functions use File | Settings | File Templates.
        Toast.makeText(context, lugar.toString(),Toast.LENGTH_LONG).show()
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_busqueda, container, false)

        salirCarro = view.findViewById(R.id.salida)
        // Inflate the layout for this fragment
        reciclerView = view.findViewById(R.id.recyclerview_carros)

        reciclerView.layoutManager = LinearLayoutManager(context?.applicationContext)

        reciclerView.adapter = MainAdapter(estacionamiento,this, pasado)


        salirCarro.setOnClickListener {
            if (bye.size > 1){

                bye.removeAt(0)
                //implement Dialog text

                val help = mutableListOf<Automovil>()

                val horaSalida = getHoraActual("HH:mm")

                for(i in bye){
                    help.add(carros!![i])
                }
                for(i in help){
                    exitParking(i,horaSalida)
                }
                val intent = Intent(view.context,MainActivityReal::class.java)
                intent.putExtra("Estacionamiento",estacionamiento)
                intent.putExtra("Pasado",pasado)
                intent.putExtra("NumeroDeSQLite",numeroDeSQLite)
                startActivity(intent)
                finishAffinity(MainActivityReal())
                //exitParking(estacionamiento,estacionamiento.carros!![lugar], getHoraActual("HH:mm"))


            }else{
                Toast.makeText(context, "Seleccione el automovil a salir", Toast.LENGTH_LONG).show()

            }

        }

        return view
    }


    override fun onCheckedBox(pos: Int, state:Boolean) {
        if(state){
            addToBye(pos)
        }else{
            removeFromBye(pos)
        }
    }

    private fun addToBye(index: Int) {
        bye.add(index)
    }

    private fun removeFromBye(index: Int){
        if(index==0){
            bye.removeAt(0)

        }else{
            bye.remove(index)
        }
    }

    fun exitParking(index: Automovil?, horaSalida: String){

        estacionamiento.carros?.remove(index)
        index?.horaSalida = horaSalida
        //estacionamiento.carros?.set(index!!,automovil)
        estacionamiento.lugares += 1

        pasado.carros?.add(index!!)

        dbHandler.dropElement(index!!) //retiramos el automovil de entradas

        dbHandler.addFields(index,false) //ponemos el automovil en salida

    }

    fun getHoraActual(strFormato: String): String {
        val objCalendar = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat(strFormato)
        return simpleDateFormat.format(objCalendar.time)

    }

}
