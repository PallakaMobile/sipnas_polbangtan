package ps.sipnas.polbangtan.di

import androidx.room.Room
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module.module
import ps.sipnas.polbangtan.data.room.AppDB

/**
 **********************************************
 * Created by ukie on 2/4/19 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2019 | All Right Reserved
 */

val roomModule = module {
    single {
        Room.databaseBuilder(androidApplication(), AppDB::class.java, "location-db")
                .build()
    }
    // Expose WeatherDAO
    single { get<AppDB>().dataLocation() }
}