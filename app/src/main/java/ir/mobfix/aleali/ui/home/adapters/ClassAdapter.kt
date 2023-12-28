package ir.mobfix.aleali.ui.home.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import ir.mobfix.aleali.data.models.ModelsStudent
import ir.mobfix.aleali.databinding.ItemList1Binding
import ir.mobfix.aleali.databinding.ItemList2Binding
import javax.inject.Inject


class ClassAdapter @Inject constructor() : RecyclerView.Adapter<ClassAdapter.ViewHolder>() {

    private lateinit var binding: ItemList2Binding
    private lateinit var context: Context
    private var moviesList = emptyList<ModelsStudent>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemList2Binding.inflate(LayoutInflater.from(parent.context), parent, false)
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

        @SuppressLint("SetTextI18n")
        fun bind(item: ModelsStudent) {
            binding.apply {
               firstNameTitle.text = item.text
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((ModelsStudent) -> Unit)? = null
    fun setOnItemClickListener(listener: (ModelsStudent) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<ModelsStudent>) {
        val moviesDiffUtil = NotesDiffUtils(moviesList, data)
        val diffUtils = DiffUtil.calculateDiff(moviesDiffUtil)
        moviesList = data
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<ModelsStudent>, private val newItem: List<ModelsStudent>) : DiffUtil.Callback() {

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