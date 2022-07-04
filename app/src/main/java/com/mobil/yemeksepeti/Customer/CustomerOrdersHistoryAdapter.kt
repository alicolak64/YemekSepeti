package com.mobil.yemeksepeti.Customer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobil.yemeksepeti.R
import java.text.DecimalFormat

class CustomerOrdersHistoryAdapter (private var orderList : ArrayList<Order2>):
    RecyclerView.Adapter<CustomerOrdersHistoryAdapter.OrdersHistoryHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersHistoryHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.recycler_row_customer_orders_history, parent, false)
        return OrdersHistoryHolder(view)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    override fun onBindViewHolder(holder: OrdersHistoryHolder, position: Int) {
        var orderHistory = ""
        val currentItem = orderList[position]
        when (currentItem.status) {
            "0" -> {
                orderHistory += "Status :Your order is preparing\n"
            }
            "1" -> {
                orderHistory += "Status :your order is on its way\n"
            }
            "2" -> {
                orderHistory += "Status : your order has been delivered\n"
            }
        }
        val numberFormat = DecimalFormat.getCurrencyInstance()
        numberFormat.maximumFractionDigits = 2
        orderHistory += "Price :" + numberFormat.format(currentItem.totalPrice.toDouble())+"\n"
        orderHistory += "Address :" + currentItem.address

        holder.orderHistory.text = orderHistory

    }
    class OrdersHistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val orderHistory  : TextView = itemView.findViewById(R.id.textView_orderHistory)
    }

}