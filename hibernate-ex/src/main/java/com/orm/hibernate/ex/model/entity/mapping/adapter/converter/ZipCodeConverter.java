package com.orm.hibernate.ex.model.entity.mapping.adapter.converter;

import com.orm.hibernate.ex.model.entity.mapping.adapter.example.zipcode.GermanZipCode;
import com.orm.hibernate.ex.model.entity.mapping.adapter.example.zipcode.SwissZipCode;
import com.orm.hibernate.ex.model.entity.mapping.adapter.example.zipcode.ZipCode;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class ZipCodeConverter implements AttributeConverter<ZipCode, String> {
    @Override
    public String convertToDatabaseColumn(ZipCode zipCode) {
        return zipCode.getValue();
    }

    @Override
    public ZipCode convertToEntityAttribute(String s) {
        if (s.length() == 5) {
            return new GermanZipCode(s);
        } else if (s.length() == 4) {
            return new SwissZipCode(s);
        }
        throw new IllegalArgumentException("Unsupported zipcode in database: " + s);
    }
}
