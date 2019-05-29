package com.example.proyecto_estacionamiento

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast


class sqlLite(context: Context, name: String, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, name, factory, version) {
    private val NOMBRE_BD : String = "AUTOMOVIL.BD"
    private val VERSION_BD : Int = 1
    private val TABLA_AUTOMOVIL : String = "CREATE TABLE AUTOMOVIL(ID INTEGER PRIMARY KEY AUTOINCREMENT,MARCA TEXT,MODELO TEXT,MATRICULA TEXT,HORAENTRADA TEXT)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLA_AUTOMOVIL)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS"+TABLA_AUTOMOVIL)
        db.execSQL(TABLA_AUTOMOVIL)
    }
    fun agregarAutomovil (marca :String,modelo:String,matricula:String,horaEntrada:String){
        var db : SQLiteDatabase  = writableDatabase
        val registro = ContentValues()
        registro.put("MARCA",marca)
        registro.put("MODELO",modelo)
        registro.put("MATRICULA",matricula)
        registro.put("HORAENTRADA",horaEntrada)

        if(db!= null){
            db.insert("AUTOMOVIL", null, registro)
            db.close()

        }

    }

}