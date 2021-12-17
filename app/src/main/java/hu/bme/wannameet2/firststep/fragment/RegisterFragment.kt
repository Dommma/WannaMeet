package hu.bme.wannameet2.firststep.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.BaseMvRxFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import hu.bme.wannameet2.R
import hu.bme.wannameet2.databinding.FragmentRegisterBinding

class RegisterFragment : BaseMvRxFragment(), Observable {

    private val TAG = "RegisterFragment"
    private val auth: FirebaseAuth
    private var successRegister: Boolean = false
    private lateinit var user : FirebaseUser
    private val database = Firebase.firestore

    @Bindable
    var editTextEmail = MutableLiveData<String>()

    @Bindable
    var editTextPassword = MutableLiveData<String>()

    @Bindable
    var editTextPassword2 = MutableLiveData<String>()

    init {
        auth = Firebase.auth
    }

    override fun invalidate() {
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val frb = DataBindingUtil.inflate<FragmentRegisterBinding>(inflater, R.layout.fragment_register, null, false)
        frb.registerfragment = this
        return frb.root    }

    fun backToLoginButtonOnClick() {
        val loginFragment = LoginFragment()
        if(successRegister) {
            val bundle = Bundle()
            bundle.putString("email", editTextEmail.value)
            loginFragment.arguments = bundle
        }
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, loginFragment)
        transaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun registerButtonOnClick() {
        if(auth.currentUser != null) {
            auth.signOut()
        }

        if(editTextEmail.value.isNullOrEmpty() || editTextPassword.value.isNullOrEmpty() || editTextPassword2.value.isNullOrEmpty()) {
            Toast.makeText(activity, "Fill all the fields!", Toast.LENGTH_SHORT).show()
            return
        }

        if(editTextPassword.value != editTextPassword2.value) {
            Toast.makeText(activity, "The passwords are not the same!", Toast.LENGTH_SHORT).show()
            return
        }

        activity?.let {
            auth.createUserWithEmailAndPassword(editTextEmail.value!!, editTextPassword.value!!)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        user = auth.currentUser!!
                        saveNewUser()
                        successRegister = true
                        backToLoginButtonOnClick()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(activity, "Authentication failed.",
                                Toast.LENGTH_SHORT).show()
                    }
                }
        }
    }

    private fun saveNewUser() {
        val newUser = hashMapOf(
            "name" to "User Name",
            "nickname" to "Nickname",
            "email" to user.email,
            "chats" to null
        )
        database.collection("users").document(user.uid).set(newUser)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
}