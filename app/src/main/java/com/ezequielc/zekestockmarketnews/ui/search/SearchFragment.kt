package com.ezequielc.zekestockmarketnews.ui.search

import android.content.Context
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezequielc.zekestockmarketnews.adapters.SearchLoadStateAdapter
import com.ezequielc.zekestockmarketnews.adapters.SearchPagingAdapter
import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.ezequielc.zekestockmarketnews.databinding.FragmentSearchBinding
import com.ezequielc.zekestockmarketnews.interfaces.OnNewsArticleClickListener
import com.ezequielc.zekestockmarketnews.util.asMergedLoadStates
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class SearchFragment : Fragment(), OnNewsArticleClickListener {

    private var _binding: FragmentSearchBinding? = null

    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private val searchPagingAdapter = SearchPagingAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()

        // Set a listener when the user clicks on the SEARCH button in keyboard
        binding.searchInput.setOnEditorActionListener { textView, action, _ ->
            return@setOnEditorActionListener when (action) {
                EditorInfo.IME_ACTION_SEARCH -> {
                    search(textView)
                    true
                }
                else -> false
            }
        }
    }

    override fun onBookmarkClick(newsArticle: NewsArticle) {
        searchViewModel.onBookmarkClick(newsArticle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun search(view: View) {
        binding.apply {
            val query = searchInput.text?.trim().toString()

            if (query.isBlank()) {
                searchNewsTextInput.error = "Field cannot be empty"
                return
            }

            dismissKeyboard(view.windowToken)
            searchViewModel.searchNews(query)
        }
    }

    private fun dismissKeyboard(windowToken: IBinder) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(windowToken, 0)
    }

    private fun initAdapter() {
        binding.searchResultsRecyclerview.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = searchPagingAdapter.withLoadStateHeaderAndFooter(
                header = SearchLoadStateAdapter(searchPagingAdapter),
                footer = SearchLoadStateAdapter(searchPagingAdapter)
            )
        }

        lifecycleScope.launchWhenCreated {
            searchViewModel.results.collectLatest {
                searchPagingAdapter.submitData(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            // Use a state-machine to track LoadStates such that we only transition to
            // NotLoading from a RemoteMediator load if it was also presented to UI.
            searchPagingAdapter.loadStateFlow.asMergedLoadStates()
        }
    }
}