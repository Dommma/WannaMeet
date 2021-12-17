package hu.bme.wannameet2.secondstep.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import hu.bme.wannameet2.epoxy.EpoxyChatModel

data class AllChatState(
    val chats: Async<List<EpoxyChatModel>> = Uninitialized
): MvRxState