package com.ezequielc.zekestockmarketnews.interfaces

import com.ezequielc.zekestockmarketnews.data.Ticker

/**
 * Listener used to process when user clicks on Ticker in RecyclerView
 */
interface OnTickerClickListener {

    fun onTickerClick(ticker: Ticker)
}