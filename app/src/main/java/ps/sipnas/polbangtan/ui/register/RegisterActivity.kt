package ps.sipnas.polbangtan.ui.register

import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.base.BaseActivity
import ps.sipnas.polbangtan.databinding.ActivityRegisterBinding

class RegisterActivity : BaseActivity<ActivityRegisterBinding>() {

    override fun getToolbarResource(): Int = R.id.toolbar

    override fun getLayoutResource(): Int = R.layout.activity_register

    override fun myCodeHere() {
        title = getString(R.string.user_information)
    }
}
