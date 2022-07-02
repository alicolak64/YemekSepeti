package com.mobil.yemeksepeti.Customer

import java.util.ArrayList

class Order(
    var foodCart: ArrayList<FoodCart>,
    var email: String,
    var fullName: String,
    var address: String,
    var userId: String,
    var totalPrice: String,
    var status: String
)