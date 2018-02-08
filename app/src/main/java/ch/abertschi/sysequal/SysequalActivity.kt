package ch.abertschi.sysequal

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Switch

/**
 * @author abertschi
 */
class SysequalActivity : AppCompatActivity() {

    companion object {
        val CONTENT_TYPE = 0
        val AUDIO_SESSION = 0
        val PACKAGE_NAME = "ch.abertschi.sysequal"

        val SETTING_APPLY_ON_BOOT = "apply_on_boot"

        private val EQ_CD = 0
    }

    private var showEqualizer = true
    private var hasEqualizer = true
    private var settingsView: View? = null
    private var errorView: View? = null
    private var bootToggle: Switch? = null

    var prefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
        launchEqualizer()
    }

    fun launchEqualizer() {
        val localIntent = Intent("android.media.action.DISPLAY_AUDIO_EFFECT_CONTROL_PANEL")
        localIntent.putExtra("android.media.extra.CONTENT_TYPE", CONTENT_TYPE) // Music
        localIntent.putExtra("android.media.extra.AUDIO_SESSION", AUDIO_SESSION)
        localIntent.putExtra("android.media.extra.PACKAGE_NAME", PACKAGE_NAME)
        try {
            this.startActivityForResult(localIntent, EQ_CD)
        } catch (e: RuntimeException) {
            hasEqualizer = false
            errorView?.visibility = View.VISIBLE
            settingsView?.visibility = View.GONE
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == EQ_CD) {
            showEqualizer = false
        }
    }

    override fun onPause() {
        super.onPause()
        if (hasEqualizer) {
            showEqualizer = true
        }
    }

    override fun onResume() {
        super.onResume()
        if (showEqualizer && hasEqualizer) {
            launchEqualizer()
        }
    }

    private fun storedOnBootValue(): Boolean = prefs!!.getBoolean(SETTING_APPLY_ON_BOOT, false)

    @SuppressLint("ApplySharedPref")
    private fun setupViews() {
        this.prefs = this.getSharedPreferences(
                ch.abertschi.sysequal.SysequalActivity.PACKAGE_NAME, Context.MODE_PRIVATE)

        setContentView(R.layout.activity_sysequal)
        settingsView = findViewById<View>(R.id.settings_card)
        errorView = findViewById<View>(R.id.error_card)
        bootToggle = findViewById<Switch>(R.id.bootSwitch)

        bootToggle?.isChecked = storedOnBootValue()

        bootToggle?.setOnCheckedChangeListener { compoundButton, b ->
            prefs?.edit()?.putBoolean(SETTING_APPLY_ON_BOOT, b)?.commit()
        }

        errorView?.visibility = View.GONE
        settingsView?.visibility = View.VISIBLE
    }


    fun onSettingsViewPressed(view: View?) {
        bootToggle?.isChecked = !storedOnBootValue()
    }

    fun launchEqualizer(view: View?) {
        launchEqualizer()
    }

    fun launchWebsite(view: View?) {
        val browserIntent = Intent(Intent.ACTION_VIEW,
                Uri.parse("https://abertschi.ch?rel=sysequal"))
        startActivity(browserIntent)
    }
}