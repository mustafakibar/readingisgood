package kibar.readingisgood.customer.data.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    private String name;

    @Indexed(unique = true)
    private String email;

    @JsonIgnore
    private String password;

}
