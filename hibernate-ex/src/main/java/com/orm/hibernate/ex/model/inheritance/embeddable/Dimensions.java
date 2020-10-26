package com.orm.hibernate.ex.model.inheritance.embeddable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Embeddable
@AttributeOverrides({
        @AttributeOverride(name = "name", column = @Column(name = "DIMENSIONS_NAME")),
        @AttributeOverride(name = "symbol", column = @Column(name = "DIMENSIONS_SYMBOL"))
})
public class Dimensions extends Measurement {
    @NotNull
    private BigDecimal depth;
    @NotNull
    private BigDecimal height;
    @NotNull
    private BigDecimal width;

    public Dimensions() {
    }

    public Dimensions(@NotNull BigDecimal depth, @NotNull BigDecimal height, @NotNull BigDecimal width) {
        this.depth = depth;
        this.height = height;
        this.width = width;
        setName("Dimension name");
        setSymbol("Dimension symbol");
    }

    @Override
    public String toString() {
        return "Dimensions{" +
                "depth=" + depth +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}
