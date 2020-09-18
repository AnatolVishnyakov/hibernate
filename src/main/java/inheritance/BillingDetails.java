package inheritance;

import jakarta.validation.constraints.NotNull;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BillingDetails {
    @NotNull
    protected String owner;
}
