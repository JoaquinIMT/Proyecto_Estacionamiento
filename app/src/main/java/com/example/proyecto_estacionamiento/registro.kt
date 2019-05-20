package com.example.proyecto_estacionamiento

class registro(matricula: String,marca: String,modelo:String,horaEntrada:String,horaSalida:String) {
    private var matricula: String = ""
    private var marca: String = ""
    private var modelo: String = ""
    private var horaEntrada: String = ""
    private var horaSalida: String= ""

    init {
        this.matricula = matricula
        this.marca = marca
        this.modelo=modelo
        this.horaEntrada=horaEntrada
        this.horaSalida=horaSalida
    }
    fun setMatricula(matricula: String){
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

    }






}
