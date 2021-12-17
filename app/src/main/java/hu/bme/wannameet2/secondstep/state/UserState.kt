package hu.bme.wannameet2.secondstep.state

import com.airbnb.mvrx.Async
import com.airbnb.mvrx.MvRxState
import com.airbnb.mvrx.Uninitialized
import hu.bme.wannameet2.models.UserModel

data class UserState(
    val user: Async<UserModel> = Uninitialized
): MvRxState