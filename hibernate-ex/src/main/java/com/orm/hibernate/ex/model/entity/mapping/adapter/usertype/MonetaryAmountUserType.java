package com.orm.hibernate.ex.model.entity.mapping.adapter.usertype;

import com.orm.hibernate.ex.model.entity.mapping.adapter.usertype.example.MonetaryAmount;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.Type;
import org.hibernate.usertype.CompositeUserType;
import org.hibernate.usertype.DynamicParameterizedType;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Currency;
import java.util.Properties;

public class MonetaryAmountUserType implements CompositeUserType, DynamicParameterizedType {
    private Currency convertTo;

    @Override
    public void setParameterValues(Properties parameters) {
        final ParameterType parameterType = (ParameterType) parameters.get(PARAMETER_TYPE);
        final String[] columns = parameterType.getColumns();
        final String table = parameterType.getTable();
        final Annotation[] annotations = parameterType.getAnnotationsMethod();

        final String convertToParameter = parameters.getProperty("convertTo");
        this.convertTo = Currency.getInstance(convertToParameter != null ? convertToParameter : "USD");
    }

    @Override
    public Class returnedClass() {
        return MonetaryAmount.class;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public Serializable disassemble(Object value, SharedSessionContractImplementor session) throws HibernateException {
        return value.toString();
    }

    @Override
    public Object assemble(Serializable cached, SharedSessionContractImplementor session, Object owner) throws HibernateException {
        return MonetaryAmount.fromString((String) cached);
    }

    @Override
    public Object replace(Object original, Object target, SharedSessionContractImplementor session, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y || !(x == null || y == null) && x.equals(y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    // вызывается при чтении из БД
    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws HibernateException, SQLException {
        if (rs.wasNull()) {
            return null;
        }
        final BigDecimal amount = rs.getBigDecimal(names[0]);
        final Currency currency = Currency.getInstance(rs.getString(names[1]));
        return new MonetaryAmount(amount, currency);
    }

    // вызывается при записи в БД
    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, StandardBasicTypes.BIG_DECIMAL.sqlType());
            st.setNull(index + 1, StandardBasicTypes.CURRENCY.sqlType());
        } else {
            final MonetaryAmount amount = (MonetaryAmount) value;
            MonetaryAmount dbAmount = convert(amount, convertTo);
            st.setBigDecimal(index, dbAmount.getValue());
            st.setString(index + 1, convertTo.getCurrencyCode());
        }
    }

    private MonetaryAmount convert(MonetaryAmount amount, Currency toCurrency) {
        return new MonetaryAmount(
                amount.getValue().multiply(new BigDecimal(2)),
                toCurrency
        );
    }

    @Override
    public String[] getPropertyNames() {
        return new String[]{
                "value", "currency"
        };
    }

    @Override
    public Type[] getPropertyTypes() {
        return new Type[]{
                StandardBasicTypes.BIG_DECIMAL,
                StandardBasicTypes.CURRENCY
        };
    }

    @Override
    public Object getPropertyValue(Object component, int property) throws HibernateException {
        final MonetaryAmount monetaryAmount = (MonetaryAmount) component;
        if (property == 0) {
            return monetaryAmount.getValue();
        } else {
            return monetaryAmount.getCurrency();
        }
    }

    @Override
    public void setPropertyValue(Object component, int property, Object value) throws HibernateException {
        throw new UnsupportedOperationException("MonetaryAmount is immutable");
    }
}
