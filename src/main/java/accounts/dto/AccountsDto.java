package accounts.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AccountsDto {
    @NotNull(message = "Account number cannot be null")
    @Digits(integer = 10, fraction = 0, message = "Account number must be 10 digits")
    private Long accountNumber;
    @NotEmpty(message = "Account type cannot be null or empty")
    private String accountType;
    @NotEmpty(message = "branchAddress cannot be null or empty")
    private String branchAddress;
}
