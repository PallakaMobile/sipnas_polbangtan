package ps.sipnas.polbangtan.ui.home.upload.kegiatan.detail

import android.Manifest
import android.app.Dialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.tbruyelle.rxpermissions2.RxPermissions
import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.base.BaseActivity
import ps.sipnas.polbangtan.databinding.ActivityKegiatanDetailBinding
import ps.sipnas.polbangtan.databinding.LayerDialogInfoKegiatanBinding
import ps.sipnas.polbangtan.utils.GlideApp
import ps.sipnas.polbangtan.utils.Hai
import ps.sipnas.polbangtan.utils.PrefManager
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class DetailKegiatanActivity : BaseActivity<ActivityKegiatanDetailBinding>() {
    private val viewModel by viewModel<DetailKegiatanViewModel>()
    override fun getToolbarResource(): Int = R.id.toolbar

    override fun getLayoutResource(): Int = R.layout.activity_kegiatan_detail

    @Suppress("DEPRECATION")
    override fun myCodeHere() {
        title = intent.extras.getString("ket")

        if (intent.extras.getBoolean("done"))
            dataBinding.ivDeleteKegiatan.visibility = View.GONE

        val prefManager = PrefManager(this)

        when (intent.extras.getString("type")) {
            "jpg", "jpeg", "png" -> { //TODO no preview on this type file
                GlideApp.with(this)
                        .asBitmap()
                        .fitCenter()
                        .load(intent.extras.getString("image"))
                        .into(object : SimpleTarget<Bitmap>() {
                            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                                val resized = Bitmap.createScaledBitmap(resource, (resource.width * 0.8).toInt(), (resource.height * 0.8).toInt(), true)
                                dataBinding.ivKegiatan.setImageBitmap(resized)
                                dataBinding.ivKegiatan.fitImageToView()
                                dataBinding.ivKegiatan.setZoom(1F)
                                dataBinding.ivShareKegiatan.setOnClickListener {
                                    val i = Intent(Intent.ACTION_SEND)
                                    i.type = "image/*"
                                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    i.putExtra(Intent.EXTRA_STREAM, getLocalBitmapUri(resource))
                                    startActivity(Intent.createChooser(i, "Share Image"))
                                }
                            }
                        })
                dataBinding.tvType.visibility = View.GONE
                dataBinding.ivKegiatan.setOnTouchImageViewListener {
                    if (dataBinding.ivKegiatan.isZoomed) {
                        dataBinding.llFooter.visibility = View.GONE
                    } else {
                        dataBinding.llFooter.visibility = View.VISIBLE
                    }
                }
            }
            else -> {
                GlideApp.with(this)
                        .load(R.drawable.ic_file)
                        .override(100, 100)
                        .placeholder(Hai.progressDrawable(this))
                        .into(dataBinding.ivKegiatan)
                dataBinding.ivKegiatan.layoutParams.height = 500
                dataBinding.ivKegiatan.layoutParams.width = 500
                dataBinding.tvType.visibility = View.VISIBLE
                dataBinding.tvType.text = intent.extras.getString("type")

                dataBinding.ivShareKegiatan.setOnClickListener {
                    val share = Intent()
                    share.action = Intent.ACTION_SEND
                    share.type = "text/plain"
                    share.putExtra(Intent.EXTRA_TEXT, intent.extras.getString("image"))
                    startActivity(Intent.createChooser(share, "Share"))
                }
            }
        }

        dataBinding.ivInfoKegiatan.setOnClickListener {
            val dialogBinding: LayerDialogInfoKegiatanBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.layer_dialog_info_kegiatan, null, false)
            dialogBinding.lifecycleOwner = this
            dialogBinding.tvSummaryDesc.text = intent.extras.getString("ket")
            dialogBinding.tvSummaryDateSend.text = intent.extras.getString("tgl")
            dialogBinding.tvSummaryTimeSend.text = intent.extras.getString("waktu")

            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val lp = WindowManager.LayoutParams()
            lp.copyFrom(dialog.window?.attributes)
            lp.width = WindowManager.LayoutParams.MATCH_PARENT
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            dialog.setContentView(dialogBinding.root)
            dialog.setCancelable(false)
            dialog.show()
            dialog.window?.attributes = lp

            dialogBinding.btnClose.setOnClickListener {
                dialog.dismiss()
            }
        }

        dataBinding.ivDownloadKegiatan.setOnClickListener {
            RxPermissions(this)
                    .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe({ permission ->
                        if (permission) {
                            Toast.makeText(this@DetailKegiatanActivity, "Downloading", Toast.LENGTH_SHORT).show()
                            val downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                            val downloadUri = Uri.parse(intent.extras.getString("image"))
                            val request = DownloadManager.Request(downloadUri)
                            request.setDescription("Downloading a file")
                            downloadManager.enqueue(request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                                    .setAllowedOverRoaming(false)
                                    .setTitle("File Downloading...")
                                    .setDescription(downloadUri.lastPathSegment)
                                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, downloadUri.lastPathSegment))
                        }
                    }
                            , {
                        it.printStackTrace()
                    })


            val onComplete = object : BroadcastReceiver() {
                override fun onReceive(ctxt: Context, intent: Intent) {
                    Toast.makeText(this@DetailKegiatanActivity, "Success", Toast.LENGTH_SHORT).show()
                    unregisterReceiver(this)
                }
            }

            registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
        }

        dataBinding.ivDeleteKegiatan.setOnClickListener {
            viewModel.deleteImage(linkedMapOf(
                    Hai.auth to prefManager.getAuthToken()), intent.extras.getString("id"), this
            )
        }

    }

    fun getLocalBitmapUri(bmp: Bitmap): Uri? {
        var bmpUri: Uri? = null
        try {
            val fileStorage = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            val nameFile = File.createTempFile(
                    "share", /* prefix */
                    ".jpg", /* suffix */
                    fileStorage/* directory */
            )
            val out = FileOutputStream(nameFile)
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out)
            out.close()
            bmpUri = FileProvider.getUriForFile(this, applicationContext.packageName + ".provider", nameFile)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return bmpUri
    }
}
