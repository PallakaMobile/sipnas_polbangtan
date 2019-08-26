package ps.sipnas.polbangtan.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import ps.sipnas.polbangtan.utils.Hai
import ps.sipnas.polbangtan.utils.PrefManager
import java.util.*

/**
 **********************************************
 * Created by ukie on 2/2/19 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2019 | All Right Reserved
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.e("SIPNAS", "BootReceiver at ${Date()}")
        if (context != null) {
            val prefManger = PrefManager(context)
            val cal = Calendar.getInstance()
            //user service on boot run when only user login and not in saturday or sunday
            if (prefManger.getUserLogin() && ((cal.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) || cal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY)) {
//                Hai.startAlarm(context)
                Hai.startWorker(context)
                Log.e("SIPNAS", "Alarm Start")
            }
        }

    }

}