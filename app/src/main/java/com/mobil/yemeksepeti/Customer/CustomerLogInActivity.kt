package com.mobil.yemeksepeti.Customer

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.mobil.yemeksepeti.Restaurant.RestaurantLogInActivity
import com.mobil.yemeksepeti.databinding.ActivityCustomerLogInBinding

class CustomerLogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerLogInBinding

    private lateinit var actionBar: ActionBar

    private lateinit var progressDialog : ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.title = "Login"


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Logging In...")
        progressDialog.setCanceledOnTouchOutside(false)


        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()
    }

    fun login (view: View) {

        email = binding.editTextCustomerLogInEmail.text.toString().trim()
        password = binding.editTextCustomerLogInPassword.text.toString().trim()


        when {
            mailControl(email) -> {
                Toast.makeText(applicationContext, "Invalid Email Format", Toast.LENGTH_LONG).show()
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(applicationContext, "Enter Password", Toast.LENGTH_LONG).show()
            }
            else -> {

                progressDialog.show()
                firebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener {
                        progressDialog.dismiss()

                        val firebaseUser = firebaseAuth.currentUser
                        val email = firebaseUser!!.email
                        Toast.makeText(applicationContext, "Login Successful", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this,CustomerMainPageActivity::class.java))
                        finish()
                    }
                    .addOnFailureListener { e->
                        progressDialog.dismiss()
                        Toast.makeText(this, "Login failed due to ${e.message}",Toast.LENGTH_SHORT).show()
                    }

            }
        }

    }

    fun signUp(view: View) {
        val intent = Intent(applicationContext, CustomerSignUpActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun restaurant (view : View) {
        val intent = Intent (applicationContext, RestaurantLogInActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun forgotPassword (view : View) {
        val intent = Intent (applicationContext, CustomerForgotPassword::class.java)
        startActivity(intent)
        finish()
    }

    private fun mailControl (email : String) : Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun checkUser() {

        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null) {
            startActivity(Intent(this,CustomerMainPageActivity::class.java))
            finish()
        }

    }

}