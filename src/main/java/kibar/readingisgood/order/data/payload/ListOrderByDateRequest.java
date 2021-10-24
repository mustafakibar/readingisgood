package kibar.readingisgood.order.data.payload;

import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

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
    private LocalDateTime from;

    @NotNull
    @NotBlank
    private LocalDateTime to;

    @NotNull
    @NotBlank
    private PageRequest pageRequest;

}
