package kibar.readingisgood.order.data.model;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collation = "orders")
public class Order {

    @Id
    private String id;

    private String bookId;

    private String customerId;

    private Long amount;

    @CreatedDate
    private Date createdAt;

    private OrderStatus status;

}
