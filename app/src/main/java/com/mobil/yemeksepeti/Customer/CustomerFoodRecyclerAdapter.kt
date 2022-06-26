package com.mobil.yemeksepeti.Customer

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobil.yemeksepeti.Customer.CustomerAddCartActivity
import com.mobil.yemeksepeti.R
import java.text.DecimalFormat


class CustomerFoodRecyclerAdapter(
    private var foodNameList: ArrayList<String>,
    private var foodPriceList: ArrayList<String>,
    private var foodCategoryList: ArrayList<String>
):
    RecyclerView.Adapter<CustomerFoodRecyclerAdapter.FoodHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row_customer_food, parent, false)
        return FoodHolder(view)
    }

    override fun onBindViewHolder(holder: FoodHolder, position: Int) {

        when (foodCategoryList[position]) {
            "Soup" -> {
                holder.foodImage.setImageResource(R.drawable.soup)
            }
            "Main Course" -> {
                holder.foodImage.setImageResource(R.drawable.maincourse)
            }
            "Dessert" -> {
                holder.foodImage.setImageResource(R.drawable.dessert)
            }
            "Drink" -> {
                holder.foodImage.setImageResource(R.drawable.drink)
            }
        }

        val currentItem = foodNameList[position]
        holder.foodName.text = currentItem
        val currentItem2 = foodPriceList[position]
        val numberFormat = DecimalFormat.getCurrencyInstance()
        numberFormat.maximumFractionDigits = 2
        holder.foodPrice.text = numberFormat.format(currentItem2.toDouble())
        holder.itemView.setOnClickListener {

            val intent = Intent(holder.itemView.context, CustomerAddCartActivity::class.java)
            intent.putExtra("name", foodNameList[holder.adapterPosition])
            intent.putExtra("price", foodPriceList[holder.adapterPosition])
            intent.putExtra("category", foodCategoryList[holder.adapterPosition])
            holder.itemView.context.startActivity(intent)

        }

    }

    override fun getItemCount(): Int {
        return foodNameList.size
    }

    class FoodHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val foodImage : ImageView = itemView.findViewById(R.id.customer_recycler_row_image)
        val foodName: TextView = itemView.findViewById(R.id.customer_recycler_row_name)
        val foodPrice: TextView = itemView.findViewById(R.id.customer_recycler_row_price)
    }
}