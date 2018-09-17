package com.bytatech.ayoos.payment.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A CreditCard.
 */
@Entity
@Table(name = "credit_card")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "creditcard")
public class CreditCard implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "jhi_number")
    private String number;

    @Column(name = "jhi_type")
    private String type;

    @Column(name = "expire_month")
    private Integer expireMonth;

    @Column(name = "expire_year")
    private Integer expireYear;

    @Column(name = "cvv_2")
    private Integer cvv2;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public CreditCard number(String number) {
        this.number = number;
        return this;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public CreditCard type(String type) {
        this.type = type;
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getExpireMonth() {
        return expireMonth;
    }

    public CreditCard expireMonth(Integer expireMonth) {
        this.expireMonth = expireMonth;
        return this;
    }

    public void setExpireMonth(Integer expireMonth) {
        this.expireMonth = expireMonth;
    }

    public Integer getExpireYear() {
        return expireYear;
    }

    public CreditCard expireYear(Integer expireYear) {
        this.expireYear = expireYear;
        return this;
    }

    public void setExpireYear(Integer expireYear) {
        this.expireYear = expireYear;
    }

    public Integer getCvv2() {
        return cvv2;
    }

    public CreditCard cvv2(Integer cvv2) {
        this.cvv2 = cvv2;
        return this;
    }

    public void setCvv2(Integer cvv2) {
        this.cvv2 = cvv2;
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
        CreditCard creditCard = (CreditCard) o;
        if (creditCard.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), creditCard.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "CreditCard{" +
            "id=" + getId() +
            ", number='" + getNumber() + "'" +
            ", type='" + getType() + "'" +
            ", expireMonth=" + getExpireMonth() +
            ", expireYear=" + getExpireYear() +
            ", cvv2=" + getCvv2() +
            "}";
    }
}
