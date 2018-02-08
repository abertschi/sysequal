package ch.abertschi.sysequal

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


/**
 * Created by abertschi on 08.02.18.
 */
class BootService : BroadcastReceiver() {

    private val TAG = "ch.abertschi"

    override fun onReceive(context: Context, intent: Intent) {
        // this does not seem to work, call activity to restore settings manually
        //        Intent localIntent =
        //                new Intent("android.media.action.ACTION_OPEN_AUDIO_EFFECT_CONTROL_SESSION");
        //        localIntent.putExtra("android.media.extra.CONTENT_TYPE", MainActivity.CONTENT_TYPE);
        //        localIntent.putExtra("android.media.extra.AUDIO_SESSION", MainActivity.AUDIO_SESSION);
        //        localIntent.putExtra("android.media.extra.PACKAGE_NAME", MainActivity.PACKAGE_NAME);


        val prefs = context.getSharedPreferences(SysequalActivity.PACKAGE_NAME, Context.MODE_PRIVATE)
        if (prefs!!.getBoolean(SysequalActivity.SETTING_APPLY_ON_BOOT, false)) {
            val dialogIntent = Intent(context, SysequalActivity::class.java)
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(dialogIntent)
        }

        try {
            //            context.sendBroadcast(localIntent);
        } catch (e: RuntimeException) {
            Log.w(TAG, "No system equalizer found")
            Log.w(TAG, e)
        }

    }
}