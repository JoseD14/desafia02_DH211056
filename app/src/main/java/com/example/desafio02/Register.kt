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
    private fun register(email: String, password: String){
        auth.createUserWithEmailAndPassword(email,password)
            .addOnCompleteListener{task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else {
                    Log.d("FirebaseAuth", "Error en el registro: ${task.exception?.message}")
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
    private fun gotoLogin(){
        val intent = Intent(this, Login::class.java)
        startActivity(intent)

    }
}