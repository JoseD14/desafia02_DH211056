package com.example.desafio02

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var  btnLogin: Button
    private lateinit var btnRegister: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        btnLogin = findViewById(R.id.btnLoginLogin)
        btnLogin.setOnClickListener{
            val email = findViewById<EditText>(R.id.txtEmailLogin).text.toString()
            val password = findViewById<EditText>(R.id.txtPasswordLogin).text.toString()
            if (email.isEmpty() || password.isEmpty()){
                Toast.makeText(this,
                    "Se deben llenar los campos",
                    Toast.LENGTH_LONG).show()
            }
            else {
                this.login(email, password)
            }
        }
        btnRegister = findViewById(R.id.btnRegisterLogin)
        btnRegister.setOnClickListener{
            this.goToRegister()
        }


    }
    private fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{ task ->
                if(task.isSuccessful) {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }

            }.addOnFailureListener{ exception ->
                Toast.makeText(
                    applicationContext,
                    exception.localizedMessage,
                    Toast.LENGTH_LONG
                ).show()
            }
    }
    private fun goToRegister(){
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }
}