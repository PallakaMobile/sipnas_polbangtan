package ps.sipnas.polbangtan.ui.about

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.base.BaseFragment
import ps.sipnas.polbangtan.databinding.FragmentAboutBinding
import ps.sipnas.polbangtan.ui.aboutimport.AboutAdapter

/**
 **********************************************
 * Created by ukie on 10/24/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class AboutFragment : BaseFragment<FragmentAboutBinding>() {
    override fun getLayoutResource(): Int = R.layout.fragment_about
    override fun myCodeHere() {
        val aboutAdapter = AboutAdapter()
        dataBinding.rvAbout.layoutManager = LinearLayoutManager(activity)
        dataBinding.rvAbout.setHasFixedSize(true)
        dataBinding.rvAbout.adapter = aboutAdapter
        aboutAdapter.updateAboutAdapter(
                listOf(About(ContextCompat.getDrawable(activity, R.drawable.ic_about_location), "Alamat",
                        "Kampus I\n Jl. Malino No.KM. 7, Romang Lompoa,Bontomarannu, Kabupaten Gowa, Sulawesi Selatan 92171\n\n" +
                                "Kampus II\n Desa Mappesangka & Desa Turu Adae, Kecamatan Ponre, Kabupaten Bone."),
                        About(ContextCompat.getDrawable(activity, R.drawable.ic_about_web), "Website", "polbangtan-gowa.ac.id"),
                        About(ContextCompat.getDrawable(activity, R.drawable.ic_about_mail), "Alamat Email", "info@polbangtan-gowa.ac.id"))
        )
    }

    data class About(
            val logo: Drawable? = null,
            val title: String? = "",
            val desc: String? = ""
    )
}