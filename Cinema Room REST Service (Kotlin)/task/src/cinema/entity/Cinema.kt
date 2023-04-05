package cinema.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonProperty

const val SEAT_ROWS = 9
const val SEAT_COLS = 9

data class Cinema(
    @JsonProperty("total_rows") val numRows: Int = SEAT_ROWS,

    @JsonProperty("total_columns") val numCol: Int = SEAT_COLS,

    @JsonIgnore val seats: Array<Array<Seat?>> = Array(SEAT_ROWS) { arrayOfNulls(SEAT_COLS) },
    @JsonIgnore val listTicket: MutableList<Ticket> = mutableListOf()
) {
    init {
        for (row in 0 until SEAT_ROWS) {
            for (col in 0 until SEAT_COLS) {
                seats[row][col] = Seat(row + 1, col + 1, price = if (row <= 3) 10 else 8)
            }
        }
    }

    @JsonProperty("available_seats")
    fun getAvailableSeat(): List<Seat?> {
        val rs = mutableListOf<Seat?>()
        for (row in 0 until SEAT_ROWS) {
            for (col in 0 until SEAT_COLS) {
                if (seats[row][col]?.avail == true) {
                    rs.add(seats[row][col])
                }
            }
        }
        return rs
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cinema

        if (numRows != other.numRows) return false
        if (numCol != other.numCol) return false
        return seats.contentDeepEquals(other.seats)
    }

    override fun hashCode(): Int {
        var result = numRows
        result = 31 * result + numCol
        result = 31 * result + seats.contentDeepHashCode()
        return result
    }

    fun statistic(): Map<String, Int> {
        val rs = mapOf<String, Int>()
        var currentIncome = 0
        var numberOfPurchasedTickets = 0
        listTicket.forEach {
            if (it.state == "Sold") {
                currentIncome += it.seat?.price ?: 0

                numberOfPurchasedTickets += 1
            }
        }
        return mapOf(
            "current_income" to currentIncome,
            "number_of_available_seats" to SEAT_ROWS * SEAT_COLS - numberOfPurchasedTickets,
            "number_of_purchased_tickets" to numberOfPurchasedTickets
        )

    }

}