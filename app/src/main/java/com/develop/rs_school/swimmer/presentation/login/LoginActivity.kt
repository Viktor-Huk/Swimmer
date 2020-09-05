package com.develop.rs_school.swimmer.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.develop.rs_school.swimmer.R
import com.develop.rs_school.swimmer.SwimmerApp
import com.develop.rs_school.swimmer.databinding.ActivityLoginBinding
import com.develop.rs_school.swimmer.presentation.main.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import javax.inject.Inject


class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var loginViewModelFactory: ViewModelProvider.Factory
    private val loginViewModel by viewModels<LoginViewModel> { loginViewModelFactory }

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as SwimmerApp).appComponent.inject(this)

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loginViewModel.goToProfile.observe(this, Observer {
            if (it != null) {
                val mainActivityIntent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(mainActivityIntent)
                finish()
            }
        })

        loginViewModel.showError.observe(this, Observer {
            if (it != null)
                Snackbar.make(
                    binding.root,
                    loginViewModel.errorString,
                    Snackbar.LENGTH_SHORT
                ).show()
        })

        loginViewModel.showCodeBar.observe(this, Observer {
            it?.let {
                if (it) {
                    binding.inputCardViewCode.visibility = View.VISIBLE
                    binding.buttonSignIn.text = getString(R.string.button_sign_in)
                    binding.textInput.isEnabled = false
                } else {
                    binding.inputCardViewCode.visibility = View.GONE
                    binding.buttonSignIn.text = getString(R.string.button_get_code)
                    binding.textInput.isEnabled = true
                }
            }
        })

        val slots = UnderscoreDigitSlotsParser().parseSlots(getString(R.string.phoneNumberMask))
        val formatWatcher: FormatWatcher = MaskFormatWatcher(MaskImpl.createTerminated(slots))
        formatWatcher.installOn(binding.textInput)

        binding.buttonSignIn.setOnClickListener {
            when (binding.buttonSignIn.text) {
                getString(R.string.button_sign_in) -> loginViewModel.loginAttempt(
                    binding.textInput.text.toString(),
                    binding.textInputCode.text.toString()
                )
                getString(R.string.button_get_code) -> loginViewModel.sendCodeInSms(binding.textInput.text.toString())
            }
        }

    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) hideSystemUI()
    }

    //FIXME see rssFeed project
    private fun hideSystemUI() {
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_IMMERSIVE
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_FULLSCREEN)
    }
}
