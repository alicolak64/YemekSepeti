package com.mobil.yemeksepeti.Customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
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
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mobil.yemeksepeti.databinding.ActivityCustomerCartBinding
import java.text.DecimalFormat
import java.util.ArrayList


class CustomerCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerCartBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: DatabaseReference
    private var mStorageRef: StorageReference? = null
    private lateinit var userID : String
    private lateinit var recyclerView : RecyclerView
    private var foodList = ArrayList<FoodCart>()

    private var totalPrice = 0.0
    var list = ArrayList<FoodCart>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        checkUser()

        database = Firebase.database.getReferenceFromUrl("https://fooddelivery-51f56-default-rtdb.firebaseio.com/")

        storage = FirebaseStorage.getInstance()

        mStorageRef = FirebaseStorage.getInstance().reference

        userID = FirebaseAuth.getInstance().uid.toString()

        val numberFormat = DecimalFormat.getCurrencyInstance()
        numberFormat.maximumFractionDigits = 2



        recyclerView = binding.recyclerViewCustomerCartPage
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                this, DividerItemDecoration.VERTICAL_LIST
            )
        )
        recyclerView.itemAnimator = DefaultItemAnimator()

        var x = 0

        database.child(userID).child("Carts").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){

                    val tempCount = i.child("foodCount").value
                    val tempName = i.child("foodName").value
                    val tempPrice = i.child("foodPrice").value
                    list.add(FoodCart(tempName.toString(),tempPrice.toString(),tempCount.toString()))
                    foodList = list
                    x++
                    if (x == snapshot.childrenCount.toInt()) {
                        for (i in list) {
                            totalPrice += (i.foodPrice.toDouble()) * (i.foodCount.toInt())
                        }
                        val numberFormat = DecimalFormat.getCurrencyInstance()
                        numberFormat.maximumFractionDigits = 2
                        recyclerView.adapter =
                            CustomerCartRecyclerAdapter(list)
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

    fun removeCart (view: View) {
        database.child(userID).child("Carts").removeValue()
        val intent = Intent(applicationContext, CustomerMainPageActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun customerHome(view: View){
        val intent = Intent(applicationContext, CustomerMainPageActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun customerBasket(view: View){
        val intent = Intent(applicationContext, CustomerCartActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun customerSearch(view: View){
        val intent = Intent(applicationContext, CustomerSearchActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun customerLine(view: View){
        val intent = Intent(applicationContext, CustomerMainPageActivity::class.java)
        intent.putExtra("Activity","Go to others")
        startActivity(intent)
        finish()
    }


    fun continueButton(view: View){

        val selectedOption = binding.radioGroup.checkedRadioButtonId

        var address = ""
        var fullName = ""
        var email = ""
        var address1 = ""
        var address2 = ""


        val button: Button = binding.radioGroup.findViewById(selectedOption)

        database = Firebase.database.getReferenceFromUrl("https://fooddelivery-51f56-default-rtdb.firebaseio.com/")
        database.child("Customer").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {

                    fullName = i.key.toString()
                    email = i.child("email").value.toString()
                    address1 = i.child("address1").value.toString()
                    address2 = i.child("address2").value.toString()

                    if (firebaseAuth.currentUser?.email.toString() == email) {
                        when (button.text) {
                            "Address" -> {
                                address = address1
                            }
                            "Address2" -> {
                                address = address2
                            }
                        }
                        val intent = Intent(applicationContext, CustomerPaymentPageActivity::class.java)
                        intent.putExtra("fullName",fullName)
                        intent.putExtra("email",email)
                        intent.putExtra("address",address)
                        startActivity(intent)
                        finish()
                        break
                    }

                }
            }


            override fun onCancelled(error: DatabaseError) {
            }
        })
    }


}