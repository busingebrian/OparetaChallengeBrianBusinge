package com.leroy.oparetachallenge.ui

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.leroy.oparetachallenge.databinding.ConversionListItemBinding
import com.leroy.oparetachallenge.models.ConversionQuote

class MainAdapter(): RecyclerView.Adapter<MainAdapter.QuoteHolder>() {

    var quotes: MutableMap<String, ConversionQuote> = mutableMapOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QuoteHolder {
        return QuoteHolder(ConversionListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: QuoteHolder, position: Int) {
        holder.bindTo(quotes.keys.elementAt(position), quotes)
    }

    override fun getItemCount(): Int {
        return quotes.size
    }

    class QuoteHolder(var binding: ConversionListItemBinding): RecyclerView.ViewHolder(binding.root) {

        fun bindTo(key: String, quotes: MutableMap<String, ConversionQuote>) {
            val conversionQuote = quotes[key]
            binding.symbolText.text = key
            binding.amountText.text = conversionQuote?.price?.toString()
            binding.lastUpdatedText.text = conversionQuote?.lastUpdated?.toString()
        }
    }
}