package ps.sipnas.polbangtan.ui

import android.content.Intent
import android.util.Log
import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.base.BaseActivity
import ps.sipnas.polbangtan.databinding.ActivityMainBinding
import ps.sipnas.polbangtan.ui.about.AboutFragment
import ps.sipnas.polbangtan.ui.home.HomeFragment
import ps.sipnas.polbangtan.ui.login.LoginActivity
import ps.sipnas.polbangtan.ui.notification.NotificationFragment
import ps.sipnas.polbangtan.ui.profile.ProfileFragment
import ps.sipnas.polbangtan.utils.Hai
import ps.sipnas.polbangtan.utils.PrefManager
import ps.sipnas.polbangtan.utils.ViewPagerAdapter


class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getToolbarResource(): Int = 0
    override fun getLayoutResource(): Int = R.layout.activity_main
    private val tabIcons = intArrayOf(R.drawable.ic_home, R.drawable.ic_notification, R.drawable.ic_profile, R.drawable.ic_about)

    override fun myCodeHere() {
//        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener {
        //            Logger.d("Fcm ${it.token}")
//        }

        val prefManager = PrefManager(this)
        //go to login
        if (!prefManager.getUserLogin()) {
            startActivity(Intent(applicationContext, LoginActivity::class.java))
            finish()
            return
        }

        //setup view pager
        val viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        viewPagerAdapter.addFragment(HomeFragment())
        viewPagerAdapter.addFragment(NotificationFragment())
        viewPagerAdapter.addFragment(ProfileFragment())
        viewPagerAdapter.addFragment(AboutFragment())
        dataBinding.container.adapter = viewPagerAdapter

        //setup tablayout
        dataBinding.tabs.setupWithViewPager(dataBinding.container)
        for (i in 0..3) {
            dataBinding.tabs.getTabAt(i)?.setIcon(tabIcons[i])
        }

        //run job when app was killed
        /*  val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
          for (item in jobScheduler.allPendingJobs) {
              Log.e("SIPNAS", item.id.toString())
              if (item.id != 0)
                  Hai.startJob(applicationContext)
          }*/


        /*   val intent = Intent(this, LocationWorker::class.java)
           val isWorking = PendingIntent.getBroadcast(this@MainActivity, 0, intent, 0) != null
           Log.e("SIPNAS", "Is working ? $isWorking")
           if (!isWorking)
               Hai.startAlarm(this)*/

        Log.e("SIPNAS", prefManager.getString(Hai.locationWork))
        if (prefManager.getString(Hai.locationWork) == "") {
            Hai.startWorker(this)
        }
    }


}
