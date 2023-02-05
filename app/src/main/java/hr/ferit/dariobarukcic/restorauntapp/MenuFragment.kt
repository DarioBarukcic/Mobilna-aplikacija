package hr.ferit.dariobarukcic.restorauntapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentTransaction

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_menu, container, false)

        val username = arguments?.getString("USERNAME").toString()

        val orderFoodButton = view.findViewById<Button>(R.id.orderFoodButton)
        val reserveTableButton = view.findViewById<Button>(R.id.reserveTableButton)
        val orderList = ArrayList<Dish>()
        val reservationsButton = view.findViewById<Button>(R.id.ReservationsButton)

        orderFoodButton.setOnClickListener {
            val orderFoodFragment = OrderFoodFragment(orderList)

            val bundle = Bundle()
            bundle.putString("USERNAME", username)
            orderFoodFragment.arguments = bundle

            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, orderFoodFragment)
            fragmentTransaction?.commit()
        }

        reserveTableButton.setOnClickListener {
            val reserveTableFragment = ReserveTableFragment()

            val bundle = Bundle()
            bundle.putString("USERNAME", username)
            reserveTableFragment.arguments = bundle

            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, reserveTableFragment)
            fragmentTransaction?.commit()
        }

        reservationsButton.setOnClickListener{
            val reservationsFragment = ReservationsFragment()

            val bundle = Bundle()
            bundle.putString("USERNAME", username)
            reservationsFragment.arguments = bundle

            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, reservationsFragment)
            fragmentTransaction?.commit()
        }
        return view
    }
}
