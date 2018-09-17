package com.bytatech.ayoos.payment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A FundingInstrument.
 */
@Entity
@Table(name = "funding_instrument")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "fundinginstrument")
public class FundingInstrument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties("fundingInstruments")
    private Payer payer;

    @OneToOne
    @JoinColumn(unique = true)
    private CreditCard creditCard;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Payer getPayer() {
        return payer;
    }

    public FundingInstrument payer(Payer payer) {
        this.payer = payer;
        return this;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }

    public FundingInstrument creditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
        return this;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
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
        FundingInstrument fundingInstrument = (FundingInstrument) o;
        if (fundingInstrument.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), fundingInstrument.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FundingInstrument{" +
            "id=" + getId() +
            "}";
    }
}
