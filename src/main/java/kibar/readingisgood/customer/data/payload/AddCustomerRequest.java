package kibar.readingisgood.customer.data.payload;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddCustomerRequest {

    @NotBlank
    private String name;

    @Email
    @NotBlank
    private String mail;

    @NotBlank
    private String password;

}
