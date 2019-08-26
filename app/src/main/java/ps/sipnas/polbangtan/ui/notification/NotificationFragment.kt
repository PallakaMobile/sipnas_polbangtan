package ps.sipnas.polbangtan.ui.notification

import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.base.BaseFragment
import ps.sipnas.polbangtan.databinding.FragmentNotificationBinding
import ps.sipnas.polbangtan.utils.Hai
import ps.sipnas.polbangtan.utils.PrefManager

/**
 **********************************************
 * Created by ukie on 10/21/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class NotificationFragment : BaseFragment<FragmentNotificationBinding>() {
    private val viewModel by viewModel<NotificationViewModel>()

    override fun getLayoutResource(): Int = R.layout.fragment_notification

    override fun myCodeHere() {
        dataBinding.lifecycleOwner = this
        dataBinding.rvNotification.setHasFixedSize(true)
        dataBinding.rvNotification.layoutManager = LinearLayoutManager(activity)

        dataBinding.viewModel = viewModel
        val prefManager = PrefManager(activity)
        viewModel.getNotification(linkedMapOf(Hai.auth to prefManager.getAuthToken()))

    }

}