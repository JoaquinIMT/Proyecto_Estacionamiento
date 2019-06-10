package com.example.proyecto_estacionamiento

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Estacionamiento(var lugares: Int, var carros: MutableList<Automovil>?): Parcelable{

}

@Parcelize
class Automovil(var matricula: String, var marca: String, var modelo:String, var horaEntrada:String, var horaSalida:String): Parcelable
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