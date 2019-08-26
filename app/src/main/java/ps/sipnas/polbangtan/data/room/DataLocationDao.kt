package ps.sipnas.polbangtan.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

/**
 **********************************************
 * Created by ukie on 2/4/19 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2019 | All Right Reserved
 */

@Dao
interface DataLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(dataLocation: DataLocation)

    @Query("SELECT * FROM location_table ORDER BY date ASC")
    fun getAllLocation(): List<DataLocation>

    @Query("DELETE  FROM location_table WHERE date =:dateTime")
    fun deleteUpdated(dateTime: String)


}