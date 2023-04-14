package com.example.plant

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.example.plant.Species
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import org.checkerframework.checker.units.qual.A

class MainActivity : AppCompatActivity() {
    private lateinit var username : TextView
    private val TAG = "MyActivity"
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView : NavigationView
    private lateinit var layoutspecies : LinearLayout

    private lateinit var layoutArticles : LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mapping()

        val user = FirebaseAuth.getInstance()

        val currentUser = Firebase.auth.currentUser // viết tắt của kotlin

        val currentUserTextView = username
        currentUser?.let {
            currentUserTextView.text = it.displayName
        }


//        readFS(currentUser?.uid)
//        { data ->
//            if (data != null) {
//                val displayName = data["displayName"] as String?
//                if (displayName != null) {
//                    currentUserTextView.text = displayName
//                }
//            }
//            else
//            {
//            }
//
//        }

        // toggle bar
        toggle = ActionBarDrawerToggle(this ,drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        navView.setNavigationItemSelectedListener {
            when(it.itemId)
            {
                R.id.nav_logout -> {user.signOut()
                    startActivity(Intent(this@MainActivity, LoginAction::class.java))
                }
                R.id.nav_infor -> {
                    startActivity(Intent(this@MainActivity, ProfileActivity::class.java))
                }
            }
            true
        }

        //species
        layoutspecies.setOnClickListener {
            val intent = Intent(this@MainActivity, Species::class.java)
            startActivity(intent)
        }
        //Articles
        layoutArticles.setOnClickListener {
            val intent = Intent(this@MainActivity, ArticlesAction::class.java)
            startActivity(intent)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(toggle.onOptionsItemSelected(item))
        {
            return true
        }
        return super.onOptionsItemSelected(item)
    }
    private fun mapping()
    {
        username = findViewById(R.id.username)
        layoutArticles = findViewById(R.id.layoutArticles)
        drawerLayout = findViewById(R.id.drawerLayout)
        navView = findViewById(R.id.nav_view)
        layoutspecies = findViewById(R.id.layoutspecies)
    }
    private fun readFS(uid :String?,callback: (Map<String, Any>?) -> Unit)
    {
       if(uid != null)
       {
           val db = FirebaseFirestore.getInstance()
           db.collection("users").document(uid)
               .get()
               .addOnSuccessListener { documentSnapshot ->
                   if (documentSnapshot.exists()) {
                       // Dữ liệu tài liệu được lưu trong documentSnapshot
                       val data = documentSnapshot.data

                   } else {
                       // Tài liệu không tồn tại
                   }
               }
               .addOnFailureListener { exception ->
                   // Xử lý lỗi
               }
       }

    }
}