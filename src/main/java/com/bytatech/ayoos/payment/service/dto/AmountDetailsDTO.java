package com.bytatech.ayoos.payment.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the AmountDetails entity.
 */
public class AmountDetailsDTO implements Serializable {

    private Long id;

    private Double subtotal;

    private Double tax;

    private Double shipping;

    private Double handlingFee;

    private Double shippingDiscount;

    private Double insurance;

    private Double otherFee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTax() {
        return tax;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getShipping() {
        return shipping;
    }

    public void setShipping(Double shipping) {
        this.shipping = shipping;
    }

    public Double getHandlingFee() {
        return handlingFee;
    }

    public void setHandlingFee(Double handlingFee) {
        this.handlingFee = handlingFee;
    }

    public Double getShippingDiscount() {
        return shippingDiscount;
    }

    public void setShippingDiscount(Double shippingDiscount) {
        this.shippingDiscount = shippingDiscount;
    }

    public Double getInsurance() {
        return insurance;
    }

    public void setInsurance(Double insurance) {
        this.insurance = insurance;
    }

    public Double getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(Double otherFee) {
        this.otherFee = otherFee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AmountDetailsDTO amountDetailsDTO = (AmountDetailsDTO) o;
        if (amountDetailsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), amountDetailsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AmountDetailsDTO{" +
            "id=" + getId() +
            ", subtotal=" + getSubtotal() +
            ", tax=" + getTax() +
            ", shipping=" + getShipping() +
            ", handlingFee=" + getHandlingFee() +
            ", shippingDiscount=" + getShippingDiscount() +
            ", insurance=" + getInsurance() +
            ", otherFee=" + getOtherFee() +
            "}";
    }
}
