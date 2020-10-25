@TypeDefs({
        @TypeDef(
                name = "monetary_amount_usd",
                typeClass = MonetaryAmountUserType.class,
                parameters = {@Parameter(name = "convertTo", value = "USD")}
        ),
        @TypeDef(
                name = "monetary_amount_eur",
                typeClass = MonetaryAmountUserType.class,
                parameters = {@Parameter(name = "convertTo", value = "EUR")}
        )
})
package com.orm.hibernate.ex.model.entity.mapping.adapter.usertype.example;

import com.orm.hibernate.ex.model.entity.mapping.adapter.usertype.MonetaryAmountUserType;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;