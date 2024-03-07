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
import ir.mobfix.aleali.data.models.UserResponse
import ir.mobfix.aleali.databinding.ItemList1Binding
import javax.inject.Inject


class UserAdapter @Inject constructor() : RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    private lateinit var binding: ItemList1Binding
    private lateinit var context: Context
    private var moviesList = emptyList<UserResponse>()

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

        @SuppressLint("SetTextI18n")
        fun bind(item: UserResponse) {
            binding.apply {
               // textMenu.text = item.text
                //textMenu.setBackgroundColor(ContextCompat.getColor(context,item.color))
               // imgMenu.setColorFilter(ContextCompat.getColor(context,item.color))
             /*   imgMenu.load(item.img){
                    crossfade(500)
                    crossfade(true)
                }*/
                root.setOnClickListener {
                    onItemClickListener?.let {
                        it(item)
                    }
                }
            }
        }
    }

    private var onItemClickListener: ((UserResponse) -> Unit)? = null
    fun setOnItemClickListener(listener: (UserResponse) -> Unit) {
        onItemClickListener = listener
    }

    fun setData(data: List<UserResponse>) {
        val moviesDiffUtil = NotesDiffUtils(moviesList, data)
        val diffUtils = DiffUtil.calculateDiff(moviesDiffUtil)
        moviesList = data
        diffUtils.dispatchUpdatesTo(this)
    }

    class NotesDiffUtils(private val oldItem: List<UserResponse>, private val newItem: List<UserResponse>) : DiffUtil.Callback() {

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