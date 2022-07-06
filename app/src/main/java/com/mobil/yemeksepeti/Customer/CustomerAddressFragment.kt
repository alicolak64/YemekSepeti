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
import com.mobil.yemeksepeti.databinding.FragmentCustomerAddressBinding


class CustomerAddressFragment : Fragment() {

    private lateinit var binding: FragmentCustomerAddressBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var database: DatabaseReference
    var fullName=""
    var email=""
    var address1=""
    var address2=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        firebaseAuth = FirebaseAuth.getInstance()
        binding = FragmentCustomerAddressBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        database = Firebase.database.reference
        database.child("Customer").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {

                    fullName = i.key.toString()
                    email = i.child("email").value.toString()
                    address1 = i.child("address1").value.toString()
                    address2 = i.child("address2").value.toString()
                    if (firebaseAuth.currentUser?.email.toString() == email) {
                        if (address1 == "null") {
                            binding.editTextAddress.setText("")
                        } else {
                            binding.editTextAddress.setText(address1)
                        }
                        if (address2 == "null") {
                            binding.editTextAddress2.setText("")
                        } else {
                            binding.editTextAddress2.setText(address2)
                        }
                        break
                    }

                }
            }


            override fun onCancelled(error: DatabaseError) {
            }
        })

        binding.addressUpdateButton.setOnClickListener {
            addressUpdateOnClick(it)
        }


    }
    private fun addressUpdateOnClick(view: View){
        updateData()
    }
    private fun updateData(){
        database = Firebase.database.getReference("Customer")

        email=firebaseAuth.currentUser?.email.toString()
        if (binding.editTextAddress.text.toString() == "") {
            database.child(fullName).child("address1").removeValue()
        } else {
            database.child(fullName).child("address1")
                .setValue(binding.editTextAddress.text.toString().trim())
        }
        if (binding.editTextAddress2.text.toString() == "") {
            database.child(fullName).child("address2").removeValue()
        } else {
            database.child(fullName).child("address2")
                .setValue(binding.editTextAddress2.text.toString().trim())
        }

    }

}