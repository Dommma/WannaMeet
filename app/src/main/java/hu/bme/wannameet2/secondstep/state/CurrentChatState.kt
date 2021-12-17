package hu.bme.wannameet2.secondstep.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import hu.bme.wannameet2.epoxy.EpoxyMessageModel
import hu.bme.wannameet2.models.MessageModel

data class CurrentChatState(
    val messages: Async<List<EpoxyMessageModel>> = Uninitialized
): MvRxState