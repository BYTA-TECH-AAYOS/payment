package com.bytatech.ayoos.payment.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A AmountDetails.
 */
@Entity
@Table(name = "amount_details")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "amountdetails")
public class AmountDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subtotal")
    private Double subtotal;

    @Column(name = "tax")
    private Double tax;

    @Column(name = "shipping")
    private Double shipping;

    @Column(name = "handling_fee")
    private Double handlingFee;

    @Column(name = "shipping_discount")
    private Double shippingDiscount;

    @Column(name = "insurance")
    private Double insurance;

    @Column(name = "other_fee")
    private Double otherFee;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getSubtotal() {
        return subtotal;
    }

    public AmountDetails subtotal(Double subtotal) {
        this.subtotal = subtotal;
        return this;
    }

    public void setSubtotal(Double subtotal) {
        this.subtotal = subtotal;
    }

    public Double getTax() {
        return tax;
    }

    public AmountDetails tax(Double tax) {
        this.tax = tax;
        return this;
    }

    public void setTax(Double tax) {
        this.tax = tax;
    }

    public Double getShipping() {
        return shipping;
    }

    public AmountDetails shipping(Double shipping) {
        this.shipping = shipping;
        return this;
    }

    public void setShipping(Double shipping) {
        this.shipping = shipping;
    }

    public Double getHandlingFee() {
        return handlingFee;
    }

    public AmountDetails handlingFee(Double handlingFee) {
        this.handlingFee = handlingFee;
        return this;
    }

    public void setHandlingFee(Double handlingFee) {
        this.handlingFee = handlingFee;
    }

    public Double getShippingDiscount() {
        return shippingDiscount;
    }

    public AmountDetails shippingDiscount(Double shippingDiscount) {
        this.shippingDiscount = shippingDiscount;
        return this;
    }

    public void setShippingDiscount(Double shippingDiscount) {
        this.shippingDiscount = shippingDiscount;
    }

    public Double getInsurance() {
        return insurance;
    }

    public AmountDetails insurance(Double insurance) {
        this.insurance = insurance;
        return this;
    }

    public void setInsurance(Double insurance) {
        this.insurance = insurance;
    }

    public Double getOtherFee() {
        return otherFee;
    }

    public AmountDetails otherFee(Double otherFee) {
        this.otherFee = otherFee;
        return this;
    }

    public void setOtherFee(Double otherFee) {
        this.otherFee = otherFee;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AmountDetails amountDetails = (AmountDetails) o;
        if (amountDetails.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), amountDetails.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AmountDetails{" +
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
