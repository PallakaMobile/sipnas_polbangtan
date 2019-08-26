package ps.sipnas.polbangtan.services

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import org.koin.standalone.KoinComponent
import org.koin.standalone.inject
import ps.sipnas.polbangtan.data.rest.SipnasRepository
import ps.sipnas.polbangtan.data.room.DataLocation
import ps.sipnas.polbangtan.data.room.DataLocationDao
import ps.sipnas.polbangtan.utils.Hai
import ps.sipnas.polbangtan.utils.PrefManager
import java.text.SimpleDateFormat
import java.util.*


/**
 **********************************************
 * Created by ukie on 2/2/19 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2019 | All Right Reserved
 */
class LocationWorker(appContext: Context, workerParameters: WorkerParameters) : Worker(appContext, workerParameters), KoinComponent {

    private val sipnasRepository: SipnasRepository by inject()
    private val appDB: DataLocationDao by inject()

    lateinit var composite: CompositeDisposable

    override fun doWork(): Result {
        //inject in broadcast receiver
        getCurrentLocation(applicationContext)
        return Result.success()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation(context: Context) {
        composite = CompositeDisposable()
        val prefManager = PrefManager(context)

        LocationServices.getFusedLocationProviderClient(context)
                .lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                Log.e("SIPNAS", "latitude ${location.latitude}")
                Log.e("SIPNAS", "longitude ${location.longitude}")

                Log.e("SIPNAS", isConnected(context).toString())

                // if connected, check data in database if empty to do send2server,
                // else fetch data from db then send2server
                if (isConnected(context)) {
                    composite.add(Observable.just(appDB)
                            .subscribeOn(Schedulers.io())
                            .subscribe { dataLocationDao ->
                                Log.e("SIPNAS", dataLocationDao.getAllLocation().toString())

                                //upload data to server if data in
                                if (dataLocationDao.getAllLocation().isEmpty()) {
                                    send2Server(context, linkedMapOf(
                                            Hai.auth to prefManager.getAuthToken(),
                                            "lat" to location.latitude.toString(),
                                            "long" to location.longitude.toString(),
                                            "dateTime" to "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}"
                                    ), false)
                                } else {
                                    //fetch data from db
                                    for (item in dataLocationDao.getAllLocation()) {
                                        send2Server(context, linkedMapOf(
                                                Hai.auth to prefManager.getAuthToken(),
                                                "lat" to item.latitude.toString(),
                                                "long" to item.longitude.toString(),
                                                "dateTime" to item.dateTime),
                                                true)
                                    }
                                    //after load data from db ^, then upload current location
                                    Log.e("SIPNAS", "Upload current location")
                                    send2Server(context, linkedMapOf(
                                            Hai.auth to prefManager.getAuthToken(),
                                            "lat" to location.latitude.toString(),
                                            "long" to location.longitude.toString(),
                                            "dateTime" to "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}"
                                    ), false)
                                }
                            })

                } else {
                    //insert data to db if connection not available
                    Log.e("SIPNAS", "Insert to db")
                    composite.add(
                            Observable.just(appDB)
                                    .subscribeOn(Schedulers.io())
                                    .subscribe {
                                        Log.e("SIPNAS", it.getAllLocation().toString())
                                        it.insert(DataLocation(
                                                latitude = location.latitude,
                                                longitude = location.longitude,
                                                dateTime = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}"))

//                                        sendNotification(context, "Insert2Db-1 ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}",
//                                                "Lat : ${location.latitude} Long :  ${location.longitude} ")
                                    }
                    )
                }
            }
        }
    }

    private fun send2Server(context: Context, header: LinkedHashMap<String, String>, db: Boolean) {
        composite.add(Observable.just(appDB)
                .subscribeOn(Schedulers.io())
                .subscribe { dataLocationDao ->
                    composite.add(sipnasRepository.sendLocation(header)
                            .subscribeOn(Schedulers.io())
                            .subscribe({ response ->
                                if (response.code() == 200) {
                                    if (db) {
                                        dataLocationDao.deleteUpdated(header["dateTime"] ?: "")
//                                        sendNotification(context, "Send2Server-1  ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}",
//                                                "Lat : ${header["lat"]} Long :  ${header["long"]} ")
                                    }
//                                        sendNotification(context, "Send2Server-2  ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}",
//                                                "Lat : ${header["lat"]} Long :  ${header["long"]} ")
                                } else {
                                    if (!db) {
                                        dataLocationDao.insert(DataLocation(
                                                latitude = header["lat"]?.toDouble() ?: 0.0,
                                                longitude = header["long"]?.toDouble() ?: 0.0,
                                                dateTime = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}"))
//                                        sendNotification(context, "Insert2Db-2 ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}",
//                                                "Lat : ${header["lat"]} Long :  ${header["long"]} ")
                                    }

                                }
                            }, {
                                if (!db) {
                                    dataLocationDao.insert(DataLocation(
                                            latitude = header["lat"]?.toDouble() ?: 0.0,
                                            longitude = header["long"]?.toDouble() ?: 0.0,
                                            dateTime = "${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}"))
//                                    sendNotification(context, "Insert2Db-3 ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date())}",
//                                            "Lat : ${header["lat"]} Long :  ${header["long"]} ")
                                }
                                it.printStackTrace()
                            })
                    )
                })

    }

    private fun isConnected(context: Context): Boolean {
        val cm = context
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork != null && activeNetwork.isConnected
    }


    /* private fun sendNotification(context: Context, title: String, message: String) {
         try {

             val notifIntent: Intent?
             notifIntent = Intent(context, MainActivity::class.java)
             notifIntent.putExtra("notifIntent", true)
             notifIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)


             val notificationManager: NotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
             val notifID = (Date().time / 1000L % Integer.MAX_VALUE).toInt()
             val notificationBuilder: NotificationCompat.Builder

             val inboxStyle = NotificationCompat.InboxStyle()

             @TargetApi(Build.VERSION_CODES.O)
             if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                 *//* Create or update. *//*
                val mChannel = NotificationChannel(context.getString(R.string.app_name),
                        title,
                        NotificationManager.IMPORTANCE_HIGH)

                mChannel.enableLights(true)
                mChannel.lightColor = Color.GREEN
                mChannel.setShowBadge(true)
                mChannel.lockscreenVisibility = Notification.VISIBILITY_PUBLIC
                notificationManager.createNotificationChannel(mChannel)

                val pendingIntent = PendingIntent.getActivity(context, 0, notifIntent, PendingIntent.FLAG_ONE_SHOT)
                inboxStyle.addLine(message)

                notificationBuilder = NotificationCompat.Builder(context, context.getString(R.string.app_name))
                        .setContentTitle(title)
                        .setContentText(message)
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_notif, 1)
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                        .setChannelId(context.getString(R.string.app_name))
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setAutoCancel(true)
                        .setTicker(title)
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setWhen(System.currentTimeMillis())

            } else {
                val pendingIntent = PendingIntent.getActivity(context, 0, notifIntent, PendingIntent.FLAG_ONE_SHOT)
                inboxStyle.addLine(message)

                @Suppress("DEPRECATION")
                notificationBuilder = NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_notif)
                        .setTicker(title)
                        .setAutoCancel(true)
                        .setContentTitle(title)
                        .setContentIntent(pendingIntent)
                        .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                        .setDefaults(Notification.DEFAULT_ALL)
                        .setStyle(inboxStyle)
                        .setWhen(System.currentTimeMillis())
                        .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.mipmap.ic_launcher))
                        .setContentText(message)

            }
            notificationManager.notify(notifID, notificationBuilder.build())

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }*/

}