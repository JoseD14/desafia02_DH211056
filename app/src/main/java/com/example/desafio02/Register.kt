package com.example.desafio02

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.*
import com.google.firebase.auth.ActionCodeEmailInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.log

class Register : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    private lateinit var buttonRegister:  Button
    private lateinit var buttonLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_register)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        buttonRegister = findViewById(R.id.btnRegister)
        buttonRegister.setOnClickListener{
            val email = findViewById<EditText>(R.id.txtEmail).text.toString()
            val password = findViewById<EditText>(R.id.txtPassword).text.toString()
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,
                    "Tienes que llenar los campos",
                    Toast.LENGTH_SHORT).show()
            }
            else {
                this.register(email, password)
            }
        }
        buttonLogin = findViewById(R.id.btnLogin)
        buttonLogin.setOnClickListener{
            this.gotoLogin()
        }

    }
    private fun register(email: String, password: String) {
        val nombre = findViewById<EditText>(R.id.txtNombre).text.toString()
        val carnet = findViewById<EditText>(R.id.txtCarnet).text.toString()
        val carrera = findViewById<EditText>(R.id.txtCarrera).text.toString()
        val telefono = findViewById<EditText>(R.id.txtTelefono).text.toString()

        if (nombre.isEmpty() || carnet.isEmpty()) {
            Toast.makeText(this, "Nombre y carnet son requeridos", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("Register", "Intentando crear usuario con: $email")

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d("Register", "Usuario creado con éxito")

                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val db = FirebaseFirestore.getInstance()

                    val userMap = hashMapOf(
                        "nombre" to nombre,
                        "carnet" to carnet,
                        "email" to email,
                        "carrera" to carrera,
                        "telefono" to telefono
                    )

                    db.collection("usuarios").document(userId).set(userMap)
                        .addOnSuccessListener {
                            Log.d("Register", "Datos guardados en Firestore con éxito")
                            Toast.makeText(this, "Registrado correctamente", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@Register, menu_principal::class.java)
                            startActivity(intent)
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Log.e("Firestore", "Error guardando datos: ", e)
                            Toast.makeText(this, "Error guardando datos: ${e.message}", Toast.LENGTH_LONG).show()
                        }
                } else {
                    val errorMessage = task.exception?.message ?: "Error desconocido"
                    Log.e("FirebaseAuth", "Error en el registro: $errorMessage")
                    Toast.makeText(this, "Error en el registro: $errorMessage", Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun gotoLogin(){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)

    }
}