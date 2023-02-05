package hr.ferit.dariobarukcic.restorauntapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class OrderBasketRecyclerAdapter(
    val items:ArrayList<Dish>,
    val listener: ContentListener
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {
        return DishViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.final_dish_layout, parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        when(holder) {
            is DishViewHolder -> {
                holder.bind(position, listener, items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun removeDish(index: Int){
        items.removeAt(index)
        notifyItemRemoved(items.size)
        notifyItemRangeChanged(index, items.size)
    }

    class DishViewHolder constructor(
        itemView: View
    ): RecyclerView.ViewHolder(itemView) {
        private val description: TextView = itemView.findViewById(R.id.descriptionTextView)
        private val name: TextView = itemView.findViewById(R.id.nameTextView)
        private val dishImage: ImageView = itemView.findViewById(R.id.dishImageView)
        private val price: TextView = itemView.findViewById(R.id.priceTextView)
        private val deleteButton = itemView.findViewById<ImageButton>(R.id.removeDishImageButton)
        @SuppressLint("SetTextI18n")
        fun bind(index: Int, listener:ContentListener, dish: Dish) {
            Glide
                .with(itemView.context)
                .load(dish.pictureURL)
                .into(dishImage)
            description.text = dish.description
            name.text=dish.name
            price.text=dish.price.toString()+" €"

            deleteButton.setOnClickListener{
                listener.onItemButtonClick(index,dish)
            }
        }
    }

    interface ContentListener{
        fun onItemButtonClick(index: Int, dish: Dish)
    }
}