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

class MateriasFragmento : Fragment() {
    private lateinit var btnInscribirMateria: Button
    private lateinit var rvMaterias: RecyclerView
    private val materiasList= mutableListOf<Materia>()
    private lateinit var adapter :MateriaAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_materias_fragmento, container, false)

        btnInscribirMateria = view.findViewById(R.id.btnInscribirMateria)
        rvMaterias = view.findViewById(R.id.rvMaterias)
        btnInscribirMateria.setOnClickListener{
            findNavController().navigate(R.id.action_materiasFragment_to_agregarmateriaFragment)
        }
        adapter = MateriaAdapter(materiasList) { materia ->
            eliminarMateriaFirebase(materia)
        }

        rvMaterias.layoutManager = LinearLayoutManager(requireContext())
        rvMaterias.adapter = adapter

        cargarMaterias()

        return view
    }
    private fun cargarMaterias() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }
        val db = FirebaseFirestore.getInstance()
        db.collection("materias")
            .whereEqualTo("usuarioId", uid)
            .get()
            .addOnSuccessListener { result ->
                materiasList.clear()
                for (doc in result) {
                    val materia = Materia(
                        id = doc.id,
                        Nombre_materia = doc.getString("Nombre_materia") ?: "",
                        Codigo = doc.getString("Codigo") ?: "",
                        Grupo = doc.getString("Grupo") ?: "",
                        Dia = doc.getString("Dia") ?: "",
                        Hora = doc.getString("Hora") ?: "",
                        Aula = doc.getString("Aula") ?: "",
                        Nombre_profesor = doc.getString("Nombre_profesor") ?: "",

                    )
                    materiasList.add(materia)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar materias", Toast.LENGTH_SHORT).show()
            }
}
    //funcion para borrar los datos de firebase y quitar todo
    private fun eliminarMateriaFirebase(materia: Materia) {
        val db = FirebaseFirestore.getInstance()
        db.collection("materias").document(materia.id).delete()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Materia retirada", Toast.LENGTH_SHORT).show()
                adapter.eliminarMateria(materia)
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al eliminar", Toast.LENGTH_SHORT).show()
            }
    }
}