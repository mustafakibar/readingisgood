package kibar.readingisgood.statistics.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomerOrderSummary {

    private Long totalOrderCount;
    
    private Long totalBookCount;

}
