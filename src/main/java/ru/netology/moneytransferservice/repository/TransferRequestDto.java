package ru.netology.moneytransferservice.repository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record TransferRequestDto(

        @NotNull
        @Valid
        Card cardFrom,

        @NotNull
        @NotBlank
        @Size(min = 18, max = 18)
        String cardToNumber,

        @NotNull
        @Valid
        Amount amount
) {
}
