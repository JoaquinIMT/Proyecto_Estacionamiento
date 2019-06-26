package com.example.proyecto_estacionamiento

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import net.glxn.qrgen.android.QRCode
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException

class QR : AppCompatActivity() {

    val CODIGO_PERMISO_ESCRIBIR_ALMACENAMIENTO = 1
    val ALTURA_CODIGO = 500
    val ANCHURA_CODIGO = 500
    var etTextoParaCodigo: EditText? = null

    private var tienePermisoParaEscribir = false // Para los permisos en tiempo de ejecución

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qr)
        verificarYPedirPermisos()

        etTextoParaCodigo = findViewById(R.id.etTextoParaCodigo)

        val imagenCodigo = findViewById(R.id.ivCodigoGenerado) as ImageView

        val btnGenerar = findViewById(R.id.btnGenerar) as Button
        val btnGuardar = findViewById(R.id.btnGuardar) as Button

        btnGenerar.setOnClickListener{
            val texto = obtenerTextoParaCodigo()
            if (texto.isEmpty()) return@setOnClickListener

            val bitmap = QRCode.from(texto).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).bitmap()
            imagenCodigo.setImageBitmap(bitmap)
        }
        btnGuardar.setOnClickListener{
            val texto = obtenerTextoParaCodigo()
            if (texto.isEmpty()) return@setOnClickListener
            if (!tienePermisoParaEscribir) {
                noTienePermiso()
                return@setOnClickListener
            }
            // Crear stream del código QR
            val byteArrayOutputStream = QRCode.from(texto).withSize(ANCHURA_CODIGO, ALTURA_CODIGO).stream()
            // E intentar guardar
            val fos: FileOutputStream
            try
            {
                fos = FileOutputStream(Environment.getExternalStorageDirectory().toString() + "/codigo.png")
                byteArrayOutputStream.writeTo(fos)
                Toast.makeText(this@QR, "Código guardado", Toast.LENGTH_SHORT).show()
            } catch (e: FileNotFoundException)
            {
                e.printStackTrace()
            } catch (e: IOException)
            {
                e.printStackTrace()
            }
        }

    }
    private fun obtenerTextoParaCodigo(): String {
        etTextoParaCodigo?.error = null
        val posibleTexto = etTextoParaCodigo?.text.toString()
        if (posibleTexto.isEmpty()) {
            etTextoParaCodigo?.error = "Escribe el texto del código QR"
            etTextoParaCodigo?.requestFocus()
        }
        return posibleTexto
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
        if (checkSelfPermission(this@QR, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
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
}
