package com.example.desafio02

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.desafio02.datos.Tarea
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AddTareaActivity : AppCompatActivity() {
    private var txtnombre: EditText? = null
    private var txtdescripcion: EditText? = null
    private var txtdate: EditText? = null
    private lateinit var spinnerEstado: Spinner
    private lateinit var database: DatabaseReference

    private var key = ""
    private var accion = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tarea)

        // Inicializar vistas
        txtnombre = findViewById(R.id.txtNombre)
        txtdescripcion = findViewById(R.id.txtDescripcion)
        spinnerEstado = findViewById(R.id.spinnerEstado)

        // Configurar Spinner
        val estados = arrayOf("Pendiente", "Hecho")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, estados)
        spinnerEstado.adapter = adapter

        // Inicializar datos de la tarea (si existen)
        inicializar()
    }

    private fun inicializar() {
        val datos: Bundle? = intent.extras

        if (datos != null) {
            key = datos.getString("key", "")
            txtnombre?.setText(datos.getString("nombre", ""))
            txtdescripcion?.setText(datos.getString("descripcion", ""))
            accion = datos.getString("accion", "")

            // Si estamos editando, asignamos la nueva fecha
            txtdate?.setText(obtenerFechaActual())

            // Seleccionar el estado correcto en el Spinner
            val estado = datos.getString("estado", "")
            val adapter = spinnerEstado.adapter as ArrayAdapter<String>
            val position = adapter.getPosition(estado)
            if (position >= 0) {
                spinnerEstado.setSelection(position)
            }
        } else {
            // Si es una nueva tarea, asignamos la fecha actual
            txtdate?.setText(obtenerFechaActual())
        }
    }

    fun guardar(v: View?) {
        val nombre: String = txtnombre?.text.toString()
        val descripcion: String = txtdescripcion?.text.toString()
        val fecha: String = obtenerFechaActual()
        val estadoSeleccionado = spinnerEstado.selectedItem.toString()

        database = FirebaseDatabase.getInstance().getReference("tareas")
        val tarea = Tarea(nombre, descripcion, fecha, estadoSeleccionado)

        if (accion == "a") {  // Agregar nueva tarea
            val newKey = database.push().key
            if (newKey != null) {
                database.child(newKey).setValue(tarea)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Se guardó con éxito", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al guardar", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "No se pudo generar una clave", Toast.LENGTH_SHORT).show()
            }
        } else if (accion == "e") {  // Editar tarea existente
            if (key.isNotEmpty()) {
                val tareaValues = tarea.toMap()
                val childUpdates = hashMapOf<String, Any>(key to tareaValues)
                database.updateChildren(childUpdates)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Se actualizó con éxito", Toast.LENGTH_SHORT).show()
                        finish()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Error al actualizar", Toast.LENGTH_SHORT).show()
                    }
            } else {
                Toast.makeText(this, "No se encontró la clave del registro", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun cancelar(v: View?) {
        finish()
    }

    fun obtenerFechaActual(): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return sdf.format(Date())
    }
}
