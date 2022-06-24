package com.mobil.yemeksepeti.Customer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.mobil.yemeksepeti.databinding.ActivityCustomerForgotPasswordBinding

class CustomerForgotPassword : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun sendLink(view: View){
        FirebaseAuth.getInstance().sendPasswordResetEmail(binding.editText.text.toString().trim())
            .addOnCompleteListener{task ->
                if (task.isSuccessful){
                    Toast.makeText(this@CustomerForgotPassword,
                        "Email sent successfully to reset your password!", Toast.LENGTH_LONG).show()
                    finish()
                    startActivity(Intent(this,CustomerLogInActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this@CustomerForgotPassword,
                        task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }
    override fun onBackPressed() {
        startActivity(Intent(this,CustomerLogInActivity::class.java))
    }
}