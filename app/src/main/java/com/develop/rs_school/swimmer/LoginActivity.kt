package com.develop.rs_school.swimmer

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.develop.rs_school.swimmer.network.SwimmerApi.firstAuth
import com.develop.rs_school.swimmer.network.auth
import kotlinx.android.synthetic.main.login_activity.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher


class LoginActivity : AppCompatActivity() {

    private var coroutineJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + coroutineJob)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        uiScope.launch {firstAuth()}

        val slots = UnderscoreDigitSlotsParser().parseSlots("+375(__)___-__-__")
        val formatWatcher: FormatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
        formatWatcher.installOn(text_input)

        button_signin.setOnClickListener {

            uiScope.launch {
                val authApiStatus = auth(text_input.text.toString())
                if(authApiStatus != ""){
                    val mainActivity = Intent(this@LoginActivity, MainActivity::class.java)

                    startActivity(mainActivity)
                    finish()
                }
                else{
                    Toast.makeText(this@LoginActivity, "Incorrect data. Try again!", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }


    override fun onDestroy() {
        super.onDestroy()
        coroutineJob.cancel()
    }
}
