package com.example.desafio02

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AgregarMateriaFragmento : Fragment() {
    private lateinit var materia: EditText
    private lateinit var codigo: EditText
    private lateinit var grupo: EditText
    private lateinit var dia: Spinner
    private lateinit var hora: Spinner
    private lateinit var aula: EditText
    private lateinit var docente: EditText
    private lateinit var btnInscribir: Button
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.activity_agregar_materia_fragmento, container, false)

        materia = view.findViewById(R.id.etNombre)
        codigo = view.findViewById(R.id.etCodigo)
        grupo = view.findViewById(R.id.etGrupo)
        dia = view.findViewById(R.id.spinnerDia)
        hora = view.findViewById(R.id.spinnerHora)
        aula = view.findViewById(R.id.etAula)
        docente = view.findViewById(R.id.etDocente)
        btnInscribir = view.findViewById(R.id.btnInscribirMateria)

        val adapterDia = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.dia_array,
            android.R.layout.simple_spinner_item
        )
        adapterDia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        dia.adapter = adapterDia

        val adapterHora = ArrayAdapter.createFromResource(
            requireContext(),
            R.array.hora_array,
            android.R.layout.simple_spinner_item
        )
        adapterHora.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        hora.adapter = adapterHora

        btnInscribir.setOnClickListener{
            inscribirMateria()
        }

        return view
    }
    private fun inscribirMateria() {
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        if (uid != null) {
            val db = FirebaseFirestore.getInstance()
            val materia = hashMapOf(
                "Nombre_materia" to materia.text.toString(),
                "Codigo" to codigo.text.toString(),
                "Grupo" to grupo.text.toString(),
                "Dia" to dia.selectedItem.toString(),
                "Hora" to hora.selectedItem.toString(),
                "Aula" to aula.text.toString(),
                "Nombre_profesor" to docente.text.toString(),
                "usuarioId" to uid
            )

            db.collection("materias")
                .add(materia)
                .addOnSuccessListener { documentReference ->
                    // Agrega el campo 'id' al documento
                    db.collection("materias").document(documentReference.id)
                        .update("id", documentReference.id)
                        .addOnSuccessListener {
                            Toast.makeText(requireContext(), "Materia inscrita", Toast.LENGTH_SHORT).show()
                            limpiarCampos()
                            findNavController().navigateUp()
                        }
                }

                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Error al inscribir", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(requireContext(), "Usuario no autenticado", Toast.LENGTH_SHORT).show()
        }
    }
    private fun limpiarCampos() {
        materia.text.clear()
        codigo.text.clear()
        grupo.text.clear()
        aula.text.clear()
        dia.setSelection(0)
        hora.setSelection(0)
        docente.text.clear()
    }
}