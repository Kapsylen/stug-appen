package stugapi.presentation.dto;

public record FakturaEnhetDto(
    String id,
    String description,
    String quantity,
    String price,
    String total
) {}
