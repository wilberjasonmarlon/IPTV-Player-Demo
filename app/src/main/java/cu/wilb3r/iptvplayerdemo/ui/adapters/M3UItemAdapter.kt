package cu.wilb3r.iptvplayerdemo.ui.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import cu.wilb3r.iptvplayerdemo.R
import cu.wilb3r.iptvplayerdemo.data.M3UItem
import cu.wilb3r.iptvplayerdemo.databinding.ItemBinding
import java.util.*
import kotlin.collections.ArrayList

class M3UItemAdapter (
    var items: ArrayList<M3UItem>,
    var context: Context,
    var listener: AdapterListener
): RecyclerView.Adapter<M3UItemAdapter.ViewHolder>(), Filterable {

    var filterList = ArrayList<M3UItem>()

    init {
        filterList = items
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBinding.inflate(LayoutInflater.from(context),parent, false)
        )
    }

    inner class ViewHolder(val binding: ItemBinding):
        RecyclerView.ViewHolder(binding.root){
        @SuppressLint("UseCompatLoadingForDrawables")
        fun bind(item: M3UItem) {
            binding.title.text = item.mChannel
            if(!item.mLogoURL.isBlank()){
                Glide.with(context)
                    .load(item.mLogoURL)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(context.resources.getDrawable(R.drawable.ic_tv, null))
                    .into(binding.logo)
            }

        }
    }

    interface AdapterListener {
        fun onItemTap(position: Int)
    }

    override fun getItemCount(): Int {
        return filterList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.apply {
            holder.bind(items[position])
            holder.itemView.setOnClickListener {
                listener.onItemTap(position)
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    filterList = items
                } else {
                    val resultList = ArrayList<M3UItem>()
                    for (item in items) {
                        if (
                            item.mChannel.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                            || item.mGroupTitle.toLowerCase(Locale.ROOT)
                                .contains(charSearch.toLowerCase(Locale.ROOT))
                        ) {
                            resultList.add(item)
                        }
                    }
                    filterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = filterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<M3UItem>
                notifyDataSetChanged()
            }

        }
    }
}