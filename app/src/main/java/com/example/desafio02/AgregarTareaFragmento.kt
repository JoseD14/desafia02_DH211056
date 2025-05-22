package com.example.desafio02

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class AgregarTareaFragmento : Fragment() {

    private lateinit var titulo: EditText
    private lateinit var materia: EditText
    private lateinit var fechaEntrega: EditText
    private lateinit var prioridad: EditText
    private lateinit var nota: EditText
    private lateinit var estado: Spinner
    private lateinit var btnGuardar: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_agregar_tarea_fragmento, container, false)

        // Inicializar vistas
        titulo = view.findViewById(R.id.etTitulo)
        materia = view.findViewById(R.id.etMateria)
        fechaEntrega = view.findViewById(R.id.etFecha)
        prioridad = view.findViewById(R.id.etPrioridad)
        nota = view.findViewById(R.id.etNota)
        estado = view.findViewById(R.id.spinnerEstado)
        btnGuardar = view.findViewById(R.id.btnGuardar)

        // Configurar el spinner
        val adapter = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.estado_array,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        estado.adapter = adapter

        // Listener del botón
        btnGuardar.setOnClickListener {
            guardarTarea()
        }

        return view
    }

    private fun guardarTarea() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val db = FirebaseFirestore.getInstance()
            val tarea = hashMapOf(
                "titulo" to titulo.text.toString(),
                "materia" to materia.text.toString(),
                "fechaEntrega" to fechaEntrega.text.toString(),
                "prioridad" to prioridad.text.toString(),
                "nota" to nota.text.toString(),
                "estado" to estado.selectedItem.toString(),
                "usuarioId" to uid
            )

            db.collection("tareas")
                .add(tarea)
                .addOnSuccessListener { documentReference ->
                    // Agrega el campo 'id' al documento
                    db.collection("tareas").document(documentReference.id)
                        .update("id", documentReference.id)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Tarea guardada", Toast.LENGTH_SHORT).show()
                            limpiarCampos()
                            findNavController().navigateUp()
                        }
                }

                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al guardar", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun limpiarCampos() {
        titulo.text.clear()
        materia.text.clear()
        fechaEntrega.text.clear()
        prioridad.text.clear()
        nota.text.clear()
        estado.setSelection(0)
    }
}
