package com.orm.hibernate.ex.model.inheritance.embeddable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "WEIGHT_NAME")),
        @AttributeOverride(name = "symbol", column = @Column(name = "WEIGHT_SYMBOL"))
})
public class Weight extends Measurement {
    @NotNull
    @Column(name = "WEIGHT")
    private BigDecimal value;

    public Weight() {
    }

    public Weight(@NotNull BigDecimal value) {
        this.value = value;
        setName("Weight name");
        setSymbol("Weight symbol");
    }

    @Override
    public String toString() {
        return "Weight{" +
                "value=" + value +
                '}';
    }
}
