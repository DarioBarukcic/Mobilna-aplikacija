package hr.ferit.dariobarukcic.restorauntapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FinishOrderFragment(
    val orderList: ArrayList<Dish>
) : Fragment() {

    private val db = Firebase.firestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_finish_order, container, false)

        val username = arguments?.getString("USERNAME").toString()

        val adressEditText = view.findViewById<EditText>(R.id.adressEditText)

        val radioGroup = view.findViewById<RadioGroup>(R.id.payRadioGroup)

        view.findViewById<Button>(R.id.payButton).setOnClickListener{
            val selectedOption: Int = radioGroup.checkedRadioButtonId
            val selectedOptionButton = view.findViewById<RadioButton>(selectedOption)
            val adress = adressEditText.text.toString()
            val payingWay=selectedOptionButton.text.toString()

            if(adress==""){
                Toast.makeText(context,"Unesite adresu", Toast.LENGTH_LONG).show()
            }
            else{
                val finalOrder = Order(orderList, adress, payingWay,username)
                db.collection("orderBaskets").add(finalOrder)
                Toast.makeText(context,"Uspješno naručeno", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }
}