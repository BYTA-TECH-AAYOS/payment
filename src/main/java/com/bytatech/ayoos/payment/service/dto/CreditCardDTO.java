package com.bytatech.ayoos.payment.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the CreditCard entity.
 */
public class CreditCardDTO implements Serializable {

    private Long id;

    private String number;

    private String type;

    private Integer expireMonth;

    private Integer expireYear;

    private Integer cvv2;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(Integer expireMonth) {
        this.expireMonth = expireMonth;
    }

    public Integer getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(Integer expireYear) {
        this.expireYear = expireYear;
    }

    public Integer getCvv2() {
        return cvv2;
    }

    public void setCvv2(Integer cvv2) {
        this.cvv2 = cvv2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CreditCardDTO creditCardDTO = (CreditCardDTO) o;
        if (creditCardDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), creditCardDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CreditCardDTO{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", type='" + getType() + "'" +
            ", expireMonth=" + getExpireMonth() +
            ", expireYear=" + getExpireYear() +
            ", cvv2=" + getCvv2() +
            "}";
    }
}
