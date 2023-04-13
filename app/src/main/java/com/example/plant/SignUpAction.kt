

package com.example.plant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpAction : AppCompatActivity() {
    private lateinit var name : EditText

    private lateinit var login : TextView
    private lateinit var signUPEmail : EditText
    private lateinit var signUpButton : Button
    private lateinit var signUpPassword : EditText
    private lateinit var confirmSignUpPassword : EditText
    private lateinit var forget : TextView
    private lateinit var userID: String

    private val db = FirebaseFirestore.getInstance()
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
            singUp()

        }

    }
    private fun singUp()
    {


        val name = name.text.toString();
        var email = signUPEmail.text.toString();
        var password = signUpPassword.text.toString();
        var confirmPassword = confirmSignUpPassword.text.toString();
        if (name.isNotEmpty() &&email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty())  {
            if(email.contains("@") && password.length > 0 && confirmPassword == password)
            {
                firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()
                        val usercurent = firebaseAuth.currentUser

                        usercurent?.updateProfile(profileUpdates)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // Cập nhật hồ sơ hoàn tất
                                userID = usercurent.uid

                                val userHashMap = hashMapOf(
                                    "email" to usercurent.email,
                                    "displayName" to usercurent.displayName,
                                    "numberPhone" to usercurent.phoneNumber,
                                    "UID" to userID,
                                    "photoURL" to usercurent.photoUrl
                                )

                                db.collection("users").document(userID)
                                    .set(userHashMap)
                                    .addOnSuccessListener {
                                        // Thực hiện hành động sau khi lưu trữ dữ liệu thành công
                                        navi()
                                    }
                                    .addOnFailureListener { exception ->
                                        // Xử lý lỗi khi lưu trữ dữ liệu thất bại
                                        Toast.makeText(this@SignUpAction, exception.toString(), Toast.LENGTH_SHORT).show()
                                    }
                            } else {
                                // Xử lý lỗi khi cập nhật hồ sơ không thành công
                                Toast.makeText(this@SignUpAction, "Failed to update profile.", Toast.LENGTH_SHORT).show()
                            }
                        }


                    } else {
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

    private fun navi() {
        val intent = Intent(this, LoginAction::class.java) // navi
        startActivity(intent)
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