package stugapi.infrastructure.entities;

/**
 * Represents the status of an invoice in the system.
 */
public enum FakturaStatus {
    /**
     * Invoice has been fully paid
     */
    PAID,
    
    /**
     * Invoice is past its due date and hasn't been paid
     */
    OVERDUE,
    
    /**
     * Invoice has been sent to the client
     */
    SENT,
    
    /**
     * Invoice is in draft state, not yet sent
     */
    DRAFT
}