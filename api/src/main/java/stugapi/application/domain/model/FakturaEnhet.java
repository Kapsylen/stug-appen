package stugapi.application.domain.model;

public record FakturaEnhet(
    String id,
    String description,
    String quantity,
    String price,
    String total
) {}
