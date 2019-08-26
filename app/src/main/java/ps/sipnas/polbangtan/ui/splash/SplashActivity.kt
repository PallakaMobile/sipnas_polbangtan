package ps.sipnas.polbangtan.ui.splash

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.location.LocationServices
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.base.BaseActivity
import ps.sipnas.polbangtan.databinding.ActivitySplashBinding
import ps.sipnas.polbangtan.ui.MainActivity
import java.util.concurrent.TimeUnit

class SplashActivity : BaseActivity<ActivitySplashBinding>() {
    override fun getToolbarResource(): Int = 0

    override fun getLayoutResource(): Int = R.layout.activity_splash

    @SuppressLint("MissingPermission")
    override fun myCodeHere() {


        Observable.timer(1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.single())
                .subscribe {

                    RxPermissions(this).request(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
                            ?.subscribe { permission ->
                                if (permission) {
                                    LocationServices.getFusedLocationProviderClient(this)
                                            .lastLocation.addOnSuccessListener {
                                        startActivity(Intent(applicationContext, MainActivity::class.java))
                                        finish()
                                    }
                                } else {
                                    val builder = AlertDialog.Builder(this)
                                    builder.setMessage(getString(R.string.access_location_not_allowed))
                                    builder.setPositiveButton(getString(R.string.retry)) { dialog, _ -> dialog.dismiss() }
                                    builder.setCancelable(false)
                                    builder.show()
                                }

                            }

                }
    }

}
