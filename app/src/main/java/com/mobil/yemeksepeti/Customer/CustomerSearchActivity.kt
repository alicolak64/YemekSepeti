package com.mobil.yemeksepeti.Customer

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mobil.yemeksepeti.R

class CustomerSearchActivity : AppCompatActivity() {
    private lateinit var storage: FirebaseStorage
    private lateinit var database: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: SearchView
    val filteredList = ArrayList<String>()
    val foodPhotoList: ArrayList<Bitmap> = ArrayList()
    val tempFoodPhotoList: ArrayList<Bitmap> = ArrayList()
    val foodNameList: ArrayList<String> = ArrayList()
    val foodPriceList: ArrayList<String> = ArrayList()
    val tempFoodPriceList: ArrayList<String> = ArrayList()
    val foodCategoryList: ArrayList<String> = ArrayList()
    val tempFoodCategoryList: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_search)

        searchView = findViewById(R.id.searchView)
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredList.clear()
                tempFoodPriceList.clear()
                tempFoodCategoryList.clear()
                tempFoodPhotoList.clear()
                filterList(newText)
                return true
            }
        })
        recyclerView = findViewById(R.id.recycler_view_search)
        recyclerView.hasFixedSize()
        recyclerView.layoutManager=LinearLayoutManager(this)


        database =
            Firebase.database.getReferenceFromUrl("https://fooddelivery-51f56-default-rtdb.firebaseio.com/")

        storage = FirebaseStorage.getInstance()


        database.child("Food").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (i in snapshot.children) {
                    val foodName = i.child("name").value.toString()
                    val foodPrice = i.child("price").value.toString()
                    val foodCategory = i.child("category").value.toString()

                    foodNameList.add(foodName)
                    foodPriceList.add(foodPrice)
                    foodCategoryList.add(foodCategory)

                }

                for (imageName in foodNameList) {
                    val photoId = storage.reference.child("images/$imageName.jpeg")

                    val ONE_MEGABYTE: Long = 1024 * 1024
                    photoId.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
                        val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        foodPhotoList.add(bitmap)
                    }.addOnFailureListener {
                        // Handle any errors
                    }

                }
            }


            override fun onCancelled(error: DatabaseError) {
            }
        })
    }
    private fun filterList(text: String?) {
        var a =0
        for(i in foodNameList){
            if (i == text){
                filteredList.add(i)
                tempFoodCategoryList.add(foodCategoryList[a])
                tempFoodPriceList.add(foodPriceList[a])
                a++
            }

            a++
        }
        if (filteredList.isEmpty()){

        }else{
            Toast.makeText(this,"Data found",Toast.LENGTH_SHORT).show()
            recyclerView.adapter =
                CustomerSearchRecyclerAdapter(filteredList, tempFoodPriceList, tempFoodCategoryList)
        }
    }

    fun dessert(view: View){
        filteredList.clear()
        tempFoodPriceList.clear()
        tempFoodCategoryList.clear()
        tempFoodPhotoList.clear()
        var a = 0
        for (i in foodCategoryList){
            if(i == "Dessert") {
                filteredList.add(foodNameList[a])
                tempFoodCategoryList.add(foodCategoryList[a])
                tempFoodPriceList.add(foodPriceList[a])
                a++
            }else{
                a++
            }
            recyclerView.adapter =
                CustomerSearchRecyclerAdapter(filteredList, tempFoodPriceList, tempFoodCategoryList)

        }
    }
    fun drink(view: View){
        filteredList.clear()
        tempFoodPriceList.clear()
        tempFoodCategoryList.clear()
        tempFoodPhotoList.clear()
        var a = 0
        for (i in foodCategoryList){
            if(i == "Drink") {
                filteredList.add(foodNameList[a])
                tempFoodCategoryList.add(foodCategoryList[a])
                tempFoodPriceList.add(foodPriceList[a])
                a++
            }else{
                a++
            }
            recyclerView.adapter =
                CustomerSearchRecyclerAdapter(filteredList, tempFoodPriceList, tempFoodCategoryList)
        }

    }
    fun mainCourse(view: View){
        filteredList.clear()
        tempFoodPriceList.clear()
        tempFoodCategoryList.clear()
        tempFoodPhotoList.clear()
        var a = 0
        for (i in foodCategoryList){
            if(i == "Main Course") {
                filteredList.add(foodNameList[a])
                tempFoodCategoryList.add(foodCategoryList[a])
                tempFoodPriceList.add(foodPriceList[a])
                a++
            }else{
                a++
            }
            recyclerView.adapter =
                CustomerSearchRecyclerAdapter(filteredList, tempFoodPriceList, tempFoodCategoryList)
        }
    }
    fun soup(view: View){
        filteredList.clear()
        tempFoodPriceList.clear()
        tempFoodCategoryList.clear()
        tempFoodPhotoList.clear()
        var a = 0
        for (i in foodCategoryList){
            if(i == "Soup") {
                filteredList.add(foodNameList[a])
                tempFoodCategoryList.add(foodCategoryList[a])
                tempFoodPriceList.add(foodPriceList[a])
                a++
            }else{
                a++
            }
            recyclerView.adapter =
                CustomerSearchRecyclerAdapter(filteredList, tempFoodPriceList, tempFoodCategoryList)
        }
    }

    fun customerHome(view: View) {
        val intent = Intent(applicationContext, CustomerMainPageActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun customerBasket(view: View) {
        val intent = Intent(applicationContext, CustomerCartActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun customerSearch(view: View) {
        val intent = Intent(applicationContext, CustomerSearchActivity::class.java)
        startActivity(intent)
        finish()
    }

    fun customerLine(view: View) {
        val intent = Intent(applicationContext, CustomerMainPageActivity::class.java)
        intent.putExtra("Activity", "Go to others")
        startActivity(intent)
        finish()
    }
}