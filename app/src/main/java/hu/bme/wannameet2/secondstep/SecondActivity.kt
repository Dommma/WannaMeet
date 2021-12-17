package hu.bme.wannameet2.secondstep

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import hu.bme.wannameet2.secondstep.fragment.AllChatFragment
import hu.bme.wannameet2.secondstep.fragment.ProfileFragment
import kotlinx.android.synthetic.main.fragment_all_chat.*
import androidx.fragment.app.FragmentManager
import hu.bme.wannameet2.secondstep.fragment.AddChatFragment


class SecondActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(hu.bme.wannameet2.R.layout.activity_second)
        val allChatFragment = AllChatFragment()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(hu.bme.wannameet2.R.id.fragment_container, allChatFragment)
        transaction.addToBackStack("allchat")
        transaction.commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(hu.bme.wannameet2.R.menu.menu_header, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            hu.bme.wannameet2.R.id.goToProfile -> {
                val profileFragment = ProfileFragment()
                val transaction = supportFragmentManager.beginTransaction()
                transaction.replace(hu.bme.wannameet2.R.id.fragment_container, profileFragment)
                transaction.addToBackStack("profile")
                transaction.commit()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val fm: FragmentManager = supportFragmentManager
        if (fm.getBackStackEntryCount() > 1) {
            fm.popBackStack()
        } else {
            finish()
        }
    }
}