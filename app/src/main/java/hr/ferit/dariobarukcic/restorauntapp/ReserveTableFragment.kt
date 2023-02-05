package hr.ferit.dariobarukcic.restorauntapp

import android.annotation.SuppressLint
import android.app.DatePickerDialog

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class ReserveTableFragment : Fragment(){

    private val db = Firebase.firestore

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reserve_table, container, false)

        val username = arguments?.getString("USERNAME").toString()

        val dateText = view.findViewById<TextView>(R.id.dateTimeTextView)
        val dateButton = view.findViewById<Button>(R.id.dateButton)
        val returnButton = view.findViewById<Button>(R.id.reserveTableReturnButton)
        val confirmButton = view.findViewById<Button>(R.id.confirmButton)
        var date=""

        dateButton.setOnClickListener{
            val cal = Calendar.getInstance()
            val datePicker = DatePickerDialog.OnDateSetListener{ datePicker, year, month, day ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, month)
                cal.set(Calendar.DAY_OF_MONTH, day)

                val dateFormat = SimpleDateFormat("d.M.yyyy")
                date = dateFormat.format(cal.time)
                dateText.text ="Odabrali ste: " + date
            }
            DatePickerDialog(requireContext(),datePicker,cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        returnButton.setOnClickListener{
            val menuFragment = MenuFragment()

            val bundle = Bundle()
            bundle.putString("USERNAME", username)
            menuFragment.arguments = bundle

            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, menuFragment)
            fragmentTransaction?.commit()
        }



        confirmButton.setOnClickListener{
            if(date=="")
                Toast.makeText(context, "Odaberite datum!", Toast.LENGTH_LONG).show()
            else{
                db.collection("tables")
                    .get()
                    .addOnSuccessListener { result ->
                        for(data in result.documents){
                            val table = data.toObject((Table::class.java))
                            if(table != null && data.id==date){
                                table.id = data.id
                                //Toast.makeText(context, table.times?.get(0)?.startTime.toString(), Toast.LENGTH_LONG).show()
                                if(table.counter<=0){
                                    Toast.makeText(context,"Taj dan su svi stolovi rezervirani!",Toast.LENGTH_LONG).show()
                                }else{
                                    Toast.makeText(context,"Uspješno ste rezervirali stol!",Toast.LENGTH_LONG).show()
                                    table.counter--
                                    table.users?.add(username)
                                    db.collection("tables").document(table.id!!).set(table)
                                }

                            }
                        }
                    }
                    .addOnFailureListener{ exception ->
                        Log.w("OrderFoodFragment","Error getting documents", exception)
                    }
            }

        }
        return view
    }
}