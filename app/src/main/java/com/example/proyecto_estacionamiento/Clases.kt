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

class Name(var userName: String? = null)

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

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTACIONAMIENTO)
        onCreate(db)
    }

    fun addFields(automovil: Automovil){
        val values = ContentValues()

        values.put(COLUMN_MATRICULA,automovil.matricula)
        values.put(COLUMN_MARCA,automovil.marca)
        values.put(COLUMN_MODELO,automovil.modelo)
        values.put(COLUMN_HORAE,automovil.horaEntrada)
        values.put(COLUMN_HORASA,automovil.horaSalida)

        val db = this.writableDatabase
        db.insert(TABLE_ESTACIONAMIENTO, null, values)
        db.close()
    }

    fun modify(index: Int, automovil: Automovil, tipo: Int){
        val values = ContentValues()
        val db = this.writableDatabase

        if(tipo == 0){
            values.put(COLUMN_HORASA, automovil.horaSalida)

            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_HORASA='${automovil.horaSalida}' WHERE $COLUMN_ID=${automovil._id}")
        }

        //db.update(TABLE_ESTACIONAMIENTO,values, COLUMN_ID+"="+index.toString(),null)

        db.close()

    }


    fun getAllName(): Cursor? {
        val db = this.readableDatabase

        val cursorPrueba: Cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + TABLE_ESTACIONAMIENTO + "'", null)
        if(cursorPrueba.count <= 0){
            onCreate(db)
        }
        cursorPrueba.close()

        return db.rawQuery("SELECT * FROM $TABLE_ESTACIONAMIENTO", null)
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "mindorksName.db"
        const val TABLE_ESTACIONAMIENTO = "estacionamiento"
        const val COLUMN_ID = "_id"
        const val COLUMN_MATRICULA = "matricula"
        const val COLUMN_MARCA = "marca"
        const val COLUMN_MODELO = "modelo"
        const val COLUMN_HORAE = "hora_entrada"
        const val COLUMN_HORASA = "hora_salida"
    }
}

@Parcelize
class Estacionamiento(var lugares: Int, var carros: MutableList<Automovil>?): Parcelable

@Parcelize
class Pasado(var carros: MutableList<Automovil>?): Parcelable

@Parcelize
class Automovil(var matricula: String, var marca: String, var modelo:String, var horaEntrada:String, var horaSalida:String, var _id : Int): Parcelable