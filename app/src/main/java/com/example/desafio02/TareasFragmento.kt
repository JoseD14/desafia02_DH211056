package com.example.desafio02

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class TareasFragmento : Fragment() {

    private lateinit var btnAgregarTarea: Button
    private lateinit var rvTareas: RecyclerView
    private val tareasList = mutableListOf<Tarea>()
    private lateinit var adapter: TareaAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_tareas_fragmento, container, false)

        btnAgregarTarea = view.findViewById(R.id.btnAgregarTarea)
        rvTareas = view.findViewById(R.id.rvTareas)

        btnAgregarTarea.setOnClickListener {
            findNavController().navigate(R.id.action_tareasFragment_to_agregarTareaFragment)
        }

        adapter = TareaAdapter(tareasList) { tarea ->
            eliminarTareaFirebase(tarea)
        }

        rvTareas.layoutManager = LinearLayoutManager(requireContext())
        rvTareas.adapter = adapter

        cargarTareas()

        return view
    }

    private fun cargarTareas() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        db.collection("tareas")
            .whereEqualTo("usuarioId", uid)
            .get()
            .addOnSuccessListener { result ->
                tareasList.clear()
                for (doc in result) {
                    val tarea = Tarea(
                        id = doc.id,
                        titulo = doc.getString("titulo") ?: "",
                        materia = doc.getString("materia") ?: "",
                        fechaEntrega = doc.getString("fechaEntrega") ?: "",
                        prioridad = doc.getString("prioridad") ?: "",
                        nota = doc.getString("nota") ?: "",
                        estado = doc.getString("estado") ?: ""
                    )
                    tareasList.add(tarea)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar tareas", Toast.LENGTH_SHORT).show()
            }
    }

    private fun eliminarTareaFirebase(tarea: Tarea) {
        val db = FirebaseFirestore.getInstance()
        db.collection("tareas").document(tarea.id).delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Tarea eliminada", Toast.LENGTH_SHORT).show()
                adapter.eliminarTarea(tarea)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
    }
}
