package hu.bme.wannameet2.firststep.fragment

import android.content.Intent
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
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import hu.bme.wannameet2.R
import hu.bme.wannameet2.databinding.FragmentLoginBinding
import hu.bme.wannameet2.secondstep.SecondActivity

class LoginFragment : BaseMvRxFragment(), Observable {

    private val TAG = "LoginFragment"
    private val auth: FirebaseAuth

    @Bindable
    var editTextPassword = MutableLiveData<String>()

    @Bindable
    var editTextEmail = MutableLiveData<String>()

    init {
        auth = Firebase.auth
    }

    override fun invalidate() {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val args = this.arguments
        if(args!=null) {
            val inputData = args.get("email").toString()
            editTextEmail.value = inputData
        }

        val ftb = DataBindingUtil.inflate<FragmentLoginBinding>(inflater, R.layout.fragment_login, null, false)
        ftb.lifecycleOwner = this
        ftb.loginfragemnt = this
        return ftb.root

    }

    fun toRegisterButtonOnClick() {
        val registerFragment = RegisterFragment()
        val transaction = requireFragmentManager().beginTransaction()
        transaction.replace(R.id.fragment_container, registerFragment)
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun loginButtonOnClick() {
        if(auth.currentUser != null) {
            auth.signOut()
        }
        if(editTextEmail.value.isNullOrEmpty() || editTextPassword.value.isNullOrEmpty()) {
            Toast.makeText(activity, "Fill all the fields!", Toast.LENGTH_SHORT).show()
            return
        }
        activity?.let {
            auth.signInWithEmailAndPassword(editTextEmail.value!!, editTextPassword.value!!)
                .addOnCompleteListener(it) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        successLogin()
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(activity, "Authentication failed.",
                            Toast.LENGTH_SHORT).show()
                        editTextPassword.value =""
                    }
                }
        }
    }

    private fun successLogin() {
        val intent = Intent(activity, SecondActivity::class.java)
        startActivity(intent)
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
}