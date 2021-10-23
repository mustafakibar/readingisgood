package kibar.readingisgood.customer.data.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCustomerRequest {

    @NotNull
    @NotBlank
    private String name;

    @Email(message = "Girdiğiniz e-posta adresi yanlış. Kontrol edip tekrar deneyiniz...")
    @NotNull
    private String mail;

    @NotBlank
    private String password;

}
