package ps.sipnas.polbangtan.ui.home.detail

import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.base.BaseActivity
import ps.sipnas.polbangtan.data.model.DataProfile
import ps.sipnas.polbangtan.databinding.ActivityDetailSpdBinding
import ps.sipnas.polbangtan.utils.Hai
import ps.sipnas.polbangtan.utils.PrefManager

class DetailSPDActivity : BaseActivity<ActivityDetailSpdBinding>() {
    private val viewModel by viewModel<DetailSPDViewModel>()

    override fun getToolbarResource(): Int = R.id.toolbar

    override fun getLayoutResource(): Int = R.layout.activity_detail_spd

    override fun myCodeHere() {
        title = getString(R.string.detail)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel
        val prefManager = PrefManager(this)
        viewModel.getDetailSPD(
                linkedMapOf(
                        Hai.auth to prefManager.getAuthToken(),
                        "idSpd" to intent.extras.getString("id")
                )
        )
    }
}
