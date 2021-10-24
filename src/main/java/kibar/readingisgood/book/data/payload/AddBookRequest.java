package kibar.readingisgood.book.data.payload;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank
    private String name;

    @PositiveOrZero
    @NotNull
    private Long stock;

}
