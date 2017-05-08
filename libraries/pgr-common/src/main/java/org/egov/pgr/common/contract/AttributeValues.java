package org.egov.pgr.common.contract;

import lombok.AllArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class AttributeValues {
    List<AttributeEntry> attributeValues;

    public static String getAttributeSingleValue(List<AttributeEntry> attributeValues, String expectedKey) {
        return attributeValues.stream()
            .filter(a -> expectedKey.equals(a.getKey()))
            .findFirst()
            .map(AttributeEntry::getName)
            .orElse(null);
    }

    public static List<String> getAttributeMultipleValue(List<AttributeEntry> attributeValues, String expectedKey) {
        return attributeValues.stream()
            .filter(a -> expectedKey.equals(a.getKey()))
            .map(AttributeEntry::getName)
            .collect(Collectors.toList());
    }
}