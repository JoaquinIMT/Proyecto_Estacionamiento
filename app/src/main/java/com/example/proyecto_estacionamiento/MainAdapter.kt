package com.example.proyecto_estacionamiento

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.BoringLayout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.carros_row.view.*
import java.lang.reflect.Array
import kotlin.coroutines.coroutineContext


//Esta clase es para que los elementos se generen repetitivamente con datos especificos
class MainAdapter(var estacionamiento: Estacionamiento,val context: FragmentoBusqueda, val pasado: Pasado): RecyclerView.Adapter<CustomViewHolder>(),Filterable {

    val carros = if(estacionamiento.carros != null){

        estacionamiento.carros!!

    }else{

        mutableListOf()

    }

    var filterList: List<Automovil> = carros

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {

                val text: String = constraint.toString()
                if(text.isEmpty()) {

                    filterList = carros

                }else{

                    val returnList = ArrayList<Automovil>()
                    for(row in carros){
                        if(row.marca.toLowerCase().contains(text.toLowerCase()) ||
                            row.matricula.toLowerCase().contains(text.toLowerCase()) ||
                                    row.modelo.toLowerCase().contains(text.toLowerCase()))  returnList.add(row)
                    }

                    filterList = returnList
                }

                val filterResults = Filter.FilterResults()
                filterResults.values = filterList

                return filterResults

            }

            override fun publishResults(constraint: CharSequence?, filterResults: FilterResults?) {

                filterList = filterResults?.values as List<Automovil>
                notifyDataSetChanged()

            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val lineOfRow = layoutInflater.inflate(R.layout.carros_row,parent,false)
        return CustomViewHolder(lineOfRow,cosa = context)
    }

    override fun getItemCount(): Int {

        return if(filterList.isEmpty()){
            0
        }else filterList.size

    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if(filterList.isNotEmpty()){
            val autos = filterList[position]

            if (autos.horaSalida!=""){ //Con esta condición verificamos si el auto se encuentra en el estacionamiento y si no es así (posee hora de salida) se marca con rojo
                holder.view.state_carro.setImageResource(R.drawable.bg_boton_redondo_rojo)
            }


            holder.view.matricula.text = autos.matricula
            holder.view.hora_entrada1.text = autos.horaEntrada
            holder.view.hora_entrada2.text = "--:--"
            holder.view.marca_modelo.text = autos.marca + " " + autos.modelo

            holder.estacionamiento = estacionamiento
            holder.carro = autos
            holder.position = position

            holder.cosa = context
            holder.pasado = pasado

            holder.hold = false

            holder.numeroDeSQLite = autos._id

        }
    }
}


class CustomViewHolder(var view: View, var estacionamiento: Estacionamiento? = null,
                       var carro: Automovil? = null, var position: Int? = null,
                       var cosa: FragmentoBusqueda? = null, var pasado: Pasado? = null,
                       var hold: Boolean = false, var numeroDeSQLite: Int? = null ): RecyclerView.ViewHolder(view){


    var activityCallBack : funcionloca? = null


    interface funcionloca {
        fun onCheckedBox(pos : Int,state: Boolean)
    }


    var selected: Int? = null

    init {

        if(cosa != null){
            activityCallBack = cosa!!
        }

        view.setOnClickListener{
            val intent = Intent(view.context,RegistroAutomovil::class.java)
            intent.putExtra("Auto",carro)
            intent.putExtra("Pasado",pasado)
            intent.putExtra("Estacionamiento",estacionamiento)
            intent.putExtra("index", position)
            intent.putExtra("numeroDeSQLite",numeroDeSQLite)
            view.context.startActivity(intent)

        }
        view.setOnLongClickListener {
            if(hold){
                view.state_carro.setImageResource(R.drawable.bg_boton_redondo_azul)// = ContextCompat.getDrawable(view.context,R.drawable.bg_boton_redondo_rojo)
                activityCallBack?.onCheckedBox(position!!,false)
                hold = false
            }else{
                view.state_carro.setImageResource(R.drawable.bg_boton_redondo_rojo)// = ContextCompat.getDrawable(view.context,R.drawable.bg_boton_redondo_rojo)
                activityCallBack?.onCheckedBox(position!!,true)
                hold = true
            }

            //Toast.makeText(view.context,"Holi", Toast.LENGTH_SHORT).show()
            true
        }


        /*view.setOnLongClickListener {
            fun click(): Boolean {
                view.setBackgroundColor(Color.parseColor("#5054B4"))

                return true
            }

            click()
        }*/ //Retomar esta parte de codigo cuando se pueda eliminar el arreglo


    }


}




class MainAdapter2(val pasado: Pasado)/*(Carros: ArrayList<Array>)*/: RecyclerView.Adapter<CustomViewHolder2>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder2 {
        val layoutInflater = LayoutInflater.from(parent.context)
        val lineOfRow = layoutInflater.inflate(R.layout.carros_row,parent,false)
        return CustomViewHolder2(lineOfRow)
    }

    override fun getItemCount(): Int {
        return if(pasado.carros != null){
            pasado.carros!!.size
        }else 0
    }

    override fun onBindViewHolder(holder: CustomViewHolder2, position: Int) {
        if(pasado.carros != null){
            val autos = pasado.carros!![position]

            if (autos.horaSalida!=""){ //Con esta condición verificamos si el auto se encuentra en el estacionamiento y si no es así (posee hora de salida) se marca con rojo
                holder.view.state_carro.setImageResource(R.drawable.bg_boton_redondo_rojo)
            }

            holder.view.matricula.text = autos.matricula
            holder.view.hora_entrada1.text = autos.horaEntrada
            holder.view.hora_entrada2.text = autos.horaSalida
            holder.view.marca_modelo.text = autos.marca + " " + autos.modelo

            holder.carro = autos
            holder.position = position


        }

    }
}


class CustomViewHolder2(var view: View, var carro: Automovil? = null,
                        var position: Int? = null, var cosa: FragmentoBusqueda? = null): RecyclerView.ViewHolder(view){
    init {

        view.setOnClickListener{
            val intent = Intent(view.context,RegistroAutomovil::class.java)
            intent.putExtra("Auto",carro)
            intent.putExtra("Pasado",Pasado(mutableListOf()))
            intent.putExtra("Estacionamiento",Estacionamiento(0, mutableListOf()))
            intent.putExtra("index", position)
            view.context.startActivity(intent)

        }

    }


}
