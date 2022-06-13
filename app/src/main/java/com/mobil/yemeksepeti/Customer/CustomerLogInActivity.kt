package com.mobil.yemeksepeti.Customer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mobil.yemeksepeti.databinding.ActivityCustomerLogInBinding

class CustomerLogInActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCustomerLogInBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerLogInBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}