package com.example.proyecto_estacionamiento


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_busqueda.*
import kotlinx.android.synthetic.main.fragment_primer_fragmento.*
import java.sql.Date
import java.sql.Time
import java.util.*

class FragmentoBusqueda(val estacionamiento: Estacionamiento) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_busqueda, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerview_carros.layoutManager = LinearLayoutManager(context?.applicationContext)

        recyclerview_carros.adapter = MainAdapter(estacionamiento)

    }

}
