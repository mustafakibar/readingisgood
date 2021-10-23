package kibar.readingisgood.order.data.payload;

import org.springframework.data.domain.PageRequest;

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
public class ListOrderByCustomerIdRequest {

    @NotNull
    @NotBlank
    private String id;

    @NotNull
    @NotBlank
    private PageRequest pageRequest;

}
