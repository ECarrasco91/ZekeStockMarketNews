package com.ezequielc.zekestockmarketnews.util

import com.ezequielc.zekestockmarketnews.BuildConfig

const val DATABASE_NAME = "news_db"
const val STOCK_DATA_TOKEN = BuildConfig.STOCK_DATA_API_TOKEN
const val STOCK_DATA_BASE_URL = "https://api.stockdata.org/v1/"
const val DEFAULT_KEY = "default_news"
const val REFRESH_KEY = "refresh_news"
const val STOCK_DATA_STARTING_PAGE_INDEX = 1
const val NETWORK_PAGE_SIZE = 2 // free news request limit
const val QUERY_KEY = "query"
const val QUERY_DEFAULT = ""
const val FINNHUB_TOKEN = BuildConfig.FINNHUB_API_TOKEN
const val FINNHUB_BASE_URL = "https://finnhub.io/api/v1/"
const val CHART_LABEL = ""