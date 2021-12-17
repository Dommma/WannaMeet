package hu.bme.wannameet2.secondstep.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import hu.bme.wannameet2.epoxy.EpoxyUserModel

data class AddChatState (
    val users: Async<List<EpoxyUserModel>> = Uninitialized
    ): MvRxState