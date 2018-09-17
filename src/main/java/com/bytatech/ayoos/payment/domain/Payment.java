package com.bytatech.ayoos.payment.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A Payment.
 */
@Entity
@Table(name = "payment")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "payment")
public class Payment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "state")
    private String state;

    @Column(name = "intent")
    private String intent;

    @Column(name = "payment_gateway_provider")
    private String paymentGatewayProvider;

    @OneToOne(cascade=CascadeType.PERSIST,fetch=FetchType.EAGER)
    @JoinColumn(unique = true)
    private Payer payer;

    @OneToOne(cascade=CascadeType.PERSIST,fetch=FetchType.EAGER)
    @JoinColumn(unique = true)
    private TransactionInfo transaction;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public Payment paymentId(String paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public Payment createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public Payment updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public String getState() {
        return state;
    }

    public Payment state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getIntent() {
        return intent;
    }

    public Payment intent(String intent) {
        this.intent = intent;
        return this;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getPaymentGatewayProvider() {
        return paymentGatewayProvider;
    }

    public Payment paymentGatewayProvider(String paymentGatewayProvider) {
        this.paymentGatewayProvider = paymentGatewayProvider;
        return this;
    }

    public void setPaymentGatewayProvider(String paymentGatewayProvider) {
        this.paymentGatewayProvider = paymentGatewayProvider;
    }

    public Payer getPayer() {
        return payer;
    }

    public Payment payer(Payer payer) {
        this.payer = payer;
        return this;
    }

    public void setPayer(Payer payer) {
        this.payer = payer;
    }

    public TransactionInfo getTransaction() {
        return transaction;
    }

    public Payment transaction(TransactionInfo transactionInfo) {
        this.transaction = transactionInfo;
        return this;
    }

    public void setTransaction(TransactionInfo transactionInfo) {
        this.transaction = transactionInfo;
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
        Payment payment = (Payment) o;
        if (payment.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payment.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Payment{" +
            "id=" + getId() +
            ", paymentId='" + getPaymentId() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", state='" + getState() + "'" +
            ", intent='" + getIntent() + "'" +
            ", paymentGatewayProvider='" + getPaymentGatewayProvider() + "'" +
            "}";
    }
}
