package cinema.controller


import cinema.entity.*


import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(
    private val cinema: Cinema = Cinema()
) {

    @GetMapping("/seats", produces = ["application/json"])
    fun getSeatInfo(): ResponseEntity<Cinema> {
        return ResponseEntity(cinema, HttpStatus.OK)
    }

    @PostMapping("/purchase", produces = ["application/json"], consumes = ["application/json"])
    fun purchase(@RequestBody seat: Seat): ResponseEntity<Any> {
        println(seat)
        val row = seat.row - 1
        val column = seat.column - 1
        return when {
            row !in 0 until SEAT_ROWS || column !in 0 until SEAT_COLS ->
                ResponseEntity.status(400)
                    .body(mapOf("error" to "The number of a row or a column is out of bounds!"))

            cinema.seats[row][column]?.avail == false ->
                ResponseEntity.status(400)
                    .body(mapOf("error" to "The ticket has been already purchased!"))

            else -> {
                cinema.seats[row][column]?.avail = false
                val ticket=Ticket(seat = cinema.seats[row][column])
                cinema.listTicket.add(ticket)
                ResponseEntity.status(200).body(ticket)
            }

        }
    }

    @PostMapping("/return", consumes = ["application/json"], produces = ["application/json"])
    fun returnTicket(@RequestBody ticket: Ticket): ResponseEntity<Any> {
        println(ticket.toString())
        val rs: Ticket? = cinema.listTicket.find { it.token==ticket.token }
        return if(rs==null){
            ResponseEntity.status(400).body(mapOf("error" to "Wrong token!"))
        }else{
            cinema.seats[rs.seat?.row!!][rs.seat.column]?.avail=true
            rs.state="Refunded"
            ResponseEntity.status(200).body(mapOf("returned_ticket" to rs.seat))
        }


    }

    @PostMapping("/stats", produces = ["application/json"])
    fun stats(@RequestParam("password", required =false) pass:String?):ResponseEntity<Any>{
        return if(pass=="super_secret"){
            ResponseEntity.status(200).body(cinema.statistic())
        } else{
            ResponseEntity.status(401).body(mapOf("error" to "The password is wrong!"))
        }
    }
}