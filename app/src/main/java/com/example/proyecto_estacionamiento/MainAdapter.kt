package com.example.proyecto_estacionamiento

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.carros_row.view.*
import java.lang.reflect.Array


//Esta clase es para que los elementos se generen repetitivamente con datos especificos
class MainAdapter(val estacionamiento: Estacionamiento)/*(Carros: ArrayList<Array>)*/: RecyclerView.Adapter<CustomViewHolder>() {

    //val carros = Carros

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val lineOfRow = layoutInflater.inflate(R.layout.carros_row,parent,false)
        return CustomViewHolder(lineOfRow)
    }

    override fun getItemCount(): Int {
        /*if(estacionamiento.carros != null){
            return estacionamiento.carros!!.size
        }else return 0
        */
        return if(estacionamiento.carros != null){
            estacionamiento.carros!!.size
        }else 0
        //return carros.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        if(estacionamiento.carros != null){
            val autos = estacionamiento.carros!![position]

            if (autos.horaSalida!=""){ //Con esta condición verificamos si el auto se encuentra en el estacionamiento y si no es así (posee hora de salida) se marca con rojo
                holder.view.state_carro.setImageResource(R.drawable.bg_boton_redondo_rojo)
            }

            holder.view.matricula.text = autos.matricula
            holder.view.hora_entrada1.text = autos.horaEntrada
            holder.view.hora_entrada2.text = autos.horaSalida
            holder.view.marca_modelo.text = autos.marca + " " + autos.modelo

            holder.estacionamiento = estacionamiento
            holder.carro = autos
            holder.position = position
        }

    }
}


class CustomViewHolder(val view: View, var estacionamiento: Estacionamiento? = null, var carro: Automovil? = null, var position: Int? = null ): RecyclerView.ViewHolder(view){

    init {
        /*view.setOnLongClickListener {
            fun click(): Boolean {
                view.setBackgroundColor(Color.parseColor("#5054B4"))

                return true
            }

            click()
        }*/ //Retomar esta parte de codigo cuando se pueda eliminar el arreglo

        view.setOnClickListener{
            val intent = Intent(view.context,RegistroAutomovil::class.java)
            /*intent.putExtra("matricula",carros?.matricula)
            intent.putExtra("modelo",carros?.modelo)
            intent.putExtra("marca",carros?.marca)
            intent.putExtra("enHora",carros?.enHora)
            intent.putExtra("saHora",carros?.saHora)
            intent.putExtra("estado","Verificacion")*/
            intent.putExtra("Auto",carro)
            intent.putExtra("Estacionamiento",estacionamiento)
            intent.putExtra("index", position)
            view.context.startActivity(intent)

        }
    }


}