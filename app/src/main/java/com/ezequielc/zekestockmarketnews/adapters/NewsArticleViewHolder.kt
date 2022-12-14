package com.ezequielc.zekestockmarketnews.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezequielc.zekestockmarketnews.R
import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.ezequielc.zekestockmarketnews.databinding.NewsListItemBinding
import com.ezequielc.zekestockmarketnews.interfaces.OnNewsArticleClickListener

class NewsArticleViewHolder(
    private val binding: NewsListItemBinding,
    private val listener: OnNewsArticleClickListener
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NewsArticle) {
        binding.apply {
            root.setOnClickListener { listener.onItemClick(item) }

            Glide.with(itemView)
                .load(item.image_url)
                .error(R.drawable.ic_image_error_24dp)
                .into(itemArticleImageview)

            itemTitleTextview.text = item.title
            itemDescriptionTextview.text = item.description

            itemBookmarkImageview.apply {
                setOnClickListener { listener.onBookmarkClick(item) }
                setImageResource(
                    when (item.isBookmarked) {
                        true -> R.drawable.ic_bookmark_filled_24dp
                        false -> R.drawable.ic_bookmark_border_24dp
                    }
                )
            }
        }
    }
}