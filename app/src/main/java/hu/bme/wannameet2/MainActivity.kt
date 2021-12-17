package hu.bme.wannameet2

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.TextView
import hu.bme.wannameet2.firststep.FirstActivity

class MainActivity : AppCompatActivity() {

    private val SPLASH_SCREEN : Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val spalshTv = findViewById<TextView>(R.id.spalshTv)

        val animation = AnimationUtils.loadAnimation(applicationContext, R.anim.bounce_animation)
        spalshTv.startAnimation(animation)

        val handler = Handler().postDelayed({
            val intent = Intent(this@MainActivity, FirstActivity::class.java)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            startActivity(intent)
            finish()
        }, SPLASH_SCREEN)
    }
}