package com.example.proyecto_estacionamiento

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
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

        return estacionamiento.carros.size
        //return carros.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

    }


}


class CustomViewHolder(val view: View): RecyclerView.ViewHolder(view){

    init {
        view.setOnClickListener{

            view.setBackgroundColor(Color.parseColor("#5054B4"))
        }
    }


}