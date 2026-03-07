public enum TicketStatus {
    ISSUED, IN_USE, PAID, VALIDATED, CANCELED, REFUNDED
}
/*
 * ISSUED: The ticket has been generated and handed to the customer at the entry
 * point.
 * IN_USE: The vehicle associated with the ticket is currently parked within the
 * lot.
 * PAID: The parking fees have been successfully processed for the ticket.
 * VALIDATED: The ticket has been marked as valid for exit, often through a
 * discount or special authorization.
 * CANCELED: The ticket has been voided and is no longer a valid record for
 * parking.
 * REFUNDED: The payment associated with the ticket has been returned to the
 * customer.
 */
