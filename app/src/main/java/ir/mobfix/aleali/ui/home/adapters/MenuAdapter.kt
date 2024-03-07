package ir.mobfix.aleali.ui.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ir.mobfix.aleali.data.models.ResponseMenu2.ResponseMenu2Item
import ir.mobfix.aleali.databinding.ItemList1Binding

import javax.inject.Inject

class MenuAdapter @Inject constructor() : RecyclerView.Adapter<MenuAdapter.ViewHolder>() {

    private lateinit var binding: ItemList1Binding
    private lateinit var context: Context
    private var moviesList = emptyList<ResponseMenu2Item>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemList1Binding.inflate(LayoutInflater.from(parent.context), parent, false)
        context = parent.context
        return ViewHolder()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //getItem from PagingDataAdapter
        holder.bind(moviesList[position])
        //Not duplicate items
        holder.setIsRecyclable(false)
    }

    override fun getItemCount() = moviesList.size

    inner class ViewHolder : RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n", "ResourceType")
        fun bind(item: ResponseMenu2Item) {
            binding.apply {
                textMenu.text = item.title
                textMenu.setBackgroundColor(Color.parseColor(item.color))
                imgMenu.setColorFilter(Color.parseColor(item.color))
                imgMenu.load(item.image) {
                    crossfade(500)
                    crossfade(true)
                }
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((ResponseMenu2Item) -> Unit)? = null
    fun setOnItemClickListener(listener: (ResponseMenu2Item) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<ResponseMenu2Item>) {
        val moviesDiffUtil = NotesDiffUtils(moviesList, data)
        val diffUtils = DiffUtil.calculateDiff(moviesDiffUtil)
        moviesList = data
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(
        private val oldItem: List<ResponseMenu2Item>,
        private val newItem: List<ResponseMenu2Item>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldItem.size
        }

        override fun getNewListSize(): Int {
            return newItem.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldItem[oldItemPosition] === newItem[newItemPosition]
        }
    }
}