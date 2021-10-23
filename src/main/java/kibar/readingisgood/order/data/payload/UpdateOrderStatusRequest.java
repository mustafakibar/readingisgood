package kibar.readingisgood.order.data.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import kibar.readingisgood.order.data.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequest {

    @NotNull
    @NotBlank
    private String id;

    @NotNull
    @NotBlank
    private OrderStatus orderStatus;

}
