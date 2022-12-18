package com.ezequielc.zekestockmarketnews.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezequielc.zekestockmarketnews.adapters.NewsArticleListAdapter
import com.ezequielc.zekestockmarketnews.databinding.FragmentHomeBinding
import com.ezequielc.zekestockmarketnews.util.DEFAULT_KEY
import com.ezequielc.zekestockmarketnews.util.REFRESH_KEY
import com.ezequielc.zekestockmarketnews.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private var latestNewsJob: Job? = null

    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val latestNewsListAdapter = NewsArticleListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            newsListRecyclerview.apply {
                adapter = latestNewsListAdapter
                layoutManager = LinearLayoutManager(requireContext())
            }

            swipeRefreshLayout.setOnRefreshListener {
                refreshLatestNews()
            }
        }

        showLatestNews()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        latestNewsJob?.cancel()
    }

    private fun showLatestNews() {
        latestNewsJob?.cancel()
        latestNewsJob = lifecycleScope.launch {
            homeViewModel.loadLatestMarketNews(DEFAULT_KEY)
                .observe(viewLifecycleOwner) { resource ->
                    if (resource is Resource.Success) {
                        latestNewsListAdapter.submitList(resource.data)
                    }
                }
        }
    }

    private fun refreshLatestNews() {
        latestNewsJob?.cancel()
        latestNewsJob = lifecycleScope.launch {
            homeViewModel.loadLatestMarketNews(REFRESH_KEY)
                .observe(viewLifecycleOwner) { resource ->
                    if (resource is Resource.Success) {
                        latestNewsListAdapter.submitList(resource.data)

                        binding.apply {
                            newsListRecyclerview.scrollToPosition(0)
                            swipeRefreshLayout.isRefreshing = false
                        }
                    }
                }
        }
    }
}