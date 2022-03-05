package Apps.com.converterkt

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_splash.*
import java.util.concurrent.TimeUnit

class SplashActivity : AppCompatActivity() {
    val disposable = Observable.timer(3500, TimeUnit.MILLISECONDS)
        .subscribe {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }

        with(animationView){
            speed = 3f
        }




    }

    override fun onDestroy() {
        super.onDestroy()
        disposable.dispose()
    }
}