package com.example.proyecto_estacionamiento

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
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

class MindOrksDBOpenHelper(context: Context, factory: SQLiteDatabase.CursorFactory?) :
    SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE = ("CREATE TABLE " +
                TABLE_ESTACIONAMIENTO +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_MATRICULA + " TEXT,"
                + COLUMN_MARCA + " TEXT,"
                + COLUMN_MODELO + " TEXT,"
                + COLUMN_HORAE + " TEXT,"
                + COLUMN_HORASA + " TEXT" +")")

        db.execSQL(CREATE_PRODUCTS_TABLE)
    }

    fun onCreateSalida(db: SQLiteDatabase) {

        val CREATE_PRODUCTS_TABLE_SALIDA = ("CREATE TABLE " +
                TABLE_SALIDA +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_MATRICULA + " TEXT,"
                + COLUMN_MARCA + " TEXT,"
                + COLUMN_MODELO + " TEXT,"
                + COLUMN_HORAE + " TEXT,"
                + COLUMN_HORASA + " TEXT" +")")

        db.execSQL(CREATE_PRODUCTS_TABLE_SALIDA)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTACIONAMIENTO)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALIDA)
        onCreate(db)
        onCreateSalida(db)
    }

    fun addFields(automovil: Automovil, tipo: Boolean){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_MATRICULA,automovil.matricula)
        values.put(COLUMN_MARCA,automovil.marca)
        values.put(COLUMN_MODELO,automovil.modelo)
        values.put(COLUMN_HORAE,automovil.horaEntrada)
        values.put(COLUMN_HORASA,automovil.horaSalida)

        if(tipo){

            db.insert(TABLE_ESTACIONAMIENTO, null, values)
        }else{
            db.insert(TABLE_SALIDA,null,values)
        }
        db.close()
    }

    fun dropElement(automovil: Automovil){
        val db = this.writableDatabase

        db.execSQL("DELETE FROM $TABLE_ESTACIONAMIENTO WHERE $COLUMN_MATRICULA='${automovil.matricula}' AND $COLUMN_MODELO='${automovil.modelo}'")
    }

    fun modify(index: Int, automovil: Automovil){
        val db = this.writableDatabase

        db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_HORASA='${automovil.horaSalida}' WHERE $COLUMN_ID=${automovil._id}")

        //db.update(TABLE_ESTACIONAMIENTO,values, COLUMN_ID+"="+index.toString(),null)

        db.close()

    }

    fun dropTable(tipo: Boolean){
        val db = this.writableDatabase

        if(tipo){
            db.execSQL("DROP TABLE IF EXISTS $TABLE_ESTACIONAMIENTO")
        } else db.execSQL("DROP TABLE IF EXISTS $TABLE_SALIDA")

        db.close()
    }


    fun getAllName(tipo: Boolean): Cursor? {
        val db = this.readableDatabase
        val cursorPrueba: Cursor = if(tipo){
             db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                    + TABLE_ESTACIONAMIENTO + "'", null)
        }else{

             db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                    + TABLE_SALIDA + "'", null)
        }
        if(cursorPrueba.count <= 0 && tipo){
            onCreate(db)
        }else if(cursorPrueba.count <= 0 && !tipo){
            onCreateSalida(db)
        }

        cursorPrueba.close()
        val rawQuery = if(tipo){
            db.rawQuery("SELECT * FROM $TABLE_ESTACIONAMIENTO", null)
        }else{
            db.rawQuery("SELECT * FROM $TABLE_SALIDA", null)
        }
        //onUpgrade(db,0,1)

        return rawQuery
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "mindorksName.db"
        const val TABLE_ESTACIONAMIENTO = "estacionamiento"
        const val TABLE_SALIDA = "salida"
        const val COLUMN_ID = "_id"
        const val COLUMN_MATRICULA = "matricula"
        const val COLUMN_MARCA = "marca"
        const val COLUMN_MODELO = "modelo"
        const val COLUMN_HORAE = "hora_entrada"
        const val COLUMN_HORASA = "hora_salida"
    }
}



//Con Parcelize se le asigna a la clase la propiedad para ser pasada como información a travez de los intent
@Parcelize
class Estacionamiento(var lugares: Int, var carros: MutableList<Automovil>?): Parcelable

@Parcelize
class Pasado(var carros: MutableList<Automovil>?): Parcelable

@Parcelize
class Automovil(var matricula: String, var marca: String, var modelo:String, var horaEntrada:String, var horaSalida:String, var _id : Int): Parcelable