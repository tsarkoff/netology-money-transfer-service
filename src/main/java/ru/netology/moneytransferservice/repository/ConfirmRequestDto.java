package ru.netology.moneytransferservice.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConfirmRequestDto(
        @NotNull
        @NotBlank
        String operationId,

        @NotNull
        @NotBlank
        String code
) {
}
