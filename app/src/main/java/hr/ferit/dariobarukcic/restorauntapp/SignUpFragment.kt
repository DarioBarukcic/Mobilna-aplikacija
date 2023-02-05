package hr.ferit.dariobarukcic.restorauntapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class SignUpFragment : Fragment() {

    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var registerButtonEditText: Button
    private lateinit var signUpButtonEditText: Button

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        registerButtonEditText = view.findViewById(R.id.registerButton)
        signUpButtonEditText = view.findViewById(R.id.signUpButton)

        auth = Firebase.auth

        signUpButtonEditText.setOnClickListener{
            signUpUser()
        }

        registerButtonEditText.setOnClickListener{
            val registerFragment = RegisterFragment()
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, registerFragment)
            fragmentTransaction?.commit()
        }

        return view
    }

    private fun signUpUser(){
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()

        if (email.isBlank() || password.isBlank()) {
            Toast.makeText(activity, "Email i lozinka ne mogu biti prazni", Toast.LENGTH_LONG).show()
            return
        }

        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(){
            if (it.isSuccessful) {
                Toast.makeText(activity, "Uspjesna prijava kao: " + email, Toast.LENGTH_LONG).show()

                /*val sharedPref = activity?.getSharedPreferences("Login", Context.MODE_PRIVATE)
                val Ed = sharedPref!!.edit()
                Ed.putString("Unm", email)
                Ed.putString("Psw", password)
                Ed.commit()*/

                val menuFragment = MenuFragment()
                val bundle = Bundle()
                bundle.putString("USERNAME", email)
                menuFragment.arguments = bundle
                val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.fragmentContainerView, menuFragment)
                fragmentTransaction?.commit()

            }else{
                Toast.makeText(activity, "Prijava nije uspjela", Toast.LENGTH_SHORT).show()
            }
        }
    }
}