package com.bytatech.ayoos.payment.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Amount entity.
 */
public class AmountDTO implements Serializable {

    private Long id;

    private Double total;

    private String currency;

    private Long detailsId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Long getDetailsId() {
        return detailsId;
    }

    public void setDetailsId(Long amountDetailsId) {
        this.detailsId = amountDetailsId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        AmountDTO amountDTO = (AmountDTO) o;
        if (amountDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), amountDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AmountDTO{" +
            "id=" + getId() +
            ", total=" + getTotal() +
            ", currency='" + getCurrency() + "'" +
            ", details=" + getDetailsId() +
            "}";
    }
}
