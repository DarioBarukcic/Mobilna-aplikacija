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


class RegisterFragment : Fragment() {

    private lateinit var emailTextView:EditText
    private lateinit var passwordTextView:EditText
    private lateinit var confirmPasswordTextView:EditText
    private lateinit var registerButtonTextView:Button
    private lateinit var signUpButtonTextView:Button

    private lateinit var auth:FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        emailTextView = view.findViewById(R.id.emailEditText)
        passwordTextView = view.findViewById(R.id.passwordEditText)
        confirmPasswordTextView = view.findViewById(R.id.confirmPasswordEditText)
        registerButtonTextView = view.findViewById(R.id.registerButton)
        signUpButtonTextView = view.findViewById(R.id.signUpButton)

        auth = Firebase.auth

        signUpButtonTextView.setOnClickListener{
            val signUpFragment = SignUpFragment()
            val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainerView, signUpFragment)
            fragmentTransaction?.commit()
        }

        registerButtonTextView.setOnClickListener{
            registerUser()
        }

        return view
    }

    private fun registerUser(){
        val email = emailTextView.text.toString()
        val password = passwordTextView.text.toString()
        val confirmPassword = confirmPasswordTextView.text.toString()

        if (email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(activity, "Email i lozinka ne mogu biti prazni", Toast.LENGTH_LONG).show()
            return
        }

        if (password != confirmPassword) {
            Toast.makeText(activity, "Lozinka i potvrda lozinke nisu iste", Toast.LENGTH_SHORT).show()
            return
        }

        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(){
            if(it.isSuccessful){
                Toast.makeText(activity, "Registracija uspjesna", Toast.LENGTH_LONG).show()
                val signUpFragment = SignUpFragment()
                val fragmentTransaction: FragmentTransaction? = activity?.supportFragmentManager?.beginTransaction()
                fragmentTransaction?.replace(R.id.fragmentContainerView, signUpFragment)
                fragmentTransaction?.commit()
            }else{
                Toast.makeText(activity, "Registracija nije uspjela!", Toast.LENGTH_LONG).show()
            }
        }
    }
}


