package com.ezequielc.zekestockmarketnews.ui.ticker

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.ezequielc.zekestockmarketnews.R
import com.ezequielc.zekestockmarketnews.databinding.FragmentTickerBinding
import com.ezequielc.zekestockmarketnews.ui.chart.CustomCandleStickChart
import com.ezequielc.zekestockmarketnews.util.Resource
import com.ezequielc.zekestockmarketnews.util.changeColorFromTheme
import com.ezequielc.zekestockmarketnews.util.getColorInt
import com.github.mikephil.charting.charts.CandleStickChart
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TickerFragment : Fragment() {

    private var _binding: FragmentTickerBinding? = null
    private var tickerQuoteJob: Job? = null

    private val binding get() = _binding!!
    private val args by navArgs<TickerFragmentArgs>()
    private val tickerViewModel: TickerViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentTickerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentTickerBinding.bind(view)

        binding.apply {
            changeChartTextColorFromTheme(chart)
            chart.setNoDataText(getString(R.string.chart_loading_text))

            val ticker = args.ticker
            companyNameTextview.text = ticker.name
            tickerViewModel.setSymbol(ticker.symbol)

            timeframeToggleGroup.check(R.id.one_day_timeframe)
            setupToggleGroupListener()

            showTickerPrice(ticker.symbol)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        tickerQuoteJob?.cancel()
    }

    private fun showTickerPrice(symbol: String) {
        tickerQuoteJob?.cancel()
        tickerQuoteJob = lifecycleScope.launch {
            tickerViewModel.getRealTimeQuote(symbol).observe(viewLifecycleOwner) { resource ->
                if (resource is Resource.Success) {
                    val tickerPrice = resource.data!!
                    binding.apply {
                        tickerViewModel.setTimestamp(tickerPrice.timestamp)
                        currentTimestampTextview.text = tickerPrice.timestampFormatted
                        currentPriceTextview.text = getString(
                            R.string.ticker_current_price, tickerPrice.currentPrice
                        )

                        val percentageWithSign = tickerPrice.dayChangePercentage.plus("%")
                        showWithColor(dayChangeTextview, tickerPrice.dayChange)
                        showWithColor(percentageChangeTextview, percentageWithSign)
                    }

                    setCandleStickData(symbol, tickerPrice.timestamp)
                }

                if (resource is Resource.Error) {
                    binding.currentTimestampTextview.text = getString(R.string.us_ticker_error)
                    updateChartWithErrorMsg()
                }
            }
        }
    }

    private fun setCandleStickData(symbol: String, timestamp: Long, timeframe: String = "1D") {
        lifecycleScope.launch {
            tickerViewModel.setCandleStickData(symbol, timestamp, timeframe)
                .observe(viewLifecycleOwner) { resource ->
                    if (resource is Resource.Error) updateChartWithErrorMsg()

                    if (resource is Resource.Success) {
                        val candleStickChartData = resource.data!!
                        val customCandleStickChart = CustomCandleStickChart(requireContext())
                        customCandleStickChart.styleCandleDataSet(candleStickChartData.candleDataSet)
                        customCandleStickChart.styleChart(binding.chart, candleStickChartData)
                    }
                }
        }
    }

    private fun showWithColor(textView: TextView, value: String) {
        textView.apply {
            text = value
            setTextColor(
                if (value.contains('-')) {
                    getColorInt(context, R.color.red)
                } else {
                    text = getString(R.string.positive_value, value)
                    getColorInt(context, R.color.green)
                }
            )
        }
    }

    private fun changeChartTextColorFromTheme(chart: CandleStickChart) {
        changeColorFromTheme(
            requireContext(),
            // Code block for light theme
            { chart.setNoDataTextColor(getColorInt(requireContext(), R.color.black)) },
            // Code block for dark theme
            { chart.setNoDataTextColor(getColorInt(requireContext(), R.color.white)) }
        )
    }

    private fun updateChartWithErrorMsg() {
        binding.chart.apply {
            setNoDataText(getString(R.string.chart_error_text))
            notifyDataSetChanged()
            invalidate()
        }
    }

    private fun setupToggleGroupListener() {
        binding.timeframeToggleGroup.addOnButtonCheckedListener { _, checkId, isChecked ->
            if (isChecked) handleTimeframe(checkId)
        }
    }

    private fun handleTimeframe(resId: Int) {
        val symbol = tickerViewModel.symbol!!
        val timestamp = tickerViewModel.timestamp ?: return
        when (resId) {
            R.id.one_day_timeframe -> setCandleStickData(symbol, timestamp, "1D")
            R.id.five_day_timeframe -> setCandleStickData(symbol, timestamp, "5D")
            R.id.one_month_timeframe -> setCandleStickData(symbol, timestamp, "1M")
            R.id.six_month_timeframe -> setCandleStickData(symbol, timestamp, "6M")
            R.id.year_to_date_timeframe -> setCandleStickData(symbol, timestamp, "YTD")
            R.id.year_timeframe -> setCandleStickData(symbol, timestamp, "1Y")
        }
    }
}