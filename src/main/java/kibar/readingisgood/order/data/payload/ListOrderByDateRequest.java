package kibar.readingisgood.order.data.payload;

import java.util.Date;

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
public class ListOrderByDateRequest {

    @NotNull
    @NotBlank
    private Date from;

    @NotNull
    @NotBlank
    private Date to;

}
