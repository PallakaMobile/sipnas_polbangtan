package ps.sipnas.polbangtan.di

import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.ext.koin.viewModel
import org.koin.dsl.module.module
import ps.sipnas.polbangtan.data.rest.SipnasRepository
import ps.sipnas.polbangtan.data.rest.SipnasRepositoryImpl
import ps.sipnas.polbangtan.ui.home.detail.DetailSPDViewModel
import ps.sipnas.polbangtan.ui.home.done.DoneViewModel
import ps.sipnas.polbangtan.ui.home.process.ProcessViewModel
import ps.sipnas.polbangtan.ui.home.statistic.StatisticViewModel
import ps.sipnas.polbangtan.ui.home.upload.UploadViewModel
import ps.sipnas.polbangtan.ui.home.upload.kegiatan.KegiatanViewModel
import ps.sipnas.polbangtan.ui.home.upload.kegiatan.detail.DetailKegiatanViewModel
import ps.sipnas.polbangtan.ui.home.upload.spj.SpjViewModel
import ps.sipnas.polbangtan.ui.home.upload.spj.detail.DetailSpjViewModel
import ps.sipnas.polbangtan.ui.login.LoginViewModel
import ps.sipnas.polbangtan.ui.notification.NotificationViewModel
import ps.sipnas.polbangtan.ui.profile.ProfileViewModel
import ps.sipnas.polbangtan.ui.profile.edit.EditProfileViewModel
import ps.sipnas.polbangtan.utils.RxEditTextBinding

/**
 **********************************************
 * Created by ukie on 10/4/18 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2018 | All Right `
 */

/**
 *  ViewModel inject
 */
val sipnasViewModel = module {
    // TODO define view model to inject
    viewModel { LoginViewModel(get()) }
    viewModel { ProcessViewModel(get()) }
    viewModel { SpjViewModel(get()) }
    viewModel { KegiatanViewModel(get()) }
    viewModel { DoneViewModel(get()) }
    viewModel { NotificationViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { UploadViewModel(get(), androidContext()) }
    viewModel { DetailSPDViewModel(get()) }
    viewModel { DetailSpjViewModel(get()) }
    viewModel { DetailKegiatanViewModel(get()) }
    viewModel { StatisticViewModel(get()) }
    viewModel { EditProfileViewModel(get()) }
}

val globalModule = module {
    single { RxEditTextBinding() }
}

val dataModule = module(createOnStart = true) {
    //TODO inject Repository when implement on RepositoryImpl
    single<SipnasRepository> { SipnasRepositoryImpl(get()) }
}


val sipnasApp = listOf(sipnasViewModel, dataModule, networkModule, globalModule, roomModule)