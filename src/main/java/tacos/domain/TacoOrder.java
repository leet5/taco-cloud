package tacos.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.CreditCardNumber;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Pattern;
import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Entity
@RequiredArgsConstructor
public class TacoOrder implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreditCardNumber(message = "Not a valid credit card number")
    private String ccNumber;

    @Pattern(regexp = "^(0[1-9]|1[0-2])([\\/])([2-9][0-9])$", message = "Must be formatted MM/YY")
    private String ccExpiration;

    @Digits(integer = 3, fraction = 0, message = "Invalid CVV")
    @Column(name = "cc_cvv")
    private String ccCVV;

    private Date placedAt;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Taco> tacos = new ArrayList<>();

    @ManyToOne
    private User user;

    public void addTaco(Taco taco) {
        this.tacos.add(taco);
    }

    @PrePersist
    protected void onCreate() {
        placedAt = new Date();
    }
}
