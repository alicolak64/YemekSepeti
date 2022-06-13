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
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mobil.yemeksepeti.databinding.ActivityCustomerSignUpBinding


class CustomerSignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerSignUpBinding

    private lateinit var actionBar : ActionBar
    private lateinit var progressDialog : ProgressDialog

    private lateinit var firebaseAuth: FirebaseAuth
    private var email = ""
    private var password = ""
    private var fullName = ""
    private lateinit var database: DatabaseReference



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = Firebase.database.getReferenceFromUrl("https://fooddelivery-51f56-default-rtdb.firebaseio.com/")


        actionBar = supportActionBar!!
        actionBar.title = "Sign Up"
        actionBar.setDisplayHomeAsUpEnabled(true)
        actionBar.setDisplayShowHomeEnabled(true)


        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Creating Account In...")
        progressDialog.setCanceledOnTouchOutside(false)

        firebaseAuth = FirebaseAuth.getInstance()
    }

    fun save (view: View) {
        fullName = binding.editTextCustomerSignUpFullName.text.toString().trim()
        email = binding.editTextCustomerSignUpEmail.text.toString().trim()
        password = binding.editTextCustomerSignUpPassword.text.toString().trim()
        var confirmPassword = binding.editTextCustomerSignUpConfirmPassword.text.toString().trim()



        when {
            mailControl(email) -> {
                Toast.makeText(applicationContext, "Invalid Email Format", Toast.LENGTH_LONG).show()
            }
            TextUtils.isEmpty(password) -> {
                Toast.makeText(applicationContext, "Enter Password", Toast.LENGTH_LONG).show()
            }
            TextUtils.isEmpty(fullName) -> {
                Toast.makeText(applicationContext, "Enter Name", Toast.LENGTH_LONG).show()
            }
            TextUtils.isEmpty(confirmPassword) -> {
                Toast.makeText(applicationContext, "Enter Confirm Password", Toast.LENGTH_LONG).show()
            }
            password.length < 6 -> {
                Toast.makeText(applicationContext, "Password must at least 6 characters long", Toast.LENGTH_LONG).show()
            }
            password != confirmPassword -> {
                Toast.makeText(applicationContext, "Passwords not same", Toast.LENGTH_LONG).show()
            }

            else -> {
                database.child("Customer").addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.hasChild(fullName)){
                            Toast.makeText(applicationContext,"Fullname is already registered",
                                Toast.LENGTH_LONG).show()

                        }else{
                            progressDialog.show()

                            firebaseAuth.createUserWithEmailAndPassword(email, password)
                                .addOnSuccessListener {
                                    progressDialog.dismiss()

                                    val firebaseUser = firebaseAuth.currentUser
                                    val email = firebaseUser!!.email
                                    database.child("Customer").child(fullName).child("email").setValue(email)
                                    database.child("Customer").child(fullName).child("password").setValue(password)
                                    Toast.makeText(applicationContext, "Registration Successful", Toast.LENGTH_LONG).show()

                                    startActivity(Intent(applicationContext,CustomerLogInActivity::class.java))
                                    firebaseAuth.signOut()
                                    finish()
                                }
                                .addOnFailureListener {e->
                                    progressDialog.dismiss()
                                    Toast.makeText(applicationContext,"Signup failed due to ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }

                })

            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() //go back to previous activity, when back button of actionbar clicked
        return super.onSupportNavigateUp()
    }

    fun backToLogin(view: View) {
        val intent = Intent(applicationContext, CustomerLogInActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun mailControl (email : String) : Boolean {
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    override fun onBackPressed() {
        startActivity(Intent(this,CustomerLogInActivity::class.java))
    }
}