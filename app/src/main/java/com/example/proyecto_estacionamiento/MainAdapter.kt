package com.example.proyecto_estacionamiento

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Array


//Esta clase es para que los elementos se generen repetitivamente con datos especificos
class MainAdapter/*(Carros: ArrayList<Array>)*/: RecyclerView.Adapter<CustomViewHolder>() {

    //val carros = Carros

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val lineOfRow = layoutInflater.inflate(R.layout.carros_row,parent,false)
        return CustomViewHolder(lineOfRow)
    }

    override fun getItemCount(): Int {

        return 10
        //return carros.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {

    }


}


class CustomViewHolder(view: View): RecyclerView.ViewHolder(view){



}