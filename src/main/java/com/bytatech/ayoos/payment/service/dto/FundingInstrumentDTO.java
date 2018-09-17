package com.bytatech.ayoos.payment.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the FundingInstrument entity.
 */
public class FundingInstrumentDTO implements Serializable {

    private Long id;

    private Long payerId;

    private Long creditCardId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public Long getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(Long creditCardId) {
        this.creditCardId = creditCardId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        FundingInstrumentDTO fundingInstrumentDTO = (FundingInstrumentDTO) o;
        if (fundingInstrumentDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fundingInstrumentDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FundingInstrumentDTO{" +
            "id=" + getId() +
            ", payer=" + getPayerId() +
            ", creditCard=" + getCreditCardId() +
            "}";
    }
}
