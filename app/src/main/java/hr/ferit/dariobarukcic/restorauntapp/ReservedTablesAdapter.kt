package hr.ferit.dariobarukcic.restorauntapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


class ReservedTablesAdapter(
    val items:ArrayList<Table>,
    val listener:ContentListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ReservationViewHolder(

            LayoutInflater.from(parent.context).inflate(
                R.layout.my_table_reservations_layout, parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ReservationViewHolder -> {
                holder.bind(position, listener, items[position])
            }
        }
    }

    fun removeReservation(index: Int){
        items.removeAt(index)
        notifyItemRemoved(items.size)
        notifyItemRangeChanged(index, items.size)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ReservationViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {
        private val dateTextView: TextView =
            itemView.findViewById(R.id.reservationDateTextView)
        private val removeImageView: ImageView =
            itemView.findViewById(R.id.removeImageButton)

        fun bind(index: Int, listener: ContentListener, table: Table) {
            dateTextView.text = table.id

            removeImageView.setOnClickListener{
                listener.onItemButtonClick(index,table)
            }
        }

    }

    interface ContentListener{
        fun onItemButtonClick(index: Int, table: Table)
    }
}