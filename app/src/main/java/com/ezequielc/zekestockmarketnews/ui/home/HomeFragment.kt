package com.ezequielc.zekestockmarketnews.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezequielc.zekestockmarketnews.R
import com.ezequielc.zekestockmarketnews.adapters.NewsArticleListAdapter
import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.ezequielc.zekestockmarketnews.databinding.FragmentHomeBinding
import com.ezequielc.zekestockmarketnews.interfaces.OnNewsArticleClickListener
import com.ezequielc.zekestockmarketnews.util.DEFAULT_KEY
import com.ezequielc.zekestockmarketnews.util.REFRESH_KEY
import com.ezequielc.zekestockmarketnews.util.Resource
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment(), OnNewsArticleClickListener {

    private var _binding: FragmentHomeBinding? = null
    private var latestNewsJob: Job? = null

    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val latestNewsListAdapter = NewsArticleListAdapter(this)

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

    override fun onItemClick(newsArticle: NewsArticle) {
        val direction = HomeFragmentDirections.actionNavigationHomeToDetailFragment(newsArticle)
        findNavController().navigate(direction)
    }

    override fun onBookmarkClick(newsArticle: NewsArticle) {
        homeViewModel.onBookmarkClick(newsArticle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        latestNewsJob?.cancel()
    }

    private fun showShimmer() {
        binding.apply {
            loadingState.shimmerLayout.visibility = View.VISIBLE
            loadingState.shimmerLayout.startShimmer()

            newsListRecyclerview.visibility = View.INVISIBLE
        }
    }

    private fun hideShimmer() {
        binding.apply {
            loadingState.shimmerLayout.visibility = View.INVISIBLE
            loadingState.shimmerLayout.stopShimmer()

            newsListRecyclerview.visibility = View.VISIBLE
        }
    }

    private fun showErrorSnackbar(key: String) {
        hideShimmer()
        Snackbar.make(requireView(), R.string.latest_news_error, Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.nav_view)
            .setAction(R.string.retry_text) {
                if (key == DEFAULT_KEY) showLatestNews() else refreshLatestNews()
            }.show()
    }

    private fun showLatestNews() {
        latestNewsJob?.cancel()
        latestNewsJob = lifecycleScope.launch {
            homeViewModel.loadLatestMarketNews(DEFAULT_KEY)
                .observe(viewLifecycleOwner) { resource ->
                    if (resource is Resource.Loading) showShimmer()

                    if (resource is Resource.Error) showErrorSnackbar(DEFAULT_KEY)

                    if (resource is Resource.Success) {
                        hideShimmer()
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
                    if (resource is Resource.Loading) showShimmer()

                    if (resource is Resource.Error) {
                        showErrorSnackbar(REFRESH_KEY)
                        binding.swipeRefreshLayout.isRefreshing = false
                    }

                    if (resource is Resource.Success) {
                        hideShimmer()
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