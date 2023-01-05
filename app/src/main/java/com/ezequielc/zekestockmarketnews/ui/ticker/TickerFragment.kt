package com.ezequielc.zekestockmarketnews.ui.ticker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.ezequielc.zekestockmarketnews.R
import com.ezequielc.zekestockmarketnews.databinding.FragmentTickerBinding
import com.ezequielc.zekestockmarketnews.ui.chart.CustomCandleStickChart
import com.ezequielc.zekestockmarketnews.util.Resource
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
            chart.setNoDataTextColor(getColorInt(R.color.black))
            chart.setNoDataText("Loading...")

            val ticker = args.ticker
            companyName.text = ticker.name

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
                        currentPriceTimestamp.text = tickerPrice.timestampFormatted
                        currentPrice.text = getString(
                            R.string.ticker_current_price, tickerPrice.currentPrice
                        )

                        val percentageWithSign = tickerPrice.dayChangePercentage.plus("%")
                        showWithColor(dayChange, tickerPrice.dayChange)
                        showWithColor(dayChangePercentage, percentageWithSign)
                    }

                    setCandleStickData(symbol, tickerPrice.timestamp)
                }
            }
        }
    }

    private fun setCandleStickData(symbol: String, timestamp: Long) {
        lifecycleScope.launch {
            tickerViewModel.setCandleStickData(symbol, timestamp)
                .observe(viewLifecycleOwner) { resource ->
                    if (resource is Resource.Success) {
                        val candleStickChartData = resource.data!!
                        val customCandleStickChart = CustomCandleStickChart(requireContext())
                        customCandleStickChart.styleCandleDataSet(candleStickChartData.candleDataSet)
                        customCandleStickChart.styleChart(binding.chart, candleStickChartData)
                    }

                    if (resource is Resource.Error) {
                        binding.chart.apply {
                            setNoDataText("Unable to display chart")
                            notifyDataSetChanged()
                            invalidate()
                        }
                    }
                }
        }
    }

    private fun getString(int: Int, args: String) = context?.resources?.getString(int, args)

    private fun getColorInt(color: Int) = ContextCompat.getColor(requireContext(), color)

    private fun showWithColor(textView: TextView, value: String) {
        textView.apply {
            text = value
            setTextColor(
                if (value.contains('-')) {
                    getColorInt(R.color.red)
                } else {
                    text = getString(R.string.positive_value, value)
                    getColorInt(R.color.green)
                }
            )
        }
    }
}