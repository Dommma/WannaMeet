package hu.bme.wannameet2.firststep

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hu.bme.wannameet2.R
import hu.bme.wannameet2.firststep.fragment.LoginFragment

class FirstActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_first)

        val loginFragment = LoginFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, loginFragment)
        transaction.commit()
    }
}