package ps.sipnas.polbangtan.ui.profile.aktifitas

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import ps.sipnas.polbangtan.R
import ps.sipnas.polbangtan.base.BaseFragment
import ps.sipnas.polbangtan.data.model.DataProfile
import ps.sipnas.polbangtan.databinding.FragmentAktifitasBinding

/**
 **********************************************
 * Created by ukie on 10/24/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class AktifitasFragment : BaseFragment<FragmentAktifitasBinding>() {

    override fun getLayoutResource(): Int = R.layout.fragment_aktifitas

    override fun myCodeHere() {
        dataBinding.rvAktifitas.setHasFixedSize(true)
        dataBinding.rvAktifitas.layoutManager = LinearLayoutManager(activity)

        val listAktifitas: List<DataProfile.AktifitasItem> = arguments?.getParcelableArrayList("aktifitas")
                ?: throw NullPointerException()

        if (listAktifitas.isEmpty()) {
            dataBinding.tvNoData.visibility = View.VISIBLE
            dataBinding.rvAktifitas.visibility = View.GONE
        } else {
            val adapter = AktifitasAdapter()
            dataBinding.rvAktifitas.adapter = adapter
            dataBinding.rvAktifitas.layoutManager = LinearLayoutManager(activity)
            adapter.updateAktifitasList(listAktifitas)
        }


    }

}