package hu.bme.wannameet2.secondstep.fragment

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
import com.airbnb.mvrx.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.wannameet2.R
import hu.bme.wannameet2.databinding.FragmentProfileBinding
import hu.bme.wannameet2.firststep.FirstActivity
import hu.bme.wannameet2.models.UserModel
import hu.bme.wannameet2.secondstep.ui.UserViewModel
import hu.bme.wannameet2.userItem
import kotlinx.android.synthetic.main.fragment_add_chat.*

class ProfileFragment: BaseMvRxFragment(), Observable {

    private val TAG = "ProfileFragment"
    private val userViewModel: UserViewModel by activityViewModel()
    private val dbRef = Firebase.firestore.collection("users").document(Firebase.auth.currentUser?.uid!!)

    @Bindable
    var editTextName = MutableLiveData<String>()

    @Bindable
    var editTextNickname = MutableLiveData<String>()

    override fun invalidate() {
        println("invalidalas")
        withState(userViewModel) { state ->
            when(state.user) {
                is Success -> {
                    println("Success")
                    println("Nev:"+state.user.invoke().name.toString())
                    editTextName.value = state.user.invoke().name.toString()
                    editTextNickname.value = state.user.invoke().nickname.toString()
                }
                is Fail -> {
                    Toast.makeText(requireContext(), "Failed to load personal datas", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    println("Egyeb")
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val frb = DataBindingUtil.inflate<FragmentProfileBinding>(inflater, R.layout.fragment_profile, null, false)
        frb.profilefragemnt = this
        frb.lifecycleOwner = this

        return frb.root}

    fun saveButtonOnClick() {
        dbRef.update(mapOf(
            "name" to editTextName.value,
            "nickname" to editTextNickname.value
        ))
    }

    fun signOutButtonOnClick() {
        Firebase.auth.signOut()
        val intent= Intent(activity, FirstActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
        activity?.finish()
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
}