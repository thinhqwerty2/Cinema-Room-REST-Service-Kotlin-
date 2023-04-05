package cinema.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import java.util.UUID


data class Seat(
    val row: Int, val column: Int,
    @JsonIgnore
    var avail: Boolean = true,
    val price:Int,
) {


}