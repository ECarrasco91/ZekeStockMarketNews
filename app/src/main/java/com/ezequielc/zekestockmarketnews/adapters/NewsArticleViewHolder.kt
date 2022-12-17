package com.ezequielc.zekestockmarketnews.adapters

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ezequielc.zekestockmarketnews.R
import com.ezequielc.zekestockmarketnews.data.NewsArticle
import com.ezequielc.zekestockmarketnews.databinding.NewsListItemBinding

class NewsArticleViewHolder(
    private val binding: NewsListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: NewsArticle) {
        binding.apply {
            Glide.with(itemView)
                .load(item.image_url)
                .error(R.drawable.ic_image_error_24dp)
                .into(itemArticleImageview)

            itemTitleTextview.text = item.title
            itemDescriptionTextview.text = item.description

            itemBookmarkImageview.apply {
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