package com.example.proyecto_estacionamiento
import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper



class sqlLite(context: Context, NOMBRE_BD: String, factory: SQLiteDatabase.CursorFactory?, VERSION_BD: Int) : SQLiteOpenHelper(context, NOMBRE_BD, factory, VERSION_BD) {
    private val NOMBRE_BD : String = "AUTOMOVIL.BD"
    private val VERSION_BD : Int = 1
    private val TABLA_MAR : String = "CREATE TABLE MAR(MARCA TEXT,MODELO TEXT)"

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(TABLA_MAR)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS"+TABLA_MAR)
        db.execSQL(TABLA_MAR)
    }
    fun agregarMarmod (marca :String,modelo:String){
        var db : SQLiteDatabase  = writableDatabase
        val registro = ContentValues()
        registro.put("MARCA",marca)
        registro.put("MODELO",modelo)

        if(db!= null){
            db.insert("AUTOMOVIL", null, registro)
            db.close()

        }

    }
    fun guardar(marca: String, modelo: String): String {
        var mensaje = ""
        val database = this.writableDatabase
        val contenedor = ContentValues()
        contenedor.put("marca", marca)
        contenedor.put("modelo", modelo)
        try {
            database.insertOrThrow(MindOrksDBOpenHelper.TABLE_MARMOD, null, contenedor)
            mensaje = "Ingresado Correctamente"
        } catch (e: SQLException) {
            mensaje = "No Ingresado"
        }
        database.close()
        return mensaje
    }

}