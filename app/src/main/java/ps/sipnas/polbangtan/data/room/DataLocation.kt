package ps.sipnas.polbangtan.data.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 **********************************************
 * Created by ukie on 2/4/19 with ♥
 * (>’_’)> email : ukie.tux@gmail.com
 * github : https://www.github.com/tuxkids <(’_’<)
 **********************************************
 * © 2019 | All Right Reserved
 */

@Entity(tableName = "location_table")
data class DataLocation(
        var latitude: Double,
        var longitude: Double,
        @PrimaryKey @ColumnInfo(name = "date") var dateTime: String
)