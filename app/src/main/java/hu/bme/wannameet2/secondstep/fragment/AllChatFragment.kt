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
import com.google.firebase.ktx.Firebase
import hu.bme.wannameet2.R
import hu.bme.wannameet2.chatItem
import hu.bme.wannameet2.databinding.FragmentAllChatBinding
import hu.bme.wannameet2.secondstep.ui.AllChatViewModel
import hu.bme.wannameet2.userItem
import kotlinx.android.synthetic.main.fragment_all_chat.*
import kotlin.random.Random

class AllChatFragment: BaseMvRxFragment() {

    private val allChatViewModel: AllChatViewModel by activityViewModel()
    private val dbRef = Firebase.firestore.collection("users").document(Firebase.auth.currentUser?.uid!!)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val frb = DataBindingUtil.inflate<FragmentAllChatBinding>(inflater, R.layout.fragment_all_chat, null, false)
        frb.allchatfragemnt = this
        frb.lifecycleOwner = this
        return frb.root
    }

    override fun invalidate() {
        withState(allChatViewModel) { state ->
            when(state.chats) {
                is Loading -> {
                    progressBar.visibility = View.VISIBLE
                    allRecyclerView.visibility = View.GONE
                    emptyTextView.visibility = View.GONE
                }
                is Success -> {
                    progressBar.visibility = View.GONE
                    if(state.chats.invoke().isEmpty()) {
                        allRecyclerView.visibility = View.GONE
                        emptyTextView.visibility = View.VISIBLE
                    }
                    else {
                        emptyTextView.visibility = View.GONE
                        allRecyclerView.visibility = View.VISIBLE
                        allRecyclerView.withModels {
                            for(chat in state.chats.invoke()) {
                                chatItem {
                                    id(chat.docRef)
                                    epoxyChatName(chat.name)
                                    epoxyChatNickname(chat.nickname)

                                    onClickChatContent { _ ->
                                        itemClicked(chat.docRef!!, chat.name!!)
                                    }
                                }
                            }
                        }
                    }
                }
                is Fail -> {
                    Toast.makeText(requireContext(), "Failed to load chats", Toast.LENGTH_SHORT).show()
                }
                else -> {}
            }
        }
    }

    fun floatingActionButtonOnClick() {
        val addChatFragment = AddChatFragment()
        val transaction = activity?.supportFragmentManager?.beginTransaction()!!
        transaction.replace(hu.bme.wannameet2.R.id.fragment_container, addChatFragment)
        transaction.addToBackStack("addchat")
        transaction.commit()
    }

    fun itemClicked(id: String, name: String) {
        val chatFragment = CurrentChatFragment()
        val bundle = Bundle()
        bundle.putString("chatid", id)
        bundle.putString("chatname", name)
        chatFragment.arguments = bundle
        val transaction = activity?.supportFragmentManager?.beginTransaction()!!
        transaction.replace(hu.bme.wannameet2.R.id.fragment_container, chatFragment)
        transaction.addToBackStack("addchat")
        transaction.commit()
    }

}