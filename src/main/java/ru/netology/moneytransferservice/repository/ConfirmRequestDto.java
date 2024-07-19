package ru.netology.moneytransferservice.repository;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConfirmRequestDto(
        @Min(-1)
        @Max(Integer.MAX_VALUE)
        int operationId,

        @NotNull
        @NotBlank
        String code
) {
}
