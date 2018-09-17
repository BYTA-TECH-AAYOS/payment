package com.bytatech.ayoos.payment.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.bytatech.ayoos.payment.domain.enumeration.PaymentMethod;

/**
 * A Payer.
 */
@Entity
@Table(name = "payer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "payer")
public class Payer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "status")
    private String status;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "payer_id")
    private String payerId;

    @OneToMany(mappedBy = "payer",cascade = CascadeType.PERSIST,fetch=FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<FundingInstrument> fundingInstruments = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public Payer paymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public Payer status(String status) {
        this.status = status;
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public Payer userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayerId() {
        return payerId;
    }

    public Payer payerId(String payerId) {
        this.payerId = payerId;
        return this;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    public Set<FundingInstrument> getFundingInstruments() {
        return fundingInstruments;
    }

    public Payer fundingInstruments(Set<FundingInstrument> fundingInstruments) {
        this.fundingInstruments = fundingInstruments;
        return this;
    }

    public Payer addFundingInstruments(FundingInstrument fundingInstrument) {
        this.fundingInstruments.add(fundingInstrument);
        fundingInstrument.setPayer(this);
        return this;
    }

    public Payer removeFundingInstruments(FundingInstrument fundingInstrument) {
        this.fundingInstruments.remove(fundingInstrument);
        fundingInstrument.setPayer(null);
        return this;
    }

    public void setFundingInstruments(Set<FundingInstrument> fundingInstruments) {
        this.fundingInstruments = fundingInstruments;
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
        Payer payer = (Payer) o;
        if (payer.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Payer{" +
            "id=" + getId() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", status='" + getStatus() + "'" +
            ", userId='" + getUserId() + "'" +
            ", payerId='" + getPayerId() + "'" +
            "}";
    }
}
