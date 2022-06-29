package com.mobil.yemeksepeti.Customer

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mobil.yemeksepeti.databinding.ActivityCustomerAddCartBinding
import java.text.DecimalFormat
import java.util.ArrayList

class CustomerAddCartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerAddCartBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: DatabaseReference
    private var mStorageRef: StorageReference? = null
    private lateinit var userID : String


    lateinit var name : String
    var price : Double = 0.0
    lateinit var category : String
    var foodCount = 1

    private var selectedBitmap : Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerAddCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()
        userID = FirebaseAuth.getInstance().uid.toString()
        foodCount=1

        database = Firebase.database.getReferenceFromUrl("https://fooddelivery-51f56-default-rtdb.firebaseio.com/")


        storage = FirebaseStorage.getInstance()

        mStorageRef = FirebaseStorage.getInstance().reference



        val intent = intent
        name = intent.getStringExtra("name").toString()
        val tempPrice = intent.getStringExtra("price").toString()
        price = tempPrice.toDouble()
        category = intent.getStringExtra("category").toString()

        val numberFormat = DecimalFormat.getCurrencyInstance()
        numberFormat.maximumFractionDigits = 2

        binding.textViewCustomerAddFoodFoodName.text = name
        binding.textViewCustomerAddFoodFoodPrice.text = numberFormat.format(price)

        val photoId = storage.reference.child("images/$name.jpeg")

        val ONE_MEGABYTE: Long = 1024 * 1024
        photoId.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            selectedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            binding.imageViewLoading.setImageBitmap(selectedBitmap)
        }.addOnFailureListener {
            // Handle any errors
        }
        checkUser()

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

    fun plusFood (view: View) {
        foodCount++
        update()
    }

    fun minusFood (view: View) {
        if (foodCount>=2) {
            foodCount--
        }
        update()
    }

    private fun update () {
        val numberFormat = DecimalFormat.getCurrencyInstance()
        numberFormat.maximumFractionDigits = 2
        binding.textViewCustomerAddFoodFoodPrice.text = numberFormat.format((price*foodCount))
        binding.textViewCountOfFood.text = foodCount.toString()
    }

    fun addCart (view: View) {
        val intent = Intent(this, CustomerMainPageActivity::class.java)

        val food = FoodCart(name,price.toString(),foodCount.toString())

        var list = ArrayList<FoodCart>()


        database.child(userID).child("Carts").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){

                    val tempCount = i.child("foodCount").value
                    val tempName = i.child("foodName").value
                    val tempPrice = i.child("foodPrice").value
                    list.add(FoodCart(tempName.toString(),tempPrice.toString(),tempCount.toString()))

                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        database.child(userID).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                var x = false

                for (i in list) {
                    if (i.foodName == food.foodName){
                        x = true
                    }
                }

                if (x) {
                    for (i in list) {
                        if (i.foodName == food.foodName) {
                            i.foodCount = (i.foodCount.toInt() + food.foodCount.toInt()).toString()
                        }
                    }
                } else {
                    list.add(food)
                }

                database.child(userID).child("Carts").removeValue()
                database.child(userID).child("Carts").setValue(list)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        startActivity(intent)
        finish()
    }


}