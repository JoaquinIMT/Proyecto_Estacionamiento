package com.example.proyecto_estacionamiento

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.*
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import net.glxn.qrgen.android.QRCode
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import com.google.zxing.integration.android.IntentIntegrator

class QR : AppCompatActivity() {

    val CODIGO_PERMISO_ESCRIBIR_ALMACENAMIENTO = 1
    val ALTURA_CODIGO = 500
    val ANCHURA_CODIGO = 500
    var etTextoParaCodigo: TextView? = null
    lateinit var estacionamiento: Estacionamiento
    lateinit var checkButton: ImageButton
    lateinit var btnFinalizar: Button
    lateinit var btnleerQR: Button
    lateinit var imagenCodigo: ImageView
    //    var arreglo: Estacionamiento?= intent.getParcelableExtra("estacionamiento")
    var todo: String = ""
    val dbHandler = MindOrksDBOpenHelper(this, null)
    private var tienePermisoParaEscribir = false // Para los permisos en tiempo de ejecución

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)
        verificarYPedirPermisos()

        imagenCodigo = findViewById(R.id.ivCodigoGenerado)
        btnFinalizar = findViewById(R.id.btnFinalizar)
        btnleerQR = findViewById(R.id.btnleerQR)
        checkButton = findViewById(R.id.checkedbtn)

        estacionamiento = intent.getParcelableExtra("estacionamiento")


        btnFinalizar.setOnClickListener {
            obtenerTextoParaCodigo()
            val texto = todo
            if (texto.isEmpty()) return@setOnClickListener
            val bitmap = QRCode.from(texto).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).bitmap()
            imagenCodigo.setImageBitmap(bitmap)
            val texto2 = todo
            if (texto2.isEmpty()) return@setOnClickListener
            if (!tienePermisoParaEscribir) {
                noTienePermiso()
                return@setOnClickListener
            }
            // Crear stream del código QR
            val byteArrayOutputStream = QRCode.from(texto2).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).stream()
            // E intentar guardar
            val fos: FileOutputStream
            try {
                fos = FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/codigo.png")
                byteArrayOutputStream.writeTo(fos)
                Toast.makeText(this@QR, "Código guardado", Toast.LENGTH_SHORT).show()

                btnFinalizar.visibility = View.GONE
                btnleerQR.visibility = View.GONE
                imagenCodigo.visibility = View.VISIBLE
                checkButton.visibility = View.VISIBLE



            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }

        btnleerQR.setOnClickListener {
            camara()
        }

        checkButton.setOnClickListener {
            dbHandler.dropTable(true)
            intent()
        }

    }

    override fun onBackPressed() {
        if(checkButton.visibility == View.VISIBLE){

            btnFinalizar.visibility = View.VISIBLE
            btnleerQR.visibility = View.VISIBLE
            imagenCodigo.visibility = View.GONE
            checkButton.visibility = View.GONE
        }else{
            super.onBackPressed()
        }
    }

    private fun obtenerTextoParaCodigo() {
        todo += intent.getStringExtra("folio")+"."
        for (i in estacionamiento?.carros!!) {

            val aux= i.matricula + "," + i.marca + "," + i.modelo + "," + i.horaEntrada + "," + i.horaSalida + "," + i.horaSalida+ "," + i.color+ "," + i.tipo + "," + i.folio
            todo = todo+ aux + "."

        }
        //Toast.makeText(this, todo, Toast.LENGTH_SHORT).show()
    }

    private fun noTienePermiso() {
        Toast.makeText(this@QR, "No has dado permiso para escribir", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        when (requestCode) {
            CODIGO_PERMISO_ESCRIBIR_ALMACENAMIENTO -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // SÍ dieron permiso
                tienePermisoParaEscribir = true

            } else {
                // NO dieron permiso
                noTienePermiso()
            }
        }
    }

    private fun verificarYPedirPermisos() {
        if (checkSelfPermission(
                this@QR,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // En caso de que haya dado permisos ponemos la bandera en true
            tienePermisoParaEscribir = true
        } else {
            // Si no, entonces pedimos permisos
            ActivityCompat.requestPermissions(
                this@QR,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                CODIGO_PERMISO_ESCRIBIR_ALMACENAMIENTO
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if (result != null) {
                if (result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Leido Correctamente" + result.contents, Toast.LENGTH_LONG).show()
                    etTextoParaCodigo?.text = result.contents
                    todo = result.contents
                    dbHandler.dropTable(true)
                    dbHandler.createTable()
                    meterDatos()
                    intent()
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun intent(){

        val intent = Intent(applicationContext,MainActivityReal::class.java)
        startActivity(intent)
        finishAffinity()
    }

    private fun camara(){
        val scanner = IntentIntegrator(this)
        scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        scanner.initiateScan()
    }

    private  fun meterDatos(){

        val separado = todo.split(".")

        for (i in 0..separado.size-2) {

                val posicion = separado.get(i)
                //Toast.makeText(this, posicion, Toast.LENGTH_SHORT).show()
                if(i == 0){
                    dbHandler.upDateFolio(posicion)
                } else{
                    var separado2 = posicion.split(",")
                    Toast.makeText(this, "entre al if", Toast.LENGTH_SHORT).show()
                    val automovil = Automovil(separado2.get(0), separado2.get(1), separado2.get(2), separado2.get(3), separado2.get(4), separado2.get(6), false,separado2.get(8))
                    dbHandler.addFields(automovil, true)

                }


        }

    }

}
