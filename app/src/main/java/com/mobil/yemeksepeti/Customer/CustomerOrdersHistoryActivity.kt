package com.mobil.yemeksepeti.Customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mobil.yemeksepeti.databinding.ActivityCustomerOrdersHistoryBinding
import java.text.DecimalFormat
import java.util.ArrayList

class CustomerOrdersHistoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerOrdersHistoryBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userID : String
    private lateinit var recyclerView : RecyclerView



    var list = ArrayList<Order2>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerOrdersHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()

        database = Firebase.database.getReferenceFromUrl("https://fooddelivery-51f56-default-rtdb.firebaseio.com/")

        userID = FirebaseAuth.getInstance().uid.toString()

        val numberFormat = DecimalFormat.getCurrencyInstance()
        numberFormat.maximumFractionDigits = 2



        recyclerView = binding.recyclerViewOrderPage
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST
            )
        )
        recyclerView.itemAnimator = DefaultItemAnimator()


        var x = 0

        database.child(userID).child("Orders").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){

                    val tempPrice = i.child("totalPrice").value.toString()
                    val tempStatus = i.child("status").value.toString()
                    val tempAddress = i.child("address").value.toString()
                    list.add(Order2(tempAddress,tempPrice,tempStatus))
                    x++
                    if (x == snapshot.childrenCount.toInt()) {
                        recyclerView.adapter =
                            CustomerOrdersHistoryAdapter(list)
                    }

                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })
    }

    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser

        if(firebaseUser != null) {

            val email = firebaseUser.email
        }else {
            startActivity(Intent(this,CustomerLogInActivity::class.java))
            finish()
        }
    }

    fun customerHome(view: View) {
        val intent = Intent(applicationContext, CustomerMainPageActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun customerBasket(view: View) {
        val intent = Intent(applicationContext, CustomerCartActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun customerSearch(view: View) {
        val intent = Intent(applicationContext, CustomerSearchActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun customerLine(view: View) {
        val intent = Intent(applicationContext, CustomerMainPageActivity::class.java)
        intent.putExtra("Activity", "Go to others")
        startActivity(intent)
        finish()
    }
}