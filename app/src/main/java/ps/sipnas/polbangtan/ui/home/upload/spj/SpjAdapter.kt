package ps.sipnas.polbangtan.ui.home.upload.spj

import android.app.Activity
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.data.model.DataImageSPJ
import ps.sipnas.polbangtan.databinding.FragmentGallerySpjItemBinding
import ps.sipnas.polbangtan.ui.home.upload.kegiatan.SpjFragment
import ps.sipnas.polbangtan.ui.home.upload.spj.detail.DetailSpjActivity
import ps.sipnas.polbangtan.utils.GlideApp
import ps.sipnas.polbangtan.utils.Hai


/**
 **********************************************
 * Created by ukie on 10/15/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class SpjAdapter : RecyclerView.Adapter<SpjAdapter.GridHolder>() {
    private lateinit var listDataSPJ: List<DataImageSPJ.Data>
    private var isDone = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GridHolder =
            GridHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.fragment_gallery_spj_item, parent, false))

    override fun onBindViewHolder(holder: GridHolder, position: Int) = holder.bind(listDataSPJ[position], isDone)

    override fun getItemCount() = if (::listDataSPJ.isInitialized) listDataSPJ.size else 0

    fun updateDataSPJ(listDataSPJ: List<DataImageSPJ.Data>, isDone: Boolean) {
//        Logger.d("update data ${listDataSPJ.size}")
        this.listDataSPJ = listDataSPJ
        this.isDone = isDone
        notifyDataSetChanged()
    }

    inner class GridHolder(private val binding: FragmentGallerySpjItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataImageSPJ.Data, isDone: Boolean) = with(itemView) {

            when (data.type) {
                "jpg", "jpeg", "png" -> {
                    GlideApp.with(this)
                            .load(data.imageUrl)
                            .placeholder(Hai.progressDrawable(this.context))
                            .error(R.drawable.ic_file)
                            .centerCrop()
                            .into(binding.ivSpj)
                    binding.ivSpj.setPadding(3, 3, 3, 3)
                    binding.tvType.visibility = View.GONE
                }
                else -> {
                    GlideApp.with(this)
                            .load(R.drawable.ic_file)
                            .placeholder(Hai.progressDrawable(this.context))
                            .fitCenter()
                            .into(binding.ivSpj)
                    binding.ivSpj.setPadding(150, 150, 150, 150)
                    binding.tvType.visibility = View.VISIBLE
                    binding.tvType.text = data.type
                }
            }


            // TODO: Bind data dengan View
            setOnClickListener {
                SpjFragment.refresh = false
                val detail = Intent(it.context.applicationContext, DetailSpjActivity::class.java)
                detail.putExtra("image", data.imageUrl)
                detail.putExtra("ket", data.ket)
                detail.putExtra("id", data.id)
                detail.putExtra("type", data.type)
                detail.putExtra("tgl", data.tglTrm)
                detail.putExtra("waktu", data.waktuTrm)
                detail.putExtra("rincian", data.rincian)
                detail.putExtra("done", isDone)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    val options = ActivityOptionsCompat.makeSceneTransitionAnimation(it.context as Activity, binding.ivSpj, "spj")
                    (it.context as Activity).startActivity(detail, options.toBundle())
                } else {
                    (it.context as Activity).startActivity(detail)
                }
            }
        }
    }
}