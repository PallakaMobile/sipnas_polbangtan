package ps.sipnas.polbangtan.ui.home.process

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.drawable.BitmapDrawable
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.base.BaseFragment
import ps.sipnas.polbangtan.databinding.FragmentProcessBinding
import ps.sipnas.polbangtan.ui.home.detail.DetailSPDActivity
import ps.sipnas.polbangtan.ui.home.upload.UploadActivity
import ps.sipnas.polbangtan.utils.Hai
import ps.sipnas.polbangtan.utils.PrefManager


class ProcessFragment : BaseFragment<FragmentProcessBinding>() {
    private val viewModel by viewModel<ProcessViewModel>()
    override fun getLayoutResource(): Int = R.layout.fragment_process

    override fun myCodeHere() {
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel

        val prefManager = PrefManager(activity)
        viewModel.getProcess(linkedMapOf(Hai.auth to prefManager.getAuthToken()))
                .observe(this, Observer { id ->
                    //upload photos
                    dataBinding.ivUpload.setOnClickListener {
                        val uploadActivity = Intent(activity.applicationContext, UploadActivity::class.java)
                        uploadActivity.putExtra("id", id)
                        uploadActivity.putExtra("done", false)
                        startActivity(uploadActivity)
                    }
                    //detail process
                    dataBinding.cvHeader.setOnClickListener {
                        val detailSPD = Intent(activity.applicationContext, DetailSPDActivity::class.java)
                        detailSPD.putExtra("id", id)
                        startActivity(detailSPD)
                    }
                })

        dataBinding.rvDetailProcess.layoutManager = LinearLayoutManager(activity)

        var isExpand = true
        rotateImage(0F)


        //set visibility detail rincian
        dataBinding.tvRincianBiayaTitle.setOnClickListener {
            if (isExpand) {
                rotateImage(180F)
                dataBinding.llRincian.visibility = View.GONE
                isExpand = false
            } else {
                rotateImage(0F)
                dataBinding.llRincian.visibility = View.VISIBLE
                isExpand = true
            }
        }
    }

    private fun rotateImage(rotation: Float) {
        val icon = BitmapFactory.decodeResource(activity.resources, R.drawable.ic_expand)
        val matrix = Matrix()
        matrix.postRotate(rotation)
        val targetBitmap = Bitmap.createBitmap(icon, 0, 0, icon.width, icon.height, matrix, true)

        dataBinding.tvRincianBiayaTitle.setCompoundDrawablesWithIntrinsicBounds(BitmapDrawable(resources, targetBitmap), null, null, null)
    }
}
