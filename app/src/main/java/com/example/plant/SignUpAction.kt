

package com.example.plant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest

class SignUpAction : AppCompatActivity() {
    private lateinit var name : EditText

    private lateinit var login : TextView
    private lateinit var signUPEmail : EditText
    private lateinit var signUpButton : Button
    private lateinit var signUpPassword : EditText
    private lateinit var confirmSignUpPassword : EditText
    private lateinit var forget : TextView

    private lateinit var firebaseAuth: FirebaseAuth  //auth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_action)

        login = findViewById(R.id.login)
        signUpButton = findViewById(R.id.signUpButton)
        forget = findViewById(R.id.forget)
        signUPEmail = findViewById(R.id.signUPEmail)
        signUpPassword = findViewById(R.id.signUpPassword)
        confirmSignUpPassword = findViewById(R.id.confirmSignUpPassword)
        name = findViewById(R.id.name)

        firebaseAuth = FirebaseAuth.getInstance()
        forget.setOnClickListener{
            startActivity(Intent(this@SignUpAction, ForgotPasswordAction::class.java)) //go to
        }
        login.setOnClickListener {
            startActivity(Intent(this@SignUpAction, LoginAction::class.java)) //go to Login
        }
        signUpButton.setOnClickListener {
            val name = name.text.toString();
            var email = signUPEmail.text.toString();
            var password = signUpPassword.text.toString();
            var confirmPassword = confirmSignUpPassword.text.toString();
            if (name.isNotEmpty() &&email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty())  {
                if(email.contains("@") && password.length > 0 && confirmPassword == password)
                {
                    firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener{
                        if(it.isSuccessful)
                        {
                            val user = firebaseAuth.currentUser   // đăng kí thành công update thêm user vừa đăng kí thêm name
                            val profileUpdates = UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build()
                            user?.updateProfile(profileUpdates)
                            var intent =Intent(this, LoginAction::class.java) // navi
                            startActivity(intent)
                        }
                        else
                        {
                            Toast.makeText(this@SignUpAction, it.exception.toString(), Toast.LENGTH_SHORT).show()
                        }
                    }
                    Toast.makeText(this@SignUpAction, "suscess.", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    Toast.makeText(this@SignUpAction, "Request not understood due to malformed syntax.", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this@SignUpAction, "please fill in", Toast.LENGTH_SHORT).show()
            }
        }

    }


    }

//fun SignUp()
//{
//    var email = signUPEmail.text.toString();
//    var password = signUpPassword.text.toString();
//    var confirmPassword = confirmSignUpPassword.text.toString();
//    fun checkValueData(email :String ,password  :String,confirmPassword :String):Boolean
//    {
//        if (email.contains("@") && password.length > 0 && confirmPassword == password) {
//            return  true
//        } else {
//            return  false
//        }
//    }
//    if(!checkValueData(email ,password ,confirmPassword))
//    {
//        Toast.makeText(this, "false" ,Toast.LENGTH_SHORT).show()
//    }
//    else
//    {
//        Toast.makeText(this, "sucsses" ,Toast.LENGTH_SHORT).show()
//    }
//}

//btn.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//        startActivity(new Intent(RegisterActive.this, Login_Active.class));
//    }
//});