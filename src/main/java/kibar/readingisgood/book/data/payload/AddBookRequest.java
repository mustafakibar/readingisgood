package kibar.readingisgood.book.data.payload;

import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddBookRequest {

    @Size(min = 3, max = 40, message = "Kitap ismi en az 3, en fazla 127 harften oluşmalıdır")
    private String name;

    @PositiveOrZero
    private Long stock;

}
