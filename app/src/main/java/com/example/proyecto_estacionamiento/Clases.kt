package com.example.proyecto_estacionamiento

import android.os.Parcelable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.parcel.Parcelize

class FragmentAdapter(manager: FragmentManager): FragmentPagerAdapter(manager) {

    private val listaFragmentos : MutableList<Fragment> = ArrayList()


    override fun getItem(position: Int): Fragment {
        return listaFragmentos[position]
    }

    override fun getCount(): Int {
        return listaFragmentos.size
    }

    fun newFragment(fragmento: Fragment){

        listaFragmentos.add(fragmento)

    }

}

@Parcelize
class Estacionamiento(var lugares: Int, var carros: MutableList<Automovil>?): Parcelable

@Parcelize
class Pasado(var carros: MutableList<Automovil>?): Parcelable

@Parcelize
class Automovil(var matricula: String, var marca: String, var modelo:String, var horaEntrada:String, var horaSalida:String, var selected:Boolean = false): Parcelable {
/*    private var matricula: String = ""
    //private var marca: String = ""
    private var modelo: String = ""
    private var horaEntrada: String = ""
    private var horaSalida: String= ""

    init {
        this.matricula = matricula
        //this.marca = marca
        this.modelo=modelo
        this.horaEntrada=horaEntrada
        this.horaSalida=horaSalida
    }*/

    /*fun setMatricula(matricula: String){
        this.matricula=matricula
    }
    fun getMatricula() : String{
        return matricula
    }
    fun setMarca(marca: String){
        this.marca=marca
    }
    fun getMarca() : String{
        return marca
    }
    fun setModelo(modelo: String){
        this.modelo=modelo
    }
    fun getModelo() : String{
        return modelo
    }
    fun setHoraEntrada(horaEntrada: String){
        this.horaEntrada=horaEntrada
    }
    fun getHoraEntrada() : String{
        return horaEntrada
    }
    fun setHoraSalida(horaSalida: String){
        this.horaSalida=horaSalida
    }
    fun getHoraSalida() : String{
        return horaSalida

    }*/






}
