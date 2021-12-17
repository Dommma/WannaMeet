package hu.bme.wannameet2.secondstep.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.Bindable
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.airbnb.mvrx.*
import hu.bme.wannameet2.R
import hu.bme.wannameet2.databinding.FragmentCurrentChatBinding
import hu.bme.wannameet2.messageItemMe
import hu.bme.wannameet2.messageItemOther
import hu.bme.wannameet2.secondstep.ui.CurrentChatViewModel
import kotlinx.android.synthetic.main.fragment_current_chat.*

class CurrentChatFragment: BaseMvRxFragment(), Observable {

    private val currentChatViewModel: CurrentChatViewModel by activityViewModel()
    private var chatId : String? = null
    private var chatName : String? = null

    @Bindable
    val messageEditText = MutableLiveData<String>()

    override fun invalidate() {
        withState(currentChatViewModel) {state ->
            when(state.messages) {
                is Loading -> {
                    currentProgressBar.visibility = View.VISIBLE
                    currentChatRecyclerView.visibility = View.GONE
                }
                is Success -> {
                    currentProgressBar.visibility = View.GONE
                    currentChatRecyclerView.visibility = View.VISIBLE
                    if(state.messages.invoke().isNotEmpty()) {
                        currentChatRecyclerView.withModels {
                            for(message in state.messages.invoke()) {
                                if (message.sender == "ME") {
                                    messageItemMe {
                                        id(message.sender+message.text)
                                        epoxyMessageText(message.text)
                                    }
                                }
                                else {
                                    messageItemOther {
                                        id(message.sender+message.text)
                                        epoxyMessageText(message.text)
                                    }
                                }


                            }
                        }
                    }
                }
                is Fail -> {

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val frb = DataBindingUtil.inflate<FragmentCurrentChatBinding>(inflater, R.layout.fragment_current_chat, null, false)
        frb.currentchatfragemnt = this
        frb.lifecycleOwner = this

        chatId = arguments?.getString("chatid")
        chatName = arguments?.getString("chatname")

        return frb.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentChatNameTv.text = chatName
        currentChatViewModel.setDbRef(chatId!!)
    }

    fun sendMessage() {
        currentChatViewModel.newMessage(messageEditText.value!!)
        messageEditText.value = ""
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {}
}