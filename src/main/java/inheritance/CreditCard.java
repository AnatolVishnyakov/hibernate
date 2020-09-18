package inheritance;

import jakarta.validation.constraints.NotNull;

import javax.persistence.*;

@Entity
@AttributeOverride(
        name = "owner",
        column = @Column(name = "cc_owner", nullable = false)
)
public class CreditCard extends BillingDetails {
    @Id
    @GeneratedValue(generator = Constants.ID_GENERATOR)
    protected Long id;
    @NotNull
    protected String cardNumber;
    @NotNull
    protected String expMonth;
    @NotNull
    private String expYear;
}
