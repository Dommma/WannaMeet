package hu.bme.wannameet2.secondstep.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.airbnb.mvrx.*
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import hu.bme.wannameet2.R
import hu.bme.wannameet2.databinding.FragmentAddChatBinding
import hu.bme.wannameet2.models.UserModel
import hu.bme.wannameet2.secondstep.ui.AddChatViewModel
import hu.bme.wannameet2.userItem
import kotlinx.android.synthetic.main.fragment_add_chat.*
import kotlinx.android.synthetic.main.fragment_add_chat.addRecyclerView
import kotlinx.android.synthetic.main.fragment_all_chat.*
import java.util.*

class AddChatFragment: BaseMvRxFragment() {

    private val addChatViewModel: AddChatViewModel by activityViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val frb = DataBindingUtil.inflate<FragmentAddChatBinding>(inflater, R.layout.fragment_add_chat, null, false)
        frb.addchatfragemnt = this
        frb.lifecycleOwner = this

        return frb.root
    }

    override fun invalidate() {
        withState(addChatViewModel) { state ->
            when(state.users) {
                is Success -> {
                    addRecyclerView.visibility = View.VISIBLE
                    addChatProgBar.visibility = View.GONE
                    println(state.users.invoke())
                    addRecyclerView.withModels {
                        for (user in state.users.invoke()) {
                            userItem {
                                id(user.id)
                                epoxyUserNickname(user.nickname)
                                epoxyUserName(user.name)

                                onClickUserContent { _ ->
                                    itemClicked(user.id)
                                }
                            }
                        }
                    }
                }
                is Loading -> {
                    addRecyclerView.visibility = View.GONE
                    addChatProgBar.visibility = View.VISIBLE
                }
                is Fail -> {
                    addChatProgBar.visibility = View.GONE
                    addRecyclerView.visibility = View.GONE
                    Toast.makeText(context, "Data loading failed!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun itemClicked(id: String) {
        Firebase.firestore.collection("users").document(Firebase.auth.currentUser?.uid!!).get().addOnSuccessListener { document ->
            val thisUser = document.toObject<UserModel>()
            if(!(thisUser?.chats.isNullOrEmpty())) {
                if(thisUser?.chats?.containsKey(id)!!) {
                    Toast.makeText(context, "You already have chat with this user!", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }
            }
            val newChatId = UUID.randomUUID()
            Firebase.firestore.collection("users").document(Firebase.auth.currentUser?.uid!!)
                .update(mapOf("chats."+id to newChatId.toString()))
            Firebase.firestore.collection("users").document(id)
                .update(mapOf("chats."+Firebase.auth.currentUser?.uid!! to newChatId.toString()))
            val hashmap = hashMapOf("messages" to null)
            Firebase.firestore.collection("chats").document(newChatId.toString()).set(hashmap)
            val fm = activity?.supportFragmentManager
            fm?.popBackStack()
        }
    }

}