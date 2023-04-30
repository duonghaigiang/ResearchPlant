
package com.example.plant

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.graphics.Bitmap
import android.net.Uri
import android.widget.*
import com.bumptech.glide.Glide
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.io.ByteArrayOutputStream

class AddNewSpeciesAction : AppCompatActivity() {
    private lateinit var speciesSpinner: Spinner
    private val speciesList = ArrayList<String>()
    private lateinit var name : EditText
    private lateinit var Description : EditText
    private lateinit var img : ImageView
    private lateinit var btnAddNews : Button
    private val REQUEST_CAMERA_PERMISSION = 100
    private val REQUEST_IMAGE_CAPTURE = 101
    private var selectedImageUri: Uri? = null
    private val db = FirebaseFirestore.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnewspeces)
        mapping()
        populateSpeciesSpinner()
        img.setOnClickListener {
            openCamera()
        }
        btnAddNews.setOnClickListener{
            createPlants()
        }

    }
    private fun uploadImgSpecies(uri: Uri, plants: String) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        currentUser?.let { user ->
            val storage = Firebase.storage
            val storageRef = storage.reference
            val imgArticles = storageRef.child("Plants/$plants.jpg")

            val uploadTask = imgArticles.putFile(uri)
            uploadTask.addOnSuccessListener { taskSnapshot ->
                imgArticles.downloadUrl.addOnSuccessListener { downloadUri ->
                    // Cập nhật đường dẫn ảnh trong tài liệu có ID bài viết cụ thể
                    db.collection("plants")
                        .document(plants)
                        .update("imgURL", downloadUri.toString())
                        .addOnSuccessListener {
                            Glide.with(this)
                                .load(downloadUri)
                                .into(img)
                            Toast.makeText(this, "Ảnh đã được cập nhật ", Toast.LENGTH_SHORT).show()
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
    private fun createPlants(){
        val name = name.text.toString()
        val description = Description.text.toString()
        val author = Firebase.auth.currentUser?.displayName
        val userId = Firebase.auth.currentUser?.uid
        val currentTime = Timestamp.now()
        val speciesId = speciesSpinner.selectedItem.toString()
        if (title.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            return
        }
        val plants = hashMapOf(
            "uid" to userId,
            "author" to author,
            "speciesId" to speciesId,
            "name" to name,
            "description" to description,
            "imgURL" to "",
            "likes" to arrayListOf<String>(),
            "createdAt" to currentTime,
            "updatedAt" to currentTime
        )
        db.collection("plants")
            .add(plants)
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Thông tin cây đã được tạo thành công", Toast.LENGTH_SHORT).show()
                // Chuyển đến trang chủ sau khi tạo bài viết thành công
                startActivity(Intent(this, MainActivity::class.java))
                finish()
                selectedImageUri?.let { uri ->
                    uploadImgSpecies(uri, documentReference.id)
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Có lỗi xảy ra: ${e.message}", Toast.LENGTH_SHORT).show()
            }

    }
    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
    }
    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (takePictureIntent.resolveActivity(packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        } else {
            checkCameraPermission()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults) // Add this line
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera()
            }
        }
    }

    private fun getImageUri(inImage: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, inImage, "Title", null)
        return Uri.parse(path)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            val imageUri = getImageUri(imageBitmap)
            selectedImageUri = imageUri
            Glide.with(this).load(imageUri).into(img)
        }
    }
    private fun populateSpeciesSpinner() {
        db.collection("species").get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val speciesName = document.getString("name") //
                    speciesName?.let { speciesList.add(it) }
                }
                val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, speciesList)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                speciesSpinner.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error getting documents: $exception", Toast.LENGTH_SHORT).show()
            }
    }
    private fun mapping()
    {
        speciesSpinner = findViewById(R.id.speciesSpinner)
        img = findViewById(R.id.img)
        Description = findViewById(R.id.Description)
        name = findViewById(R.id.name)
        btnAddNews = findViewById(R.id.btnAddNews)
    }
}