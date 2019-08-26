package ps.sipnas.polbangtan.data.room

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 **********************************************
 * Created by ukie on 2/4/19 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2019 | All Right Reserved
 */

@Database(entities = [DataLocation::class], version = 1, exportSchema = false)
abstract class AppDB : RoomDatabase() {

    abstract fun dataLocation(): DataLocationDao


}