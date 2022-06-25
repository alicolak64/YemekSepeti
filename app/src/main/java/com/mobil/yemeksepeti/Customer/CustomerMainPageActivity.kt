package com.mobil.yemeksepeti.Customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import com.mobil.yemeksepeti.R
import com.mobil.yemeksepeti.databinding.ActivityCustomerMainPageBinding


class CustomerMainPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerMainPageBinding

    private lateinit var actionBar: ActionBar

    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerMainPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent

        when {
            intent.getStringExtra("Activity") == "Go to others" -> {

                val fragmentTransaction = supportFragmentManager.beginTransaction()
                fragmentTransaction.replace(R.id.fragmentCustomerContainerView, CustomerOthersFragment())
                fragmentTransaction.commit()

            }
        }


        actionBar = supportActionBar!!
        actionBar.title = "Foods"

        firebaseAuth = FirebaseAuth.getInstance()
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

    fun customerHome(view: View){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentCustomerContainerView, CustomerFoodFragment())
        fragmentTransaction.commit()
    }
    fun customerBasket(view: View){
        val intent = Intent(applicationContext, CustomerCartActivity::class.java)
        intent.putExtra("activity","A")
        startActivity(intent)
        finish()
    }
    fun customerSearch(view: View){
        val intent = Intent(applicationContext, CustomerSearchActivity::class.java)
        startActivity(intent)
        finish()
    }
    fun customerLine(view: View){
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentCustomerContainerView, CustomerOthersFragment())
        fragmentTransaction.commit()
    }
}