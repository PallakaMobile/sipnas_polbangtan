package ps.sipnas.polbangtan.ui.home.upload

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.MultipartBody
import ps.sipnas.polbangtan.base.BaseViewModel
import ps.sipnas.polbangtan.data.model.DataUploadListSPJ
import ps.sipnas.polbangtan.data.rest.SipnasRepository
import ps.sipnas.polbangtan.utils.ProgressRequestBody
import java.io.File

/**
 **********************************************
 * Created by ukie on 11/2/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right Reserved
 */
class UploadViewModel(private val sipnasRepository: SipnasRepository, private val context: Context) : BaseViewModel(), ProgressRequestBody.UploadCallbacks {
    val progress = MutableLiveData<String>()

    fun getListUploadSPJ(headers: LinkedHashMap<String, String>): LiveData<DataUploadListSPJ> {
        val uploadListSPJ = MutableLiveData<DataUploadListSPJ>()
        composite {
            sipnasRepository.listUploadSPJ(headers)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        uploadListSPJ.postValue(it.body() ?: throw NullPointerException())
                    }, {
                        it.printStackTrace()
                    })
        }
        return uploadListSPJ
    }

    fun postUpload(headers: LinkedHashMap<String, String>, image: File): LiveData<String> {

        val progressRequestBody = ProgressRequestBody(image, "*", this, context)
        val body = MultipartBody.Part.createFormData("image", image.name, progressRequestBody)
        composite {
            sipnasRepository.uploadSPJKegiatan(headers, body)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeOn(Schedulers.io())
                    .subscribe({
                        onFinish()
                    }, {
                        it.printStackTrace()
                    })
        }
        return progress
    }

    override fun onProgressUpdate(percentage: Int) {
//        Logger.d(" progress $percentage")
        progress.value = "Uploading $percentage%"
    }

    override fun onError() {
    }

    override fun onFinish() {
        progress.value = "Upload"
    }
}