package com.mobil.yemeksepeti.Restaurant

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mobil.yemeksepeti.databinding.ActivityRestaurantUpdateFoodBinding
import java.io.ByteArrayOutputStream

class RestaurantUpdateFoodActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRestaurantUpdateFoodBinding
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var database: DatabaseReference
    private var mStorageRef: StorageReference? = null


    var selectedImage : Uri? = null
    var selectedBitmap : Bitmap? = null
    lateinit var name : String
    lateinit var price : String
    lateinit var category : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRestaurantUpdateFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        firebaseAuth = FirebaseAuth.getInstance()

        database = Firebase.database.getReferenceFromUrl("https://fooddelivery-51f56-default-rtdb.firebaseio.com/")

        storage = FirebaseStorage.getInstance()

        mStorageRef = FirebaseStorage.getInstance().reference

        val intent = intent
        name = intent.getStringExtra("name").toString()
        price = intent.getStringExtra("price").toString()
        category = intent.getStringExtra("category").toString()


        binding.editTextRestaurantFoodName.setText(name)
        binding.editTextRestaurantFoodPrice.setText(price)

        when (category) {
            "Soup" -> {
                binding.radioButtonSoup.isChecked = true
            }
            "Main Course" -> {
                binding.radioButtonMainCourse.isChecked = true
            }
            "Dessert" -> {
                binding.radioButtonDessert.isChecked = true
            }
            "Drink" -> {
                binding.radioButtonDrink.isChecked = true
            }
        }



        val photoId = storage.reference.child("images/$name.jpeg")

        val ONE_MEGABYTE: Long = 1024 * 1024
        photoId.getBytes(ONE_MEGABYTE).addOnSuccessListener { bytes ->
            selectedBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            binding.imageViewRestaurantFoodPhoto.setImageBitmap(selectedBitmap)
        }.addOnFailureListener {
            // Handle any errors
        }



    }


    fun selectImage(view: View){

        if (ContextCompat.checkSelfPermission(applicationContext,Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
        } else {
            // already let permission , go to gallery without asking permission again
            val galleryIntent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 2)
        }
    }

    fun updateFood (view: View) {



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
                                    database.child("Soup").child(name).removeValue()
                                    database.child("Soup").child(foodName).child("category").setValue(foodCategory)
                                    database.child("Soup").child(foodName).child("price").setValue(foodPrice)
                                    database.child("Soup").child(foodName).child("name").setValue(foodName)
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })

                        }
                        "Main Course" -> {

                            database.child("MainCourse").addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    database.child("MainCourse").child(name).removeValue()
                                    database.child("MainCourse").child(foodName).child("category").setValue(foodCategory)
                                    database.child("MainCourse").child(foodName).child("price").setValue(foodPrice)
                                    database.child("MainCourse").child(foodName).child("name").setValue(foodName)
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })

                        }
                        "Dessert" -> {

                            database.child("Dessert").addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    database.child("Dessert").child(name).removeValue()
                                    database.child("Dessert").child(foodName).child("category").setValue(foodCategory)
                                    database.child("Dessert").child(foodName).child("price").setValue(foodPrice)
                                    database.child("Dessert").child(foodName).child("name").setValue(foodName)
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })

                        }
                        "Drink" -> {


                            database.child("Drink").addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    database.child("Drink").child(name).removeValue()
                                    database.child("Drink").child(foodName).child("category").setValue(foodCategory)
                                    database.child("Drink").child(foodName).child("price").setValue(foodPrice)
                                    database.child("Drink").child(foodName).child("name").setValue(foodName)
                                }

                                override fun onCancelled(error: DatabaseError) {

                                }
                            })

                        }
                    }

                    database.child("Food").addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            database.child("Food").child(name).removeValue()
                            database.child("Food").child(foodName).child("category").setValue(foodCategory)
                            database.child("Food").child(foodName).child("price").setValue(foodPrice)
                            database.child("Food").child(foodName).child("name").setValue(foodName)
                        }

                        override fun onCancelled(error: DatabaseError) {

                        }
                    })



                    // and display a success toast
                    Toast.makeText(
                        this.applicationContext,
                        "Updated",
                        Toast.LENGTH_LONG
                    ).show()

                    val intent = Intent(applicationContext, RestaurantMainPageActivity::class.java)
                    startActivity(intent)

                }
                .addOnFailureListener { exception -> // if the upload is not successful hide the progress dialog
                    // and display an error toast
                    Toast.makeText(
                        this.applicationContext,
                        exception.message,
                        Toast.LENGTH_LONG
                    ).show()
                }.addOnProgressListener { taskSnapshot -> //calculating progress percentage
                    val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                    //displaying percentage in progress dialog
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

                applicationContext?.let {
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

    fun deleteFood (view: View) {

        val foodName=binding.editTextRestaurantFoodName.text.toString()
        val foodPrice=binding.editTextRestaurantFoodPrice.text.toString()
        val selectedOption = binding.radioGroupAddFood.checkedRadioButtonId

        val button: Button = binding.radioGroupAddFood.findViewById(selectedOption)

        val foodCategory = button.text

        when (foodCategory) {
            "Soup" -> {

                database.child("Soup").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        database.child("Soup").child(name).removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

            }
            "Main Course" -> {

                database.child("MainCourse").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        database.child("MainCourse").child(name).removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

            }
            "Dessert" -> {

                database.child("Dessert").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        database.child("Dessert").child(name).removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

            }
            "Drink" -> {


                database.child("Drink").addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        database.child("Drink").child(name).removeValue()
                    }

                    override fun onCancelled(error: DatabaseError) {

                    }
                })

            }
        }

        database.child("Food").addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                database.child("Food").child(name).removeValue()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        val intent = Intent(applicationContext, RestaurantMainPageActivity::class.java)
        startActivity(intent)

    }
}