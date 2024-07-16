package ru.netology.moneytransferservice.repository;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Amount {
    @Min(1)
    @Max(100_000_000)
    private int value;

    @NotNull
    private Currency currency;
}
