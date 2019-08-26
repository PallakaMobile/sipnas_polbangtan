package ps.sipnas.polbangtan.ui.home.upload.kegiatan

import androidx.recyclerview.widget.GridLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.base.BaseFragment
import ps.sipnas.polbangtan.databinding.FragmentGalleryKegiatanBinding
import ps.sipnas.polbangtan.utils.Hai
import ps.sipnas.polbangtan.utils.PrefManager

/**
 **********************************************
 * Created by ukie on 10/15/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class KegiatanFragment : BaseFragment<FragmentGalleryKegiatanBinding>() {
    companion object {
        var refresh = false
    }

    private val viewModel by viewModel<KegiatanViewModel>()
    private lateinit var prefManager: PrefManager
    private var id = ""
    private var isDone = false

    override fun getLayoutResource(): Int = R.layout.fragment_gallery_kegiatan

    override fun myCodeHere() {
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel

        val id = arguments?.getString("id") ?: ""
        isDone = arguments?.getBoolean("done") ?: false

        prefManager = PrefManager(activity)
        viewModel.getGallery(linkedMapOf(Hai.auth to prefManager.getAuthToken(), "idSpd" to id), isDone)

        dataBinding.rvGalleryKegiatan.layoutManager = GridLayoutManager(activity, 2)

    }

    override fun onResume() {
        super.onResume()
        if (refresh)
            viewModel.getGallery(linkedMapOf(Hai.auth to prefManager.getAuthToken(), "idSpd" to id), isDone)
    }

}