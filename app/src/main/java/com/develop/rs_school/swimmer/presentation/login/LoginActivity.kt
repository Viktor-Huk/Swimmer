package com.develop.rs_school.swimmer.presentation.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.develop.rs_school.swimmer.presentation.main.ui.MainActivity
import com.develop.rs_school.swimmer.R
import com.develop.rs_school.swimmer.SwimmerApp
import com.develop.rs_school.swimmer.data.SessionSource
import com.develop.rs_school.swimmer.databinding.ActivityLoginBinding
import com.develop.rs_school.swimmer.data.network.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import javax.inject.Inject


class LoginActivity : AppCompatActivity() {

    private var coroutineJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + coroutineJob)

    @Inject
    lateinit var sessionSource: SessionSource

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as SwimmerApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(getSavedSession()!=""){
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }

        //FIXME
        //uiScope.launch {firstAuth()}

        val slots = UnderscoreDigitSlotsParser().parseSlots(getString(R.string.phoneNumberMask))
        val formatWatcher: FormatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
        formatWatcher.installOn(binding.textInput)

        binding.buttonSignin.setOnClickListener {
            uiScope.launch {
                val authApiStatus = auth(binding.textInput.text.toString())
                if(authApiStatus != ""){
                    val mainActivityIntent = Intent(this@LoginActivity, MainActivity::class.java)
                    createSession(authApiStatus)
                    startActivity(mainActivityIntent)
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

    private fun createSession(session: String){
        sessionSource.saveSession(session)
    }
    private fun getSavedSession(): String {
        return sessionSource.getSession()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineJob.cancel()
    }
}
