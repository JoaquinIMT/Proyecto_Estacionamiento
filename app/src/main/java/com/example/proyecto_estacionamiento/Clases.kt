package com.example.proyecto_estacionamiento
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.os.Parcelable
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

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
                + COLUMN_RTI + " TEXT,"
                + COLUMN_RTO + " TEXT,"
                + COLUMN_PENSION + " INTEGER,"
                + COLUMN_HORAE + " TEXT,"
                + COLUMN_HORASA + " TEXT" + ")")
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
                + COLUMN_RTI + " TEXT,"
                + COLUMN_RTO + " TEXT,"
                + COLUMN_PENSION + " INTEGER,"
                + COLUMN_TOTAL + " INTEGER,"
                + COLUMN_HORAE + " TEXT,"
                + COLUMN_HORASA + " TEXT" + ")")

        db.execSQL(CREATE_PRODUCTS_TABLE_SALIDA)

    }

    fun onCreateFolio(db: SQLiteDatabase) {
        val CREATE_PRODUCTS_TABLE_NUMERO = ("CREATE TABLE " +
                TABLE_FOLIO +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_FOLIO + " TEXT" + ")")

        db.execSQL(CREATE_PRODUCTS_TABLE_NUMERO)
        val values = ContentValues()
        values.put(COLUMN_FOLIO, "A0000")
        db.insert(TABLE_FOLIO, null, values)

    }

    fun onCreateType(db: SQLiteDatabase) {
        val CREATE_PRODCTS_TABLE_TYPE = ("CREATE TABLE " +
                TABLE_TYPE +
                "(" + COLUMN_ID + " INTEGER PRIMARY KEY,"
                + COLUMN_TIPO + " INTEGER,"
                + COLUMN_PARKING_NAME + " TEXT,"
                + COLUMN_WORKER_NAME + " TEXT,"
                + COLUMN_WORKER_ID + " INTEGER,"
                + COLUMN_JWT + " TEXT,"
                + COLUMN_SHOW_ACTIVE + " INTEGER,"
                + COLUMN_BLOQUE + " INTEGER,"
                + COLUMN_PRICE + " INTEGER,"
                + COLUMN_REPEAT + " INTEGER,"
                + COLUMN_SLOTS_NUMBER + " INTEGER" + ")")
        db.execSQL(CREATE_PRODCTS_TABLE_TYPE)

    }

    fun onCreateMarMod(db: SQLiteDatabase) {

        val CREATE_PRODUCTS_TABLE_MARMOD = ("CREATE TABLE " +
                TABLE_MARMOD +
                "(" + COLUMN_MARCA + " TEXT,"
                + COLUMN_MODELO + " TEXT" + ")")

        db.execSQL(CREATE_PRODUCTS_TABLE_MARMOD)

    }

    fun onCreateLogIn(db: SQLiteDatabase) {

        val CREATE_PRODUCTS_TABLE_MARMOD = ("CREATE TABLE " +
                TABLE_CHECKIN +
                "(" + COLUMN_SHOW_ACTIVE + " INTEGER" + ")")

        db.execSQL(CREATE_PRODUCTS_TABLE_MARMOD)

        val values = ContentValues()

        values.put(COLUMN_SHOW_ACTIVE, 0)

        db.insert(TABLE_CHECKIN, null, values)

    }

    fun changeLogStatus(statusToPut: Boolean) {

        val db = this.writableDatabase

        val values = ContentValues()

        val number = if (statusToPut) 1 else 0

        values.put(COLUMN_SHOW_ACTIVE, number)

        db.update(TABLE_CHECKIN, values, null, null)

    }

    fun checkLogIn(): Boolean {
        val db = this.writableDatabase


        val cursor: Cursor = db.rawQuery(
            "select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                    + TABLE_CHECKIN + "'", null
        )

        if (cursor.count <= 0) {
            onCreateLogIn(db)
        }

        cursor.close()

        val rawQuery = db.rawQuery("SELECT * FROM $TABLE_CHECKIN", null)

        rawQuery.moveToFirst()


        val status: Boolean = rawQuery.getInt(rawQuery.getColumnIndex(COLUMN_SHOW_ACTIVE)) != 0


        rawQuery.close()

        return status

    }

    fun llenarArrayMarca(): ArrayList<String> {
        var listaMarca: ArrayList<String> = ArrayList<String>()
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery(
            "select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                    + TABLE_MARMOD + "'", null
        )

        if (cursor.count <= 0) {
            onCreateMarMod(db)
        }
        cursor.close()
        val query: String = "SELECT * FROM TABLE_MARMOD"
        var registros: Cursor = db.rawQuery(query, null)

        if (registros.moveToFirst()) {
            do {
                listaMarca.add(registros.getString(0))
            } while (registros.moveToNext())
        }
        return listaMarca
    }

    /*
    fun getMarMod(): Cursor? {
        val db = this.readableDatabase
        val cursor: Cursor = db.rawQuery(
            "select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                    + TABLE_MARMOD + "'", null
        )

        if (cursor.count <= 0) {
            onCreateMarMod(db)
        }
        cursor.close()
    }*/
    fun crear() {
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery(
            "select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                    + TABLE_MARMOD + "'", null
        )
        if (cursor.count <= 0) {
            onCreateMarMod(db)
        }
        cursor.close()
    }

    fun guardar(marca: String, modelo: String): String {
        val db = this.writableDatabase
        val cursor: Cursor = db.rawQuery(
            "select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                    + TABLE_MARMOD + "'", null
        )

        if (cursor.count <= 0) {
            onCreateMarMod(db)
        }
        cursor.close()
        var mensaje = ""
        val database = this.writableDatabase
        val contenedor = ContentValues()
        contenedor.put("marca", marca)
        contenedor.put("modelo", modelo)
        try {
            database.insertOrThrow(TABLE_MARMOD, null, contenedor)
            mensaje = "Ingresado Correctamente"
        } catch (e: SQLException) {
            mensaje = "No Ingresado"
        }
        database.close()
        return mensaje
    }

    /*
    fun llenarArrayMarca(): ArrayList<String> {
        var listaMarca : ArrayList<String> = ArrayList<String>()
        val db = this.writableDatabase
        val query : String = "SELECT * FROM TABLE_MARMOD"
        var registros: Cursor = db.rawQuery(query,null)
        if(registros.moveToFirst()){
            do{
                listaMarca.add(registros.getString(0))
            }while (registros.moveToNext())
        }
        return listaMarca

    }*/
    fun llenarArrayModelo(): ArrayList<String> {
        var listaModelo: ArrayList<String> = ArrayList()
        val db = this.writableDatabase
        val query: String = "SELECT * FROM TABLE_MARMOD"
        var registros: Cursor = db.rawQuery(query, null)
        if (registros.moveToFirst()) {
            do {
                listaModelo.add(registros.getString(1))
            } while (registros.moveToNext())
        }
        return listaModelo
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ESTACIONAMIENTO)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALIDA)
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARMOD)
        onCreate(db)
        onCreateSalida(db)
        onCreateMarMod(db)
    }

    fun limpiarRegistros() {
        val db = this.writableDatabase
        onUpgrade(db, 0, 0)
    }

    fun createTable() {
        val db = this.writableDatabase
        onCreate(db)
    }

    fun addFields(automovil: Automovil, tipo: Boolean) {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(COLUMN_MATRICULA, automovil.matricula)
        values.put(COLUMN_MARCA, automovil.marca)
        values.put(COLUMN_MODELO, automovil.modelo)
        values.put(COLUMN_HORAE, automovil.horaEntrada)
        values.put(COLUMN_HORASA, automovil.horaSalida)
        values.put(COLUMN_COLOR, automovil.color)
        values.put(COLUMN_FOLIO, automovil.folio)
        values.put(COLUMN_TIPO, automovil.tipo)
        values.put(COLUMN_RTI, automovil.realTimeIn)
        values.put(COLUMN_RTO, automovil.realTimeOut)
        values.put(COLUMN_PENSION, automovil.pensionado)

        if (tipo) {
            db.insert(TABLE_ESTACIONAMIENTO, null, values)
        } else {
            values.put(COLUMN_TOTAL, automovil.total)
            db.insert(TABLE_SALIDA, null, values)
        }
        db.close()
    }


    fun modify(automovilNuevo: Automovil, automovilAntiguo: Automovil, tipo: Boolean) {
        val tipoCarro = if (automovilNuevo.tipo) {
            1
        } else 0
        val pensionado = if (automovilNuevo.pensionado!!){
            1
        } else 0

        val db = this.writableDatabase
        if (tipo) {
            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_MODELO='${automovilNuevo.modelo}' WHERE $COLUMN_FOLIO='${automovilAntiguo.folio}'")
            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_MARCA='${automovilNuevo.marca}' WHERE $COLUMN_FOLIO='${automovilAntiguo.folio}'")
            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_COLOR='${automovilNuevo.color}' WHERE $COLUMN_FOLIO='${automovilAntiguo.folio}'")
            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_TIPO='${tipoCarro}' WHERE $COLUMN_FOLIO='${automovilAntiguo.folio}'")
            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_MATRICULA='${automovilNuevo.matricula}' WHERE $COLUMN_FOLIO='${automovilAntiguo.folio}'")
            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_PENSION='${pensionado}' WHERE $COLUMN_FOLIO='${automovilAntiguo.folio}'")//db.update(TABLE_ESTACIONAMIENTO,values, COLUMN_ID+"="+index.toString(),null)
        } else {
            db.execSQL("UPDATE $TABLE_SALIDA SET $COLUMN_MODELO='${automovilNuevo.modelo}' WHERE $COLUMN_FOLIO='${automovilAntiguo.folio}'")
            db.execSQL("UPDATE $TABLE_SALIDA SET $COLUMN_MARCA='${automovilNuevo.marca}' WHERE $COLUMN_FOLIO='${automovilAntiguo.folio}'")
            db.execSQL("UPDATE $TABLE_SALIDA SET $COLUMN_COLOR='${automovilNuevo.color}' WHERE $COLUMN_FOLIO='${automovilAntiguo.folio}'")
            db.execSQL("UPDATE $TABLE_SALIDA SET $COLUMN_TIPO='${tipoCarro}' WHERE $COLUMN_FOLIO='${automovilAntiguo.folio}'")
            db.execSQL("UPDATE $TABLE_SALIDA SET $COLUMN_MATRICULA='${automovilNuevo.matricula}' WHERE $COLUMN_FOLIO='${automovilAntiguo.folio}'")        //db.update(TABLE_ESTACIONAMIENTO,values, COLUMN_ID+"="+index.toString(),null)
            db.execSQL("UPDATE $TABLE_ESTACIONAMIENTO SET $COLUMN_PENSION='${pensionado}' WHERE $COLUMN_FOLIO='${automovilAntiguo.folio}'")
        }

        db.close()

    }

    fun upDateFolio(folioNuevo: String) {
        val db = this.writableDatabase

        val values = ContentValues()

        values.put(COLUMN_FOLIO, folioNuevo)

        db.update(TABLE_FOLIO, values, null, null)

    }

    fun newType(datosIniciales: DatosIniciales, context: Context? = null) {

        val db = this.writableDatabase

        val values = ContentValues()

        val cursor: Cursor = db.rawQuery(
            "select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                    + TABLE_TYPE + "'", null
        )

        db.execSQL("DROP TABLE IF EXISTS $TABLE_TYPE")
        onCreateType(db)
        /*if(cursor.count <= 0){
            onCreateType(db)
        }else{
            db.execSQL("delete from "+ TABLE_TYPE)
        }*/

        cursor.close()
        values.put(COLUMN_SLOTS_NUMBER, datosIniciales.slotsNumber)
        values.put(COLUMN_TIPO, datosIniciales.typeOfParking)
        values.put(COLUMN_PARKING_NAME, datosIniciales.parkingName)
        values.put(COLUMN_WORKER_NAME, datosIniciales.workerName)
        values.put(COLUMN_WORKER_ID,datosIniciales.workerID)
        values.put(COLUMN_JWT, datosIniciales.token)
        values.put(COLUMN_PRICE, datosIniciales.parkingFee!![0].cost.toInt())
        values.put(COLUMN_BLOQUE, datosIniciales.parkingFee!![0].time)
        values.put(COLUMN_REPEAT, datosIniciales.parkingFee!![0].repeats)
        values.put(COLUMN_SHOW_ACTIVE, 1)

        val insert = db.insert(TABLE_TYPE, null, values)
        if (context != null) {
            Toast.makeText(context, insert.toString(), Toast.LENGTH_LONG).show()
        }

    }

    fun insertMoreCosts(fee: Fee){

        val db = this.writableDatabase

        val values = ContentValues()

        values.put(COLUMN_PRICE, fee.cost.toInt())

        values.put(COLUMN_BLOQUE, fee.time)

        values.put(COLUMN_REPEAT, fee.repeats)

        db.insert(TABLE_TYPE, null, values)
    }

    fun getFees(): Cursor? {

        val db = this.readableDatabase

        val cursor: Cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + TABLE_TYPE + "'", null)

        if(cursor.count <= 0){
            onCreateType(db)
        }

        cursor.close()

        val rawQuery = db.rawQuery("SELECT * FROM $TABLE_TYPE", null)

        return  rawQuery

    }

    fun getToken(): String?{

        val db = this.readableDatabase

        var token: String? = null

        val cursor: Cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + TABLE_TYPE + "'", null)

        if(cursor.count <= 0){
            onCreateType(db)
        }

        cursor.close()

        val rawQuery = db.rawQuery("SELECT * FROM $TABLE_TYPE", null)

        rawQuery.moveToFirst()

        if(rawQuery.count > 0){
            token = rawQuery.getString(rawQuery.getColumnIndex(COLUMN_JWT))
        }

        rawQuery.close()

        return token

    }

    fun upDateToken(newToken: String){

        val db = this.writableDatabase

        val values = ContentValues()

        values.put(COLUMN_JWT,newToken)

        db.update(TABLE_TYPE,values,null,null)
        
    }

    fun activeCarVisibility(visible: Boolean){

        val db = this.writableDatabase

        val values = ContentValues()
        if(visible){
            values.put(COLUMN_SHOW_ACTIVE,1)
        }else{
            values.put(COLUMN_SHOW_ACTIVE,0)
        }

        db.update(TABLE_TYPE,values,null,null)

    }

    fun checkWorkerActive(): Boolean{
        val db = this.readableDatabase

        var active: Boolean = true

        val cursor: Cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + TABLE_TYPE + "'", null)

        if(cursor.count <= 0){
            onCreateType(db)
        }

        cursor.close()

        val rawQuery = db.rawQuery("SELECT * FROM $TABLE_TYPE", null)

        rawQuery.moveToFirst()

        if(rawQuery.count > 0){
            active = rawQuery.getInt(rawQuery.getColumnIndex(COLUMN_SHOW_ACTIVE)) != 0
        }

        rawQuery.close()

        return active
    }

    fun dropElement(automovil: Automovil){
        val db = this.writableDatabase

        db.execSQL("DELETE FROM $TABLE_ESTACIONAMIENTO WHERE $COLUMN_FOLIO='${automovil.folio}'")
    }

    fun dropTableType(){

        val db = this.writableDatabase

        db.execSQL("DROP TABLE IF EXISTS $TABLE_TYPE")

        db.close()

    }

    fun dropTable(tipo: Boolean){

        val db = this.writableDatabase

        if(tipo){
            db.execSQL("DROP TABLE IF EXISTS $TABLE_ESTACIONAMIENTO")
        } else db.execSQL("DROP TABLE IF EXISTS $TABLE_SALIDA")

        db.close()

    }

    fun getType(): Cursor?{
        val db = this.readableDatabase

        val cursor: Cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"
                + TABLE_TYPE + "'", null)

        if(cursor.count <= 0){
            onCreateType(db)
        }

        cursor.close()

        val rawQuery = db.rawQuery("SELECT * FROM $TABLE_TYPE", null)

        return  rawQuery

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
        const val TABLE_ESTACIONAMIENTO = "estacionamiento_completo1"
        const val TABLE_SALIDA = "salida_completa2"
        const val TABLE_MARMOD = "marcaModelo_completa1"
        const val TABLE_FOLIO = "folio_mayo1r"
        const val TABLE_TYPE = "tipo_estacionamiento1"
        const val TABLE_CHECKIN = "loged_in1"
        const val COLUMN_ID = "_id"
        const val COLUMN_MATRICULA = "matricula"
        const val COLUMN_MARCA = "marca"
        const val COLUMN_MODELO = "modelo"
        const val COLUMN_HORAE = "hora_entrada"
        const val COLUMN_HORASA = "hora_salida"
        const val COLUMN_COLOR = "color"
        const val COLUMN_FOLIO = "folio"
        const val COLUMN_TIPO = "tipo"
        const val COLUMN_PENSION = "pensionado"
        const val COLUMN_TOTAL = "total"
        const val COLUMN_RTI = "tiemporealentra"
        const val COLUMN_RTO = "tiemporealsale"
        const val COLUMN_PRICE = "precioporbloque"
        const val COLUMN_REPEAT = "repeticiones"
        const val COLUMN_BLOQUE = "bloquetiempo"
        const val COLUMN_PARKING_NAME = "parking_name"
        const val COLUMN_WORKER_NAME = "worker_name"
        const val COLUMN_WORKER_ID = "worker_id"
        const val COLUMN_SLOTS_NUMBER = "slots_number"
        const val COLUMN_JWT = "json_web_token"
        const val COLUMN_SHOW_ACTIVE = "active_parking"
        const val COLUMN_ENROLL_ID = "enroll_id"
    }
}


//Con Parcelize se le asigna a la clase la propiedad para ser pasada como información a travez de los intent
@Parcelize
class Estacionamiento(var lugares: Int, var carros: MutableList<Automovil>?, var oneSelected: Int = 0): Parcelable

@Parcelize
class Pasado(var carros: MutableList<Automovil>): Parcelable

@Parcelize
class Automovil(var matricula: String, var marca: String, var modelo:String,
                var horaEntrada:String, var horaSalida:String,var color: String,
                var tipo: Boolean,var folio : String,var total: Double? = null,
                var pensionado: Boolean? = null ,var checked: Boolean = false,
                var realTimeIn: String? = null, var realTimeOut: String? =null ): Parcelable

@Parcelize
class DatosIniciales(var parkingName: String?, var workerName: String?,
                     var typeOfParking: Int?, var parkingFee: Array<Fee>? = null,
                     var slotsNumber: Int?, var register: Boolean? = true,
                     var token: String? = null, var workerID: Int? = null ): Parcelable

@Parcelize
class Fee(var time: Int, var cost: Float, var repeats: Int): Parcelable