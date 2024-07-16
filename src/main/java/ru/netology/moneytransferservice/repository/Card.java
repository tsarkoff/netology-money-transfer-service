package ru.netology.moneytransferservice.repository;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Card {
    @NotNull
    @NotBlank
    @Size(min = 18, max = 18)
    private String number;

    @NotNull
    @NotBlank
    @Size(min = 7, max = 7)
    private String validTill;

    @NotNull
    @NotBlank
    @Size(min = 5, max = 5)
    private String CVV;

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(this.getClass()))
            return false;
        String num = ((Card) obj).number.replace("\"", "");
        String valid = ((Card) obj).validTill.replace("\"", "");
        String cvv = ((Card) obj).CVV.replace("\"", "");
        return number.equals(num) && validTill.equals(valid) && CVV.equals(cvv);
    }
}