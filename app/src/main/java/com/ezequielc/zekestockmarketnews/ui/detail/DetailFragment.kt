package com.ezequielc.zekestockmarketnews.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.ezequielc.zekestockmarketnews.R
import com.ezequielc.zekestockmarketnews.databinding.FragmentDetailBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null

    private val binding get() = _binding!!
    private val args by navArgs<DetailFragmentArgs>()
    private val detailViewModel: DetailViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentDetailBinding.bind(view)

        binding.apply {
            val newsArticle = args.newsArticle

            Glide.with(view)
                .load(newsArticle.image_url)
                .error(R.drawable.ic_image_error_24dp)
                .into(articleImageview)

            bookmarkImageview.apply {
                setOnClickListener {
                    detailViewModel.onBookmarkClick(newsArticle, this)
                    newsArticle.isBookmarked = !newsArticle.isBookmarked
                }

                setImageResource(
                    when (newsArticle.isBookmarked) {
                        true -> R.drawable.ic_bookmark_filled_24dp
                        false -> R.drawable.ic_bookmark_border_24dp
                    }
                )
            }

            titleTextview.text = newsArticle.title
            descriptionTextview.text = newsArticle.description
            timestampTextview.text = newsArticle.timestampFormatted
            sourceTextview.text = context?.resources?.getString(
                R.string.source_text, newsArticle.source
            )

            viewArticleCardview.setOnClickListener {
                val uri = Uri.parse(newsArticle.article_url)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                context?.startActivity(intent)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}