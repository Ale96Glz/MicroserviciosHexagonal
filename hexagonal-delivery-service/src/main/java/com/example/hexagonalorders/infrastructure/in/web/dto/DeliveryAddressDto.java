package com.example.hexagonalorders.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * DTO para los datos de la dirección de entrega.
 * Esta clase se utiliza para transferir datos de dirección de entrega entre la capa adaptadora
 * y la capa de aplicación.
 */
@Data
public class DeliveryAddressDto {
    @Schema(description = "Calle y número de la dirección.", 
            example = "Calle 23 #456", required = true)
    private String street;

    @Schema(description = "Ciudad.", 
            example = "La Habana", required = true)
    private String city;

    @Schema(description = "Estado o provincia.", 
            example = "La Habana", required = true)
    private String state;

    @Schema(description = "Código postal.", 
            example = "10400", required = true)
    private String postalCode;

    @Schema(description = "País.", 
            example = "Cuba", required = true)
    private String country;

    public DeliveryAddressDto() {}

    public DeliveryAddressDto(String street, String city, String state, String postalCode, String country) {
        this.street = street;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.country = country;
    }

    // Getters manuales para evitar problemas con Lombok
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPostalCode() { return postalCode; }
    public String getCountry() { return country; }

    // Setters manuales para evitar problemas con Lombok
    public void setStreet(String street) { this.street = street; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setCountry(String country) { this.country = country; }
} 