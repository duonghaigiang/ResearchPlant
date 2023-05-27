package com.example.plant
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.ktx.storage
import com.bumptech.glide.Glide
import com.google.firebase.firestore.DocumentSnapshot

class ProfileActivity : AppCompatActivity() {
    private lateinit var username : TextView
    private lateinit var changePassword : TextView
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>
    private val db = FirebaseFirestore.getInstance()
    private lateinit var emailUser : TextView
    private lateinit var btnHome : Button
    private lateinit var btn_avatar : Button
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var btnLogout : Button

    private lateinit var avatar : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activiti_profile_action)
        mapping()
        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let { uri ->
                    uploadAvatar(uri)
                }
            }
        }


        val currentUser = Firebase.auth.currentUser // viết tắt của kotlin
        val user = FirebaseAuth.getInstance()

        currentUser?.let {
            username.text = it.displayName
        }
        currentUser?.let {
            emailUser.text = it.email
        }
        btnHome.setOnClickListener{
            startActivity(Intent(this@ProfileActivity, MainActivity::class.java))
        }
        btnLogout.setOnClickListener{
            user.signOut()
            startActivity(Intent(this@ProfileActivity, LoginAction::class.java))
        }
        changePassword.setOnClickListener{
            startActivity(Intent(this@ProfileActivity, ForgotPasswordAction::class.java))
        }
        btn_avatar.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getImageLauncher.launch(galleryIntent)
        }
        loadAvatarUrl(currentUser?.uid)
        loadNumberOfArticles(currentUser?.uid)
    }
    private fun loadNumberOfArticles(uid: String?) {
        val articlesCountView : TextView = findViewById(R.id.articlesCount) // replace this with the id of your TextView

        if (uid != null) {
            db.collection("articles")
                .whereEqualTo("uid", uid)
                .get()
                .addOnSuccessListener { documents ->
                    val count = documents.size()
                   if(count == 0)
                   {
                       articlesCountView.text = "Bạn chưa có bài viết nào !"
                   }
                    else
                   {
                       articlesCountView.text = "Số lượng bài viết bạn đã đăng:" +count.toString()
                   }
                }
                .addOnFailureListener { exception ->
                    Toast.makeText(this, "Error getting documents: $exception", Toast.LENGTH_SHORT).show()
                }
        } else {
            articlesCountView.text = "0"
        }
    }
    private fun uploadAvatar(uri: Uri) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val storage = Firebase.storage
            val storageRef = storage.reference
            val avatarRef = storageRef.child("avatars/${user.uid}.jpg")

            val uploadTask = avatarRef.putFile(uri)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                avatarRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setPhotoUri(downloadUri)
                        .build()
                    user.updateProfile(profileUpdates).addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            val userHashMap = hashMapOf(
                                "photoURL" to downloadUri.toString()
                            )

                            db.collection("users").document(currentUser.uid)
                                .update(userHashMap as Map<String, Any>)
                                .addOnSuccessListener {

                                    Glide.with(this@ProfileActivity)
                                        .load(downloadUri) // hiện avatar vừa nhập
                                        .into(avatar)
                                    Toast.makeText(this@ProfileActivity, "Avatar URL updated in Firestore.", Toast.LENGTH_SHORT).show()
                                }

                                .addOnFailureListener { exception ->
                                    Toast.makeText(this@ProfileActivity, "Failed to update avatar URL in Firestore: $exception", Toast.LENGTH_SHORT).show()
                                }
                            Toast.makeText(this@ProfileActivity, "Avatar updated successfully.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@ProfileActivity, "Failed to update avatar.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this@ProfileActivity, "Failed to get download URL: $exception", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this@ProfileActivity, "Failed to upload avatar: $exception", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this@ProfileActivity, "User not logged in", Toast.LENGTH_SHORT).show()
        }
    }
    private fun loadAvatarUrl(uid: String?) {
        if (uid != null) {
            db.collection("users").document(uid)
                .get()
                .addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                    val photoURL = documentSnapshot.getString("photoURL")
                    if (photoURL != null) {
                        Glide.with(this)
                            .load(photoURL)
                            .placeholder(R.drawable.ic_baseline_account_circle_24) // Hiển thị ảnh đại diện mặc định khi đang tải
                            .error(R.drawable.ic_baseline_account_circle_24) // Hiển thị ảnh đại diện mặc định nếu tải ảnh gặp lỗi
                            .into(avatar)
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error loading avatar: $e", Toast.LENGTH_SHORT).show()
                }
        }
    }
    private fun mapping()
    {
        username = findViewById(R.id.nameuser)
        emailUser = findViewById(R.id.emailUser)
        btnHome = findViewById(R.id.btnHome)
        btnLogout = findViewById(R.id.btnLogout)
        btn_avatar = findViewById(R.id.btn_avatar)
        changePassword = findViewById(R.id.changePassword)
        avatar = findViewById(R.id.avatar)
    }

}