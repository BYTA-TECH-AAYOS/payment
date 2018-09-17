package com.bytatech.ayoos.payment.service.dto;

import java.time.Instant;
import java.io.Serializable;
import java.util.Objects;
import com.bytatech.ayoos.payment.domain.enumeration.TransactionType;

/**
 * A DTO for the RelatedTransactions entity.
 */
public class RelatedTransactionsDTO implements Serializable {

    private Long id;

    private TransactionType transactionType;

    private String intentId;

    private String paymentMode;

    private String state;

    private String saleReasonCode;

    private String paymentId;

    private String receiptId;

    private Instant createTime;

    private Instant updateTime;

    private Instant authValidUntil;

    private String refundReason;

    private String orderPendingReason;

    private String refundSaleId;

    private String description;

    private Long transactionInfoId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public String getIntentId() {
        return intentId;
    }

    public void setIntentId(String intentId) {
        this.intentId = intentId;
    }

    public String getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(String paymentMode) {
        this.paymentMode = paymentMode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSaleReasonCode() {
        return saleReasonCode;
    }

    public void setSaleReasonCode(String saleReasonCode) {
        this.saleReasonCode = saleReasonCode;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public Instant getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Instant createTime) {
        this.createTime = createTime;
    }

    public Instant getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Instant updateTime) {
        this.updateTime = updateTime;
    }

    public Instant getAuthValidUntil() {
        return authValidUntil;
    }

    public void setAuthValidUntil(Instant authValidUntil) {
        this.authValidUntil = authValidUntil;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    public String getOrderPendingReason() {
        return orderPendingReason;
    }

    public void setOrderPendingReason(String orderPendingReason) {
        this.orderPendingReason = orderPendingReason;
    }

    public String getRefundSaleId() {
        return refundSaleId;
    }

    public void setRefundSaleId(String refundSaleId) {
        this.refundSaleId = refundSaleId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getTransactionInfoId() {
        return transactionInfoId;
    }

    public void setTransactionInfoId(Long transactionInfoId) {
        this.transactionInfoId = transactionInfoId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        RelatedTransactionsDTO relatedTransactionsDTO = (RelatedTransactionsDTO) o;
        if (relatedTransactionsDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), relatedTransactionsDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "RelatedTransactionsDTO{" +
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
            ", transactionInfo=" + getTransactionInfoId() +
            "}";
    }
}
