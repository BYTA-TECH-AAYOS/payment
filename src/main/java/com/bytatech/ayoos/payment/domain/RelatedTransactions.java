package com.bytatech.ayoos.payment.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

import com.bytatech.ayoos.payment.domain.enumeration.TransactionType;

/**
 * A RelatedTransactions.
 */
@Entity
@Table(name = "related_transactions")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "relatedtransactions")
public class RelatedTransactions implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transaction_type")
    private TransactionType transactionType;

    @Column(name = "intent_id")
    private String intentId;

    @Column(name = "payment_mode")
    private String paymentMode;

    @Column(name = "state")
    private String state;

    @Column(name = "sale_reason_code")
    private String saleReasonCode;

    @Column(name = "payment_id")
    private String paymentId;

    @Column(name = "receipt_id")
    private String receiptId;

    @Column(name = "create_time")
    private Instant createTime;

    @Column(name = "update_time")
    private Instant updateTime;

    @Column(name = "auth_valid_until")
    private Instant authValidUntil;

    @Column(name = "refund_reason")
    private String refundReason;

    @Column(name = "order_pending_reason")
    private String orderPendingReason;

    @Column(name = "refund_sale_id")
    private String refundSaleId;

    @Column(name = "description")
    private String description;

    @ManyToOne
    @JsonIgnoreProperties("transactionDetails")
    private TransactionInfo transactionInfo;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public RelatedTransactions transactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
        return this;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getIntentId() {
        return intentId;
    }

    public RelatedTransactions intentId(String intentId) {
        this.intentId = intentId;
        return this;
    }

    public void setIntentId(String intentId) {
        this.intentId = intentId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public RelatedTransactions paymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
        return this;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getState() {
        return state;
    }

    public RelatedTransactions state(String state) {
        this.state = state;
        return this;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSaleReasonCode() {
        return saleReasonCode;
    }

    public RelatedTransactions saleReasonCode(String saleReasonCode) {
        this.saleReasonCode = saleReasonCode;
        return this;
    }

    public void setSaleReasonCode(String saleReasonCode) {
        this.saleReasonCode = saleReasonCode;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public RelatedTransactions paymentId(String paymentId) {
        this.paymentId = paymentId;
        return this;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public RelatedTransactions receiptId(String receiptId) {
        this.receiptId = receiptId;
        return this;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public RelatedTransactions createTime(Instant createTime) {
        this.createTime = createTime;
        return this;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public RelatedTransactions updateTime(Instant updateTime) {
        this.updateTime = updateTime;
        return this;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Instant getAuthValidUntil() {
        return authValidUntil;
    }

    public RelatedTransactions authValidUntil(Instant authValidUntil) {
        this.authValidUntil = authValidUntil;
        return this;
    }

    public void setAuthValidUntil(Instant authValidUntil) {
        this.authValidUntil = authValidUntil;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public RelatedTransactions refundReason(String refundReason) {
        this.refundReason = refundReason;
        return this;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getOrderPendingReason() {
        return orderPendingReason;
    }

    public RelatedTransactions orderPendingReason(String orderPendingReason) {
        this.orderPendingReason = orderPendingReason;
        return this;
    }

    public void setOrderPendingReason(String orderPendingReason) {
        this.orderPendingReason = orderPendingReason;
    }

    public String getRefundSaleId() {
        return refundSaleId;
    }

    public RelatedTransactions refundSaleId(String refundSaleId) {
        this.refundSaleId = refundSaleId;
        return this;
    }

    public void setRefundSaleId(String refundSaleId) {
        this.refundSaleId = refundSaleId;
    }

    public String getDescription() {
        return description;
    }

    public RelatedTransactions description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    public RelatedTransactions transactionInfo(TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
        return this;
    }

    public void setTransactionInfo(TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
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
        RelatedTransactions relatedTransactions = (RelatedTransactions) o;
        if (relatedTransactions.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), relatedTransactions.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RelatedTransactions{" +
            "id=" + getId() +
            ", transactionType='" + getTransactionType() + "'" +
            ", intentId='" + getIntentId() + "'" +
            ", paymentMode='" + getPaymentMode() + "'" +
            ", state='" + getState() + "'" +
            ", saleReasonCode='" + getSaleReasonCode() + "'" +
            ", paymentId='" + getPaymentId() + "'" +
            ", receiptId='" + getReceiptId() + "'" +
            ", createTime='" + getCreateTime() + "'" +
            ", updateTime='" + getUpdateTime() + "'" +
            ", authValidUntil='" + getAuthValidUntil() + "'" +
            ", refundReason='" + getRefundReason() + "'" +
            ", orderPendingReason='" + getOrderPendingReason() + "'" +
            ", refundSaleId='" + getRefundSaleId() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
