package kibar.readingisgood.book.data.payload;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateBookStockRequest {

    @NotNull
    private String id;

    @PositiveOrZero
    private Long amount;

}
