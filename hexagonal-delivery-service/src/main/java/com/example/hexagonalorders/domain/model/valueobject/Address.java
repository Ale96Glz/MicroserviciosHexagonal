package com.example.hexagonalorders.domain.model.valueobject;

public record Address(String street, String city, String postalCode, String country) {
    public Address {
        if (street == null || street.trim().isEmpty()) {
            throw new IllegalArgumentException("La calle no puede ser nula o vacia");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("La ciudad no puede ser nula o vacia");
        }
        if (postalCode == null || postalCode.trim().isEmpty()) {
            throw new IllegalArgumentException("El codigo postal no puede ser nulo o vacio");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("El pais no puede ser nulo o vacio");
        }
    }

    @Override
    public String toString() {
        return String.format("%s, %s, %s, %s", street, city, postalCode, country);
    }
} 