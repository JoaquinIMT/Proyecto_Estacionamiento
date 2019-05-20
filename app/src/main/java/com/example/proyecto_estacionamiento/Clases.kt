package com.example.proyecto_estacionamiento


class Estacionamiento(val lugares: Int, val carros: MutableList<Automovil>)

class Automovil(val matricula: String = "", val marca: String ="", val modelo: String ="", val enHora: Int, val saHora: Int)
