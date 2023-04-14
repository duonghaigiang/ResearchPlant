package com.example.plant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginAction : AppCompatActivity() {
    private lateinit var signUp: TextView
    private lateinit var loginEmail: TextView
    private lateinit var login_passWord: TextView
    private lateinit var loginButton: Button

    private lateinit var changePassword: TextView

    private lateinit var firebaseAuth: FirebaseAuth  //auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_action)


        mapping()

        firebaseAuth = FirebaseAuth.getInstance()
        loginButton.setOnClickListener {
            login()
        }

        signUp.setOnClickListener {
            startActivity(Intent(this@LoginAction, SignUpAction::class.java))
        }
        changePassword.setOnClickListener {
            startActivity(Intent(this@LoginAction, ForgotPasswordAction::class.java))
        }

    }
    private fun login()
    {
        var email =loginEmail.text.toString()
        var password =login_passWord.text.toString()
        if(email.isNotEmpty() && password.isNotEmpty())
        {
            if(email.contains("@") && password.length > 0)
            {
                firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                    if(it.isSuccessful)
                    {
                        var intent =Intent(this, MainActivity::class.java) //navi
                        startActivity(intent)
                    }
                    else
                    {
                        Toast.makeText(this@LoginAction, it.exception.toString(), Toast.LENGTH_SHORT).show()

                    }
                }
            }
            else
            {
                Toast.makeText(this, "please filll in", Toast.LENGTH_SHORT).show()
            }
        }
        else
        {
            Toast.makeText(this, "please filll in", Toast.LENGTH_SHORT).show()
        }
    }
    private fun mapping()
    {
        signUp = findViewById(R.id.signUp)
        loginEmail = findViewById(R.id.loginEmail)
        login_passWord = findViewById(R.id.login_passWord)
        loginButton = findViewById(R.id.loginButton)
        changePassword = findViewById(R.id.changePassword)
    }
}