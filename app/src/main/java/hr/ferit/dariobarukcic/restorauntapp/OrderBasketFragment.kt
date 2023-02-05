package hr.ferit.dariobarukcic.restorauntapp

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView

import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import java.text.DecimalFormat

class OrderBasketFragment(
    val orderList:ArrayList<Dish>,
) : Fragment(), OrderBasketRecyclerAdapter.ContentListener {

    private lateinit var basketAdapter: OrderBasketRecyclerAdapter
    lateinit var priceTextView: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_order_basket, container, false)

        val username = arguments?.getString("USERNAME").toString()

        basketAdapter = OrderBasketRecyclerAdapter(orderList, this@OrderBasketFragment)
        priceTextView = view.findViewById(R.id.calculatedPriceTextView)

        view?.findViewById<RecyclerView>(R.id.orderBasketRecyclerView)?.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = basketAdapter
        }

        calculatePrice()

        view.findViewById<Button>(R.id.basketReturnButton).setOnClickListener{
            val orderFoodFragment = OrderFoodFragment(orderList)

            val bundle = Bundle()
            bundle.putString("USERNAME", username)
            orderFoodFragment.arguments = bundle

            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, orderFoodFragment)
            fragmentTransaction?.commit()
        }

        view.findViewById<Button>(R.id.finishOrderButton).setOnClickListener{
            val finishOrderFragment = FinishOrderFragment(orderList)

            val bundle = Bundle()
            bundle.putString("USERNAME", username)
            finishOrderFragment.arguments = bundle

            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, finishOrderFragment)
            fragmentTransaction?.commit()
        }

        return view
    }

    @SuppressLint("SetTextI18n")
    fun calculatePrice(){
        var sum = 0.0
        for(dish in orderList){
            sum += dish.price!!
        }

        val format = DecimalFormat("##.##")
        val roundedNumber = format.format(sum)

        priceTextView.text = "Total=" + roundedNumber.toString()
    }

    override fun onItemButtonClick(index: Int, dish: Dish) {
        basketAdapter.removeDish(index)
        orderList.remove(dish)
        calculatePrice()
    }
}