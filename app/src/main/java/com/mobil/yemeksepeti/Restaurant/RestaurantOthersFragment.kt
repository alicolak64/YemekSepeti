package com.mobil.yemeksepeti.Restaurant

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mobil.yemeksepeti.R
import com.mobil.yemeksepeti.databinding.FragmentRestaurantOthersBinding


class RestaurantOthersFragment : Fragment() {

    private lateinit var binding: FragmentRestaurantOthersBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRestaurantOthersBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.restaurantProfileButton.setOnClickListener {
            profileOnClick(it)
        }
        binding.restaurantLogOutButton.setOnClickListener {
            logOutOnClick(it)
        }
    }

    private fun profileOnClick (view:View){
        val fragment2 = RestaurantProfileFragment()
        val fragmentManager = fragmentManager
        val fragmentTransaction = fragmentManager!!.beginTransaction()
        fragmentTransaction.replace(R.id.fragmentContainerView, fragment2)
        fragmentTransaction.commit()

    }
    private fun logOutOnClick(view: View){
        activity?.let{
            val intent = Intent (it, RestaurantLogInActivity::class.java)
            it.startActivity(intent)

        }
    }
}