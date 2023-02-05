package hr.ferit.dariobarukcic.restorauntapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class OrderFoodFragment(
    val oldOrderList: ArrayList<Dish>
) : Fragment(), DishRecyclerAdapter.ContentListener {

    private val db = Firebase.firestore
    private lateinit var dishAdapter: DishRecyclerAdapter
    private lateinit var orderList: ArrayList<Dish>
    private lateinit var counterTextView:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_food, container, false)
        val username = arguments?.getString("USERNAME").toString()
        val dishList = ArrayList<Dish>()
        counterTextView = view.findViewById(R.id.counterTextView)
        counterTextView.text= oldOrderList.count().toString()

        dishAdapter = DishRecyclerAdapter(dishList, this@OrderFoodFragment)
        db.collection("dishes")
            .get()
            .addOnSuccessListener { result ->
                for(data in result.documents){
                    val dish = data.toObject((Dish::class.java))
                    if(dish != null){
                        dish.id = data.id
                        dishList.add(dish)
                    }
                }
                view?.findViewById<RecyclerView>(R.id.foodRecyclerView)?.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = dishAdapter
                }
            }
            .addOnFailureListener{ exception ->
                Log.w("OrderFoodFragment","Error getting documents", exception)
            }

        orderList = dishAdapter.getFinalOrder()
        orderList.addAll(oldOrderList)

        view.findViewById<Button>(R.id.endPickingButton).setOnClickListener{
            val orderBasketFragment = OrderBasketFragment(orderList)

            val bundle = Bundle()
            bundle.putString("USERNAME", username)
            orderBasketFragment.arguments = bundle

            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, orderBasketFragment)
            fragmentTransaction?.commit()
        }

        view.findViewById<Button>(R.id.orderFoodReturnButton).setOnClickListener{
            val menuFragment = MenuFragment()

            val bundle = Bundle()
            bundle.putString("USERNAME", username)
            menuFragment.arguments = bundle

            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, menuFragment)
            fragmentTransaction?.commit()
        }

        return view
    }

    override fun onItemButtonClick(index: Int, dish: Dish) {
        dishAdapter.orderDish(index)

        counterTextView.text = dishAdapter.dishCounter().toString()
    }
}