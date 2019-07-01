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
                + COLUMN_COLOR + " TEXT,"
                + COLUMN_FOLIO + " TEXT,"
                + COLUMN_TIPO + " INTEGER,"
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
                + COLUMN_COLOR + " TEXT,"
                + COLUMN_FOLIO + " TEXT,"
                + COLUMN_TIPO + " INTEGER,"
                + COLUMN_HORAE + " TEXT,"
                + COLUMN_HORASA + " TEXT" +")")

        db.execSQL(CREATE_PRODUCTS_TABLE_SALIDA)

    }

    fun onCreateFolio(db: SQLiteDatabase){
        val CREATE_PRODUCTS_TABLE_NUMERO = ("CREATE TABLE " +
                TABLE_FOLIO +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FOLIO + " TEXT" +")")

        db.execSQL(CREATE_PRODUCTS_TABLE_NUMERO)
        val values = ContentValues()
        values.put(COLUMN_FOLIO,"A0000")
        db.insert(TABLE_FOLIO,null,values)

    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTACIONAMIENTO)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALIDA)
        onCreate(db)
        onCreateSalida(db)
    }

    fun createTable(){
        val db = this.writableDatabase
        onCreate(db)
    }

    fun addFields(automovil: Automovil, tipo: Boolean){
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_MATRICULA,automovil.matricula)
        values.put(COLUMN_MARCA,automovil.marca)
        values.put(COLUMN_MODELO,automovil.modelo)
        values.put(COLUMN_HORAE,automovil.horaEntrada)
        values.put(COLUMN_HORASA,automovil.horaSalida)
        values.put(COLUMN_COLOR,automovil.color)
        values.put(COLUMN_FOLIO,automovil.folio)
        values.put(COLUMN_TIPO,automovil.tipo)

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

    fun modify(automovilNuevo: Automovil,automovilAntiguo: Automovil,tipo: Boolean){
        val db = this.writableDatabase
        if(tipo){
            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_MODELO='${automovilNuevo.modelo}' WHERE $COLUMN_MATRICULA='${automovilAntiguo.matricula}' AND $COLUMN_HORAE='${automovilAntiguo.horaEntrada}'")
            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_MARCA='${automovilNuevo.marca}' WHERE $COLUMN_MATRICULA='${automovilAntiguo.matricula}' AND $COLUMN_HORAE='${automovilAntiguo.horaEntrada}'")
            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_COLOR='${automovilNuevo.color}' WHERE $COLUMN_MATRICULA='${automovilAntiguo.matricula}' AND $COLUMN_HORAE='${automovilAntiguo.horaEntrada}'")
            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_TIPO='${automovilNuevo.tipo}' WHERE $COLUMN_MATRICULA='${automovilAntiguo.matricula}' AND $COLUMN_HORAE='${automovilAntiguo.horaEntrada}'")
            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_MATRICULA='${automovilNuevo.matricula}' WHERE $COLUMN_HORAE='${automovilAntiguo.horaEntrada}' AND $COLUMN_HORAE='${automovilAntiguo.horaEntrada}'")        //db.update(TABLE_ESTACIONAMIENTO,values, COLUMN_ID+"="+index.toString(),null)
        }else{
            db.execSQL("UPDATE $TABLE_SALIDA SET $COLUMN_MODELO='${automovilNuevo.modelo}' WHERE $COLUMN_MATRICULA='${automovilAntiguo.matricula}' AND $COLUMN_HORAE='${automovilAntiguo.horaEntrada}'")
            db.execSQL("UPDATE $TABLE_SALIDA SET $COLUMN_MARCA='${automovilNuevo.marca}' WHERE $COLUMN_MATRICULA='${automovilAntiguo.matricula}' AND $COLUMN_HORAE='${automovilAntiguo.horaEntrada}'")
            db.execSQL("UPDATE $TABLE_SALIDA SET $COLUMN_COLOR='${automovilNuevo.color}' WHERE $COLUMN_MATRICULA='${automovilAntiguo.matricula}' AND $COLUMN_HORAE='${automovilAntiguo.horaEntrada}'")
            db.execSQL("UPDATE $TABLE_SALIDA SET $COLUMN_TIPO='${automovilNuevo.tipo}' WHERE $COLUMN_MATRICULA='${automovilAntiguo.matricula}' AND $COLUMN_HORAE='${automovilAntiguo.horaEntrada}'")
            db.execSQL("UPDATE $TABLE_SALIDA SET $COLUMN_MATRICULA='${automovilNuevo.matricula}' WHERE $COLUMN_HORAE='${automovilAntiguo.horaEntrada}' AND $COLUMN_HORAE='${automovilAntiguo.horaEntrada}'")        //db.update(TABLE_ESTACIONAMIENTO,values, COLUMN_ID+"="+index.toString(),null)
        }

        db.close()

    }

    fun upDateFolio(folioNuevo: String){
        val db = this.writableDatabase

        val values = ContentValues()

        values.put(COLUMN_FOLIO,folioNuevo)

        db.update(TABLE_FOLIO,values,null,null)

    }

    fun dropTable(tipo: Boolean){
        val db = this.writableDatabase

        if(tipo){
            db.execSQL("DROP TABLE IF EXISTS $TABLE_ESTACIONAMIENTO")
        } else db.execSQL("DROP TABLE IF EXISTS $TABLE_SALIDA")

        db.close()
    }

    fun getFolio(): Cursor?{
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + TABLE_FOLIO + "'", null)

        if(cursor.count <= 0){
            onCreateFolio(db)
        }
        cursor.close()

        val rawQuery = db.rawQuery("SELECT * FROM $TABLE_FOLIO", null)

        return  rawQuery

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

        return rawQuery
    }

    companion object {
        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "mindorksName.db"
        const val TABLE_ESTACIONAMIENTO = "estacionamiento_completo"
        const val TABLE_SALIDA = "salida_completa"
        const val TABLE_FOLIO = "folio_mayor"
        const val COLUMN_ID = "_id"
        const val COLUMN_MATRICULA = "matricula"
        const val COLUMN_MARCA = "marca"
        const val COLUMN_MODELO = "modelo"
        const val COLUMN_HORAE = "hora_entrada"
        const val COLUMN_HORASA = "hora_salida"
        const val COLUMN_COLOR = "color"
        const val COLUMN_FOLIO = "folio"
        const val COLUMN_TIPO = "tipo"
    }
}


//Con Parcelize se le asigna a la clase la propiedad para ser pasada como información a travez de los intent
@Parcelize
class Estacionamiento(var lugares: Int, var carros: MutableList<Automovil>?): Parcelable

@Parcelize
class Pasado(var carros: MutableList<Automovil>): Parcelable

@Parcelize
class Automovil(var matricula: String, var marca: String, var modelo:String,
                var horaEntrada:String, var horaSalida:String,var color: String,
                var tipo: Boolean,var folio : String, var checked: Boolean = false): Parcelable


