package com.mobil.yemeksepeti.Restaurant

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mobil.yemeksepeti.R
import com.mobil.yemeksepeti.databinding.FragmentRestaurantAddFoodBinding
import java.io.ByteArrayOutputStream


class RestaurantAddFoodFragment : Fragment() {

    private lateinit var binding: FragmentRestaurantAddFoodBinding
    private var mStorageRef: StorageReference? = null

    private lateinit var database: DatabaseReference

    var selectedImage : Uri? = null
    var selectedBitmap : Bitmap? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mStorageRef = FirebaseStorage.getInstance().reference
        database = Firebase.database.getReferenceFromUrl("https://fooddelivery-51f56-default-rtdb.firebaseio.com/")




    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentRestaurantAddFoodBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonRestaurantAddFood.setOnClickListener {
            addFood(it)
        }
        binding.imageViewRestaurantFoodPhoto.setOnClickListener {
            selectImage(it)
        }
    }

    private fun addFood (view: View) {

        val foodName=binding.editTextRestaurantFoodName.text.toString()
        val foodPrice=binding.editTextRestaurantFoodPrice.text.toString()
        val selectedOption = binding.radioGroupAddFood.checkedRadioButtonId

        val button: Button = binding.radioGroupAddFood.findViewById(selectedOption)

        val foodCategory = button.text


        if(selectedBitmap!=null){
            val smallBitmap = createSmallBitmap(selectedBitmap!!,300)

            val outputStream = ByteArrayOutputStream()
            smallBitmap.compress(Bitmap.CompressFormat.PNG,50,outputStream)
            val byteArray = outputStream.toByteArray()

            val myStorageRef = mStorageRef!!.child("images/$foodName.jpeg")
            val uploadTask = myStorageRef.putBytes(byteArray)


            myStorageRef.putBytes(byteArray)
                .addOnSuccessListener { // if the upload is successful hide the progress dialog


                    when (foodCategory) {
                        "Soup" -> {

                            database.child("Soup").addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.hasChild(foodName)){
                                        Toast.makeText(context,"Soup is already added",
                                            Toast.LENGTH_LONG).show()

                                    }else{
                                        database.child("Soup").child(foodName).child("category").setValue(foodCategory)
                                        database.child("Soup").child(foodName).child("name").setValue(foodName)
                                        database.child("Soup").child(foodName).child("price").setValue(foodPrice)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })

                        }
                        "Main Course" -> {

                            database.child("MainCourse").addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.hasChild(foodName)){
                                        Toast.makeText(context,"Main Course is already added",
                                            Toast.LENGTH_LONG).show()

                                    }else{
                                        database.child("MainCourse").child(foodName).child("category").setValue(foodCategory)
                                        database.child("MainCourse").child(foodName).child("name").setValue(foodName)
                                        database.child("MainCourse").child(foodName).child("price").setValue(foodPrice)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })

                        }
                        "Dessert" -> {

                            database.child("Dessert").addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.hasChild(foodName)){
                                        Toast.makeText(context,"Dessert is already added",
                                            Toast.LENGTH_LONG).show()

                                    }else{
                                        database.child("Dessert").child(foodName).child("category").setValue(foodCategory)
                                        database.child("Dessert").child(foodName).child("name").setValue(foodName)
                                        database.child("Dessert").child(foodName).child("price").setValue(foodPrice)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })

                        }
                        "Drink" -> {


                            database.child("Drink").addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.hasChild(foodName)){
                                        Toast.makeText(context,"Drink is already added",
                                            Toast.LENGTH_LONG).show()

                                    }else{
                                        database.child("Drink").child(foodName).child("category").setValue(foodCategory)
                                        database.child("Drink").child(foodName).child("name").setValue(foodName)
                                        database.child("Drink").child(foodName).child("price").setValue(foodPrice)
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })

                        }
                    }

                    database.child("Food").addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.hasChild(foodName)){
                                Toast.makeText(context,"Food is already added",
                                    Toast.LENGTH_LONG).show()

                            }else{
                                database.child("Food").child(foodName).child("category").setValue(foodCategory)
                                database.child("Food").child(foodName).child("name").setValue(foodName)
                                database.child("Food").child(foodName).child("price").setValue(foodPrice)

                            }
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })



                    // and display a success toast
                    Toast.makeText(
                        this.context,
                        "File uploaded!",
                        Toast.LENGTH_LONG
                    ).show()

                    val fragment2 = RestaurantFoodFragment()
                    val fragmentManager = fragmentManager
                    val fragmentTransaction = fragmentManager!!.beginTransaction()
                    fragmentTransaction.replace(R.id.fragmentContainerView, fragment2)
                    fragmentTransaction.commit()

                }
                .addOnFailureListener { exception -> // if the upload is not successful hide the progress dialog
                    // and display an error toast
                    Toast.makeText(
                        this.context,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()
                }.addOnProgressListener { taskSnapshot -> //calculating progress percentage
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    //displaying percentage in progress dialog
                }
        }

    }

    private fun selectImage(view: View){

        activity?.let {
            if (ContextCompat.checkSelfPermission(it.applicationContext,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
            } else {
                // already let permission , go to gallery without asking permission again
                val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent,2)
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                // let permission
                val galleryIntent = Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(galleryIntent,2)

            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == 2 && resultCode == Activity.RESULT_OK && data != null){

            selectedImage = data.data

            try {

                context?.let {
                    if(selectedImage != null) {
                        if( Build.VERSION.SDK_INT >= 28){
                            val source = ImageDecoder.createSource(it.contentResolver,selectedImage!!)
                            selectedBitmap = ImageDecoder.decodeBitmap(source)
                            binding.imageViewRestaurantFoodPhoto.setImageBitmap(selectedBitmap)
                        } else {
                            selectedBitmap = MediaStore.Images.Media.getBitmap(it.contentResolver,selectedImage)
                            binding.imageViewRestaurantFoodPhoto.setImageBitmap(selectedBitmap)
                        }

                    }
                }


            } catch (e: Exception){
                e.printStackTrace()
            }


        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    private fun createSmallBitmap(bitmap: Bitmap, maxSize: Int) : Bitmap {

        var width = bitmap.width
        var height = bitmap.height

        val percentBitmap : Double = width.toDouble() / height.toDouble()

        if (percentBitmap > 1) {
            // image vertical
            width = maxSize
            val newHeight = width / percentBitmap
            height = newHeight.toInt()
        } else {
            // image horizontal
            height = maxSize
            val newWidth = height * percentBitmap
            width = newWidth.toInt()

        }


        return Bitmap.createScaledBitmap(bitmap,width,height,true)
    }

}