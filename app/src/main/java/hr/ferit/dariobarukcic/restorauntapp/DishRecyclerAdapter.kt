package hr.ferit.dariobarukcic.restorauntapp

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class DishRecyclerAdapter(
    val items:ArrayList<Dish>,
    val listener: ContentListener
):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val orderBasket = ArrayList<Dish>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder {
        return DishViewHolder(

            LayoutInflater.from(parent.context).inflate(R.layout.order_dish_layout, parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder,
                                  position: Int) {
        when(holder) {
            is DishViewHolder -> {
                holder.bind(position,  listener, items[position],)
            }
        }
    }

    fun orderDish(index: Int){
        orderBasket.add(items[index])
    }

    fun getFinalOrder():ArrayList<Dish>{
        return orderBasket
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun dishCounter():Int{
        return orderBasket.count()
    }
    class DishViewHolder constructor(
        val view: View
    ): RecyclerView.ViewHolder(view) {
        private val description: TextView = view.findViewById(R.id.descriptionTextView)
        private val name: TextView = view.findViewById(R.id.nameTextView)
        private val dishImage: ImageView = view.findViewById(R.id.dishImageView)
        private val price: TextView = view.findViewById(R.id.priceTextView)
        private val orderButton = view.findViewById<Button>(R.id.orderButton)

        @SuppressLint("SetTextI18n")
        fun bind(index: Int, listener:ContentListener, dish: Dish) {
            Glide
                .with(view.context)
                .load(dish.pictureURL)
                .into(dishImage)
            description.text = dish.description
            name.text=dish.name
            price.text=dish.price.toString()+" €"

            orderButton.setOnClickListener{
                listener.onItemButtonClick(index,dish)
            }
        }
    }

    interface ContentListener{
        fun onItemButtonClick(index: Int, dish: Dish)
    }
}