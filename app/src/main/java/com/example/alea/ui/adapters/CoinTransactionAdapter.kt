package com.example.alea.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.alea.R
import com.example.alea.data.model.CoinTransaction

class CoinTransactionAdapter(private val items: List<CoinTransaction>) :
    RecyclerView.Adapter<CoinTransactionAdapter.VH>() {

    class VH(view: View) : RecyclerView.ViewHolder(view) {
        val description: TextView = view.findViewById(R.id.transaction_description)
        val date: TextView = view.findViewById(R.id.transaction_date)
        val amount: TextView = view.findViewById(R.id.transaction_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = VH(
        LayoutInflater.from(parent.context).inflate(R.layout.item_coin_transaction, parent, false)
    )

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.description.text = item.desc
        holder.date.text = item.date

        val isPositive = item.amount > 0
        holder.amount.text = if (isPositive) "+${item.amount} ★" else "${item.amount} ★"
        holder.amount.setTextColor(
            ContextCompat.getColor(holder.itemView.context,
                if (isPositive) R.color.color_success else R.color.color_error
            )
        )
    }

    override fun getItemCount() = items.size
}
