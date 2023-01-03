package com.ezequielc.zekestockmarketnews.ui.bookmarks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ezequielc.zekestockmarketnews.adapters.NewsArticleListAdapter
import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.ezequielc.zekestockmarketnews.databinding.FragmentBookmarksBinding
import com.ezequielc.zekestockmarketnews.interfaces.OnNewsArticleClickListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BookmarksFragment : Fragment(), OnNewsArticleClickListener {

    private var _binding: FragmentBookmarksBinding? = null
    private var bookmarkedNewsJob: Job? = null

    private val binding get() = _binding!!
    private val bookmarksViewModel: BookmarksViewModel by viewModels()
    private val bookmarkNewsAdapter = NewsArticleListAdapter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentBookmarksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.bookmarkRecyclerview.apply {
            adapter = bookmarkNewsAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }

        showBookmarkedNews()
    }

    override fun onItemClick(newsArticle: NewsArticle) {
        val direction = BookmarksFragmentDirections.actionNavigationBookmarksToDetailFragment(newsArticle)
        findNavController().navigate(direction)
    }

    override fun onBookmarkClick(newsArticle: NewsArticle) {
        bookmarksViewModel.onBookmarkClick(newsArticle)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        bookmarkedNewsJob?.cancel()
    }

    private fun showBookmarkedNews() {
        bookmarkedNewsJob?.cancel()
        bookmarkedNewsJob = lifecycleScope.launch {
            bookmarksViewModel.bookmarks.observe(viewLifecycleOwner) { articles ->
                binding.apply {
                    if (articles.isEmpty()) {
                        bookmarkRecyclerview.visibility = View.INVISIBLE
                        noBookmarksTextview.visibility = View.VISIBLE
                    } else {
                        bookmarkNewsAdapter.submitList(articles)
                        noBookmarksTextview.visibility = View.INVISIBLE
                    }
                }
            }
        }
    }
}