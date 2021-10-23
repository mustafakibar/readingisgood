package kibar.readingisgood.order.data.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateOrderRequest {

    @NotNull
    @NotBlank
    private String customerId;

    @NotNull
    @NotBlank
    private String bookId;

    @Positive
    private Long amount;

}
