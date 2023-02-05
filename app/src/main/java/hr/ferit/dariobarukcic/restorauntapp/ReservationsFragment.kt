package hr.ferit.dariobarukcic.restorauntapp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class ReservationsFragment : Fragment(), ReservedTablesAdapter.ContentListener {

    private lateinit var reservedTablesAdapter: ReservedTablesAdapter
    private val db = Firebase.firestore
    private lateinit var username:String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reservations, container, false)
        val reservationList = ArrayList<Table>()
        username = arguments?.getString("USERNAME").toString()

        reservedTablesAdapter = ReservedTablesAdapter(reservationList, this@ReservationsFragment)
        db.collection("tables")
            .get()
            .addOnSuccessListener { result ->
                for(data in result.documents){
                    val table = data.toObject((Table::class.java))
                    if(table != null && isMyTable(table)){
                        table.id = data.id
                        reservationList.add(table)
                    }
                }
                view?.findViewById<RecyclerView>(R.id.tableReservationsRecyclerView)?.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = reservedTablesAdapter
                }
            }
            .addOnFailureListener{ exception ->
                Log.w("OrderFoodFragment","Error getting documents", exception)
            }

        view.findViewById<Button>(R.id.returnButton).setOnClickListener{
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

    override fun onItemButtonClick(index: Int, removedTable: Table) {
        reservedTablesAdapter.removeReservation(index)
        var counter=0
        db.collection("tables")
            .get()
            .addOnSuccessListener { result ->
                for(data in result.documents){
                    val table = data.toObject((Table::class.java))
                    if(table != null && table.id==removedTable.id){
                        for(user in table.users!!){
                            if(user==username)
                                table.users!!.removeAt(counter)
                            counter++
                        }
                        table.counter++
                        db.collection("tables").document(table.id!!).set(table)
                    }
                }
            }
            .addOnFailureListener{ exception ->
                Log.w("OrderFoodFragment","Error getting documents", exception)
            }
    }

    private fun isMyTable(table:Table):Boolean{
        for(user:String in table.users!!){
            if(user==username)
                return true
        }
        return false
    }

}