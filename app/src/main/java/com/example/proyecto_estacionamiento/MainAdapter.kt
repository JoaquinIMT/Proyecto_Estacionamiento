package com.example.proyecto_estacionamiento

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.lang.reflect.Array


//Esta clase es para que los elementos se generen repetitivamente con datos especificos
class MainAdapter(Carros: ArrayList<Array>): RecyclerView.Adapter<CustomViewHolder>() {

    val carros = Carros

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomViewHolder {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItemCount(): Int {

        return carros.size
    }

    override fun onBindViewHolder(holder: CustomViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}


class CustomViewHolder(view: View): RecyclerView.ViewHolder(view){



}