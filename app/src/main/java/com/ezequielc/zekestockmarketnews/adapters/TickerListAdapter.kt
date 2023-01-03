package com.ezequielc.zekestockmarketnews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ezequielc.zekestockmarketnews.data.Ticker
import com.ezequielc.zekestockmarketnews.databinding.NewsTickerItemBinding

class TickerListAdapter : ListAdapter<Ticker, TickerListAdapter.TickerViewHolder>(TickerComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TickerViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = NewsTickerItemBinding.inflate(layoutInflater, parent, false)
        return TickerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TickerViewHolder, position: Int) {
        val ticker = getItem(position)
        if (ticker != null) {
            holder.bind(ticker)
        }
    }

    class TickerViewHolder(
        private val binding: NewsTickerItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Ticker) {
            binding.apply {
                tickerSymbolItem.text = item.symbol
            }
        }
    }

    class TickerComparator : DiffUtil.ItemCallback<Ticker>() {
        override fun areItemsTheSame(oldItem: Ticker, newItem: Ticker) =
            oldItem.symbol == newItem.symbol

        override fun areContentsTheSame(oldItem: Ticker, newItem: Ticker) =
            oldItem == newItem
    }
}