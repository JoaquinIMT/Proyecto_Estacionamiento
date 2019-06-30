package com.example.proyecto_estacionamiento

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
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
//    var arreglo: Estacionamiento?= intent.getParcelableExtra("estacionamiento")
    var todo: String = ""
    val dbHandler = MindOrksDBOpenHelper(this, null)
    lateinit var empezar: Button
    private var tienePermisoParaEscribir = false // Para los permisos en tiempo de ejecución

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)
        verificarYPedirPermisos()
        empezar = findViewById(R.id.btnempezar)
        estacionamiento = intent.getParcelableExtra("estacionamiento")
        etTextoParaCodigo = findViewById(R.id.etTextoParaCodigo) as TextView

        val imagenCodigo = findViewById(R.id.ivCodigoGenerado) as ImageView

        val btnGenerar = findViewById(R.id.btnGenerar) as Button
        val btnGuardar = findViewById(R.id.btnGuardar) as Button
        val btnEmpezar = findViewById(R.id.btnempezar) as Button

        btnGenerar.setOnClickListener {
            obtenerTextoParaCodigo()
            val texto = todo
            if (texto.isEmpty()) return@setOnClickListener

            val bitmap = QRCode.from(texto).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).bitmap()
            imagenCodigo.setImageBitmap(bitmap)
        }

        btnEmpezar.setOnClickListener {
            val scanner = IntentIntegrator(this)
            //scanner.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
            //scanner.initiateScan()
            val separado = todo.split(".")
            dbHandler.createTable()
            val a = Automovil("1","1","1","1","1","1",false,"1")
            dbHandler.addFields(a,true)
            intent()
            for(i in 0..separado.size-1){
                if(i>0){
                    val posicion = separado.get(i)
                    //Toast.makeText(this, posicion, Toast.LENGTH_SHORT).show()
                    var separado2 = posicion.split(",")
                    Toast.makeText(this, "0="+separado2.get(0)+"1="+separado2.get(1)+"2="+separado2.get(2)+"3="+separado2.get(3)+"4="+separado2.get(4)+"6="+separado2.get(6)+"7="+false+"8="+separado2.get(8), Toast.LENGTH_SHORT).show()

                    //val v2 = separado2.get(6)

                    //Toast.makeText(this, v2, Toast.LENGTH_SHORT).show()


                }
                //Toast.makeText(this, "gg", Toast.LENGTH_SHORT).show()

                //separado.size

                /*
                if(i>0){
                //Toast.makeText(this, separado.get(i), Toast.LENGTH_SHORT).show()
                    var separado2 = posicion.split(",")
                    //Toast.makeText(this, separado2.get(0)+separado2.get(1)+separado2.get(2)+separado2.get(3)+separado2.get(4)+separado2.get(5)+separado2.get(6).toBoolean()+separado2.get(7), Toast.LENGTH_SHORT).show()
                    val automovil = Automovil(separado2.get(0),separado2.get(1),separado2.get(2),separado2.get(3),separado2.get(4),separado2.get(5),separado2.get(6).toBoolean(),separado2.get(7))
                    dbHandler.addFields(automovil,true)

                }
                */


            }

        }

        btnGuardar.setOnClickListener {
            val texto = todo
            if (texto.isEmpty()) return@setOnClickListener
            if (!tienePermisoParaEscribir) {
                noTienePermiso()
                return@setOnClickListener
            }
            // Crear stream del código QR
            val byteArrayOutputStream = QRCode.from(texto).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).stream()
            // E intentar guardar
            val fos: FileOutputStream
            try {
                fos = FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/codigo.png")
                byteArrayOutputStream.writeTo(fos)
                Toast.makeText(this@QR, "Código guardado", Toast.LENGTH_SHORT).show()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }

    }

    private fun obtenerTextoParaCodigo() {
        /*
        etTextoParaCodigo?.error = null
        val posibleTexto = etTextoParaCodigo?.text.toString()
        if (posibleTexto.isEmpty()) {
            etTextoParaCodigo?.error = "Escribe el texto del código QR"
            etTextoParaCodigo?.requestFocus()
        }*/
        for (i in estacionamiento?.carros!!) {
            todo =
                todo + "." + i.matricula + "," + i.marca + "," + i.modelo + "," + i.horaEntrada + "," + i.horaSalida + "," + i.horaSalida+ "," + i.color+ "," + i.tipo + "," + i.folio
        }
        Toast.makeText(this, todo, Toast.LENGTH_SHORT).show()
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
                    Toast.makeText(this, "Scanned: " + result.contents, Toast.LENGTH_LONG).show()
                    etTextoParaCodigo?.text = result.contents
                    todo= result.contents

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

}
