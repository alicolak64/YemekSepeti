package com.mobil.yemeksepeti.Customer

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mobil.yemeksepeti.databinding.ActivityCustomerPaymentPageBinding
import java.text.DecimalFormat
import java.util.ArrayList


class CustomerPaymentPageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCustomerPaymentPageBinding
    var email = ""
    private var fullName = ""
    private var address = ""
    private lateinit var userID : String
    private lateinit var database: DatabaseReference
    private lateinit var firebaseAuth: FirebaseAuth

    private var list = ArrayList<FoodCart>()
    private var list2 = ArrayList<FoodCart>()
    private var list3 = ArrayList<FoodCart>()
    private var listOrder = ArrayList<Order>()
    lateinit var name : String
    lateinit var category : String
    private var price : Double = 0.0
    private var foodCount = 0
    private var x = 0
    private var totalPrice = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerPaymentPageBinding.inflate(layoutInflater)

        val intent = intent
        fullName = intent.getStringExtra("fullName").toString()
        email = intent.getStringExtra("email").toString()
        address = intent.getStringExtra("address").toString()



        firebaseAuth = FirebaseAuth.getInstance()
        userID = FirebaseAuth.getInstance().uid.toString()
        foodCount=0

        database = Firebase.database.getReferenceFromUrl("https://fooddelivery-51f56-default-rtdb.firebaseio.com/")


        setContentView(binding.root)

        database.child(userID).child("Carts").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){

                    val tempCount = i.child("foodCount").value
                    val tempName = i.child("foodName").value
                    val tempPrice = i.child("foodPrice").value
                    list3.add(FoodCart(tempName.toString(),tempPrice.toString(),tempCount.toString()))

                    x++
                    if (x == snapshot.childrenCount.toInt()) {
                        for (i in list3) {
                            totalPrice += (i.foodPrice.toDouble()) * (i.foodCount.toInt())
                        }
                        val numberFormat = DecimalFormat.getCurrencyInstance()
                        numberFormat.maximumFractionDigits = 2
                        binding.textViewTotalPrice.text = "Total Price : " + numberFormat.format(totalPrice)
                    }
                }



            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

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
    fun orderButton(view: View){
        val dialogClickListener =
            DialogInterface.OnClickListener { dialog, which ->
                when (which) {
                    DialogInterface.BUTTON_POSITIVE -> {

                        // Fill List
                        database.child(userID).child("Carts").addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(i in snapshot.children){

                                    val tempCount = i.child("foodCount").value
                                    val tempName = i.child("foodName").value
                                    val tempPrice = i.child("foodPrice").value
                                    list.add(FoodCart(tempName.toString(),tempPrice.toString(),tempCount.toString()))

                                    x++
                                    if (x == snapshot.childrenCount.toInt()) {
                                        for (i in list) {
                                            totalPrice += (i.foodPrice.toDouble()) * (i.foodCount.toInt())
                                        }
                                        val numberFormat = DecimalFormat.getCurrencyInstance()
                                        numberFormat.maximumFractionDigits = 2
                                        binding.textViewTotalPrice.text = numberFormat.format(totalPrice)
                                    }
                                }



                            }
                            override fun onCancelled(error: DatabaseError) {
                            }
                        })


                        //Remove Cart
                        database.child(userID).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                database.child(userID).child("Carts").removeValue()
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })

                        database.child(userID).child("Orders").addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                database.child(userID).child("Orders").child("foodCart").addListenerForSingleValueEvent(object : ValueEventListener {

                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        for(i in snapshot.children){

                                            val tempCount = i.child("foodCount").value
                                            val tempName = i.child("foodName").value
                                            val tempPrice = i.child("foodPrice").value
                                            list2.add(FoodCart(tempName.toString(),tempPrice.toString(),tempCount.toString()))

                                        }
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                    }
                                })

                                for(i in snapshot.children){

                                    val tempEmail = i.child("email").value
                                    val tempName = i.child("fullName").value
                                    val tempAddress = i.child("address").value
                                    val tempUserId = i.child("userId").value
                                    val tempTotalPrice = i.child("totalPrice").value
                                    val tempStatus = i.child("isStatus").value

                                    listOrder.add(Order(list2,tempEmail.toString(),tempName.toString(),tempAddress.toString(),tempUserId.toString(),tempTotalPrice.toString(),tempStatus.toString()))

                                }

                            }
                            override fun onCancelled(error: DatabaseError) {
                            }
                        })

                        database.child(userID).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                listOrder.add(Order(list,email,fullName,address,userID,totalPrice.toString(),"0"))

                                database.child(userID).child("Orders").removeValue()
                                database.child(userID).child("Orders").setValue(listOrder)
                            }

                            override fun onCancelled(error: DatabaseError) {

                            }
                        })

                        val intent = Intent(applicationContext, CustomerMainPageActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    DialogInterface.BUTTON_NEGATIVE -> {}
                }
            }

        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage("Do you confirm the payment transaction?").setPositiveButton("Yes", dialogClickListener)
            .setNegativeButton("No", dialogClickListener).show()
    }
    fun customerLine(view: View){
        val intent = Intent(applicationContext, CustomerMainPageActivity::class.java)
        intent.putExtra("Activity","Go to others")
        startActivity(intent)
        finish()
    }
}