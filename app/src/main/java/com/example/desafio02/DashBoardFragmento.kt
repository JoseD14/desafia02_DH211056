package com.example.desafio02

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class DashBoardFragmento : Fragment() {

    private lateinit var txtNombre: TextView
    private lateinit var txtCarnet: TextView
    private lateinit var txtCarrera: TextView
    private lateinit var rvPendiente : RecyclerView
    private val pendienteList= mutableListOf<Tarea>()
    private lateinit var adaptadorPendiente: PendienteAdaptador
    private lateinit var rvHorario : RecyclerView
    private val horarioList= mutableListOf<Materia>()
    private lateinit var adaptador: HorarioAdaptador

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Asocio con el activity
        val view = inflater.inflate(R.layout.activity_dasboard_fragmento, container, false)

        // Inicializo los elementos de la vista
        txtNombre = view.findViewById(R.id.txtNombre)
        txtCarnet = view.findViewById(R.id.txtCarnet)
        txtCarrera = view.findViewById(R.id.txtCarrera)
        rvHorario = view.findViewById(R.id.rvHorarios)
        rvPendiente = view.findViewById(R.id.rvTareas)

        // Obtener el ID del usuario con el que se logeo
        val uid = FirebaseAuth.getInstance().currentUser?.uid

        if (uid != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("usuarios").document(uid).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val nombre = document.getString("nombre")
                        val carnet = document.getString("carnet")
                        val carrera = document.getString("carrera")

                        txtNombre.text = "Estudiante: $nombre"
                        txtCarnet.text = "Carnet: $carnet"
                        txtCarrera.text = "Carrera: $carrera"
                    } else {
                        Toast.makeText(requireContext(), "No se encontraron datos del usuario", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error obteniendo datos", e)
                    Toast.makeText(requireContext(), "Error al cargar datos", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
        //Se inicializan los adaptadores y los LayoutManager para los RV
        adaptador = HorarioAdaptador(horarioList)
        adaptadorPendiente = PendienteAdaptador(pendienteList)
        rvPendiente.layoutManager = LinearLayoutManager(requireContext())
        rvHorario.layoutManager = LinearLayoutManager(requireContext())
        rvPendiente.adapter = adaptadorPendiente
        rvHorario.adapter = adaptador

        cargarHorarios()
        cargarPendiente()

        return view
    }
    //Cargo los horarios con el ID del estudiantes
    private fun cargarHorarios() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid == null) {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
            return
        }
        val db = FirebaseFirestore.getInstance()
        //Se obtienen los datos de las materias que quiero y muestro los datos deseados
        db.collection("materias")
            .whereEqualTo("usuarioId", uid)
            .get()
            .addOnSuccessListener { result ->
                horarioList.clear()
                for (doc in result) {
                    val materia = Materia(
                        id = doc.id,
                        Nombre_materia = doc.getString("Nombre_materia") ?: "",
                        Dia = doc.getString("Dia") ?: "",
                        Hora = doc.getString("Hora") ?: "",
                        )
                    horarioList.add(materia)
                }
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar horarios", Toast.LENGTH_SHORT).show()
            }
    }
    //cargar datos de tareas que están en pendiente
    private fun cargarPendiente() {
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
                pendienteList.clear()
                for (doc in result) {
                    val estadoTarea = doc.getString("estado") ?: ""
                    //Se filran los datos que estan en pendiente o Pendiente para asi solo mostrar tareas pendientes
                    if (estadoTarea.equals("pendiente", ignoreCase = true)) {
                        val tarea = Tarea(
                            id = doc.id,
                            titulo = doc.getString("titulo") ?: "",
                            materia = doc.getString("materia") ?: "",
                            fechaEntrega = doc.getString("fechaEntrega") ?: "",
                            estado = estadoTarea
                        )
                        pendienteList.add(tarea)
                    }
                }
                adaptadorPendiente.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Error al cargar tareas", Toast.LENGTH_SHORT).show()
            }
    }

}
