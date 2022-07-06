package com.mobil.yemeksepeti.Customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.mobil.yemeksepeti.R
import com.mobil.yemeksepeti.databinding.FragmentCustomerProfileBinding


class CustomerProfileFragment : Fragment() {
    private lateinit var binding: FragmentCustomerProfileBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    var fullName=""
    var email=""
    var password=""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()
        binding = FragmentCustomerProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Firebase.database.reference

        database.child("Customer").
        addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(i in snapshot.children){
                    fullName =i.child("fullname").value.toString()
                    email =i.child("email").value.toString()
                    password =i.child("password").value.toString()
                    if(firebaseAuth.currentUser?.email.toString() ==email){
                        binding.editTextCustomerPassword.setText(password)
                        binding.textViewCustomerEmail.text = email
                        binding.textViewCustomerFullName.text = i.key
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
            }
        })

        binding.buttonCustomerProfileUpdate.setOnClickListener {
            customerProfileUpdateButton(it)
        }
    }

    private fun customerProfileUpdateButton(view: View) {
        updateData()
        val fragment2 = CustomerOthersFragment()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentCustomerContainerView, fragment2)
        fragmentTransaction.commit()

    }
    private fun updateData(){
        database = Firebase.database.getReference("Customer")
        var user = mapOf<String,String>(
            "email" to binding.textViewCustomerEmail.toString(),
            "password" to binding.editTextCustomerPassword.toString()
        )
        database.child(binding.textViewCustomerFullName.text.toString()).child("password").
        setValue(binding.editTextCustomerPassword.text.toString().trim())
        firebaseAuth.currentUser?.updatePassword(binding.editTextCustomerPassword.text.toString().trim())
    }
}