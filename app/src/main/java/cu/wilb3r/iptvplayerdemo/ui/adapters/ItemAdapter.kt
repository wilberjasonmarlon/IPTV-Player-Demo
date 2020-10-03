package cu.wilb3r.iptvplayerdemo.ui.adapters


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import cu.wilb3r.iptvplayerdemo.data.M3UItem
import cu.wilb3r.iptvplayerdemo.databinding.ItemBinding
import kotlinx.android.synthetic.main.item.view.*
import javax.inject.Inject

class ItemAdapter @Inject constructor(
    private val glide: RequestManager
) : RecyclerView.Adapter<ItemAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemBinding) : RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<M3UItem>() {
        override fun areItemsTheSame(oldItem: M3UItem, newItem: M3UItem): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: M3UItem, newItem: M3UItem): Boolean {
            return oldItem == newItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var items: List<M3UItem>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    private var onItemClickListener: ((M3UItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.itemView.apply {
            title.text = item.mChannel
            if (!item.mLogoURL.isBlank()) {
                glide.load(item.mLogoURL).into(logo)
            }
            setOnClickListener {
                onItemClickListener?.let { click ->
                    click(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setOnItemClickListener(listener: (M3UItem) -> Unit) {
        onItemClickListener = listener
    }

}