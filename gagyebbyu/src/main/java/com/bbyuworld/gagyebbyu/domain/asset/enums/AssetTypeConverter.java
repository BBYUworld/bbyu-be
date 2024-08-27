package com.bbyuworld.gagyebbyu.domain.asset.enums;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AssetTypeConverter implements AttributeConverter<AssetType, String> {
    @Override
    public String convertToDatabaseColumn(AssetType attribute) {
        if (attribute == null) {
            return null;
        }
        return attribute.name();
    }

    @Override
    public AssetType convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            return null;
        }
        return AssetType.valueOf(dbData);
    }
}
