package ps.sipnas.polbangtan.ui.home.upload.kegiatan.detail

import android.app.Activity
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import ps.sipnas.polbangtan.base.BaseViewModel
import ps.sipnas.polbangtan.data.rest.SipnasRepository
import ps.sipnas.polbangtan.ui.home.upload.kegiatan.KegiatanFragment

/**
 **********************************************
 * Created by ukie on 11/6/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class DetailKegiatanViewModel(private val sipnasRepository: SipnasRepository) : BaseViewModel() {

    fun deleteImage(headers: LinkedHashMap<String, String>, id: String, activity: Activity) {
        composite {
            sipnasRepository.deleteImage(headers, id)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        Toast.makeText(activity, it.body()?.status, Toast.LENGTH_SHORT).show()
                        KegiatanFragment.refresh = true
                        activity.finish()
                    }, {
                        Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
                    })
        }
    }
}