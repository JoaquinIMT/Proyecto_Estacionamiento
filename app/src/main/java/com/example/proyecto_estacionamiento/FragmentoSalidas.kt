package com.example.proyecto_estacionamiento

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class FragmentoSalidas(val pasado : Pasado) : Fragment() {

    lateinit var reciclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_fragmento_salidas, container, false)

        // Inflate the layout for this fragment
        reciclerView = view.findViewById(R.id.recyclerview_carros)

        reciclerView.layoutManager = LinearLayoutManager(context?.applicationContext)

        reciclerView.adapter = MainAdapter2(pasado)


        return view
    }


}
