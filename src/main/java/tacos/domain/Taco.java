package tacos.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
public class Taco implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, message = "Name must be at least 5 characters long")
    private String name;

    private Date createdAt;

    @Size(min = 1, message = "You must choose at least 1 ingredient")
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Ingredient> ingredients = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
    }
}
