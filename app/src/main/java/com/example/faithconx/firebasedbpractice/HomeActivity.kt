package com.example.faithconx.firebasedbpractice

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.faithconx.databinding.ActivityHomeBinding
import com.example.faithconx.firebasedbpractice.adapter.NameAdapter
import com.example.faithconx.firebasedbpractice.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeActivity : AppCompatActivity() {
    companion object {
        private const val MyDB = "MyDB"
        private const val NAMES = "Names"
    }

    private lateinit var binding: ActivityHomeBinding
    private var list:ArrayList<Users?> = ArrayList<Users?>()
    private val adapter = NameAdapter(list)
    private val firebaseDatabase = FirebaseDatabase.getInstance()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        setOnclickListener()

        retrieveData()
    }

    private fun retrieveData() {
        val databaseReference = firebaseDatabase.reference.child(MyDB).child("Users")
        databaseReference.addValueEventListener(object  : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                list.clear()
                for (snapshot in dataSnapshot.children){
                    val user = snapshot.getValue(Users::class.java)

                    list.add(user)
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun saveToFirebaseRealtimeDB() {
        //APPROACH 1
//        firebaseDatabase.reference.child("MyDB").child("Android").setValue("JAVA")
        //HOW TO STORE MULTIPLE DATA
        //APPROACH 2
//        val map = mutableMapOf<String, Any>()
//        map.put("Name","Avilash")
//        map.put("Email","bharatghusi@appscrip.co")
//        firebaseDatabase.reference.child(MyDB).child("map").updateChildren(map)
        //APPROACH 2
        val name = binding.editTextName.text.toString()
        firebaseDatabase.reference.child(MyDB).child(NAMES).child("N1")
            .setValue(name) //Push create a child with unique ID

    }

    private fun setOnclickListener() {
        binding.btnLogout.setOnClickListener { logoutUser() }
        binding.btnSaveToRealtimeDB.setOnClickListener { saveToFirebaseRealtimeDB() }
        binding.btnUploadObject.setOnClickListener { redirectToUploadObjectActivity() }
    }

    private fun redirectToUploadObjectActivity() {
        startActivity(Intent(this,UploadObjectActivity::class.java))
    }

    private fun logoutUser() {
        startActivity(Intent(this@HomeActivity, RegisterActivity::class.java))
        finish()
        FirebaseAuth.getInstance().signOut()
    }


}