package com.example.plant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class ForgotPasswordAction : AppCompatActivity() {
    private lateinit var forgotemail : TextView
    private lateinit var login : TextView

    private lateinit var resetPassword : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgotpassword_action)
        forgotemail = findViewById(R.id.forgotemail)
        login = findViewById(R.id.login)

        resetPassword = findViewById(R.id.resetPassword)
        login = findViewById(R.id.login)

        login.setOnClickListener{
            startActivity(Intent(this, LoginAction::class.java))
        }
        resetPassword.setOnClickListener {
            var email =forgotemail.text.toString()
           if(email.isNotEmpty())
           {
               Firebase.auth.sendPasswordResetEmail(email)
                   .addOnCompleteListener { task ->
                       if (task.isSuccessful) {
                           Toast.makeText(this@ForgotPasswordAction, "please check your emailbox", Toast.LENGTH_SHORT).show()
                           startActivity(Intent(this, LoginAction::class.java))
                       }
                       else
                       {
                           Toast.makeText(this@ForgotPasswordAction, "error", Toast.LENGTH_SHORT).show()

                       }

                   }
               }
            else
           {
               Toast.makeText(this@ForgotPasswordAction, "please entern your email", Toast.LENGTH_SHORT).show()
           }
        }




    }
}