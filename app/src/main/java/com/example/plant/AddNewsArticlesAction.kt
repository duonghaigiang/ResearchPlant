package com.example.plant
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.Timestamp

class AddNewsArticlesAction : AppCompatActivity() {
    private lateinit var username: TextView
    private lateinit var home: TextView
    private lateinit var titleNews: EditText
    private lateinit var description: EditText
    private lateinit var btnAddNews: Button
    private lateinit var getImageLauncher: ActivityResultLauncher<Intent>
    private val db = FirebaseFirestore.getInstance()
    private lateinit var img: ImageView
    private var selectedImageUri: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnews_articles_action)
        mapping()
        showNameHeader()

        getImageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val imageUri = result.data?.data
                imageUri?.let { uri ->
                    selectedImageUri = uri
                    Glide.with(this)
                        .load(uri)
                        .into(img)
                }
            }
        }
        img.setOnClickListener {
            val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            getImageLauncher.launch(galleryIntent)
        }

        btnAddNews.setOnClickListener {
            createArticle()
        }
        home.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    private fun uploadImgArticles(uri: Uri, articleId: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val storage = Firebase.storage
            val storageRef = storage.reference
            val imgArticles = storageRef.child("imgArticles/$articleId.jpg")

            val uploadTask = imgArticles.putFile(uri)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                imgArticles.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Cập nhật đường dẫn ảnh trong tài liệu có ID bài viết cụ thể
                    db.collection("articles")
                        .document(articleId)
                        .update("imgURL", downloadUri.toString())
                        .addOnSuccessListener {
                            Glide.with(this)
                                .load(downloadUri)
                                .into(img)
                            Toast.makeText(this, "Ảnh đã được cập nhật trong Firestore.", Toast.LENGTH_SHORT).show()
                        }
                        .addOnFailureListener { exception ->
                            Toast.makeText(this, "Không thể cập nhật ảnh trong Firestore: $exception", Toast.LENGTH_SHORT).show()
                        }
                }.addOnFailureListener { exception ->
                    Toast.makeText(this, "Không thể nhận đường dẫn tải xuống: $exception", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(this, "Không thể tải lên ảnh: $exception", Toast.LENGTH_SHORT).show()
            }
        } ?: run {
            Toast.makeText(this, "Người dùng chưa đăng nhập", Toast.LENGTH_SHORT).show()
        }
    }

    private fun createArticle() {
        val title = titleNews.text.toString()
        val description = description.text.toString()
        val userId = Firebase.auth.currentUser?.uid
        val currentTime = Timestamp.now()
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }
        val article = hashMapOf(
            "uid" to userId,
            "title" to title,
            "description" to description,
            "imgURL" to "",
            "likes" to arrayListOf<String>(),
            "createdAt" to currentTime,
            "updatedAt" to currentTime
        )
        db.collection("articles")
            .add(article)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Bài viết đã được tạo thành công", Toast.LENGTH_SHORT).show()
                // Chuyển đến trang chủ sau khi tạo bài viết thành công
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                selectedImageUri?.let { uri ->
                    uploadImgArticles(uri, documentReference.id)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Có lỗi xảy ra: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun showNameHeader() {
        val currentUser = Firebase.auth.currentUser
        val currentUserTextView = username
        currentUser?.let {
            currentUserTextView.text = it.displayName
        }
    }

    private fun mapping() {
        home = findViewById(R.id.home)
        titleNews = findViewById(R.id.titleNews)
        username = findViewById(R.id.username)
        description = findViewById(R.id.Description)
        btnAddNews = findViewById(R.id.btnAddNews)
        img = findViewById(R.id.img)
    }
}