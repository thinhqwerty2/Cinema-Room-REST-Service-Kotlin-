package cinema.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

class Ticket(
    val token: String = UUID.randomUUID().toString(),
    @JsonProperty("ticket")
    val seat: Seat?,
    @JsonIgnore
    var state:String?="Sold"
) {
    override fun toString(): String {
        return "Ticket(token='$token', seat=$seat)"
    }
}