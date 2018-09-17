package com.bytatech.ayoos.payment.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the TransactionInfo entity.
 */
public class TransactionInfoDTO implements Serializable {

    private Long id;

    private String description;

    private String noteToPayee;

    private String invoiceNumber;

    private Long amountId;

    private Long payeeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoteToPayee() {
        return noteToPayee;
    }

    public void setNoteToPayee(String noteToPayee) {
        this.noteToPayee = noteToPayee;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Long getAmountId() {
        return amountId;
    }

    public void setAmountId(Long amountId) {
        this.amountId = amountId;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        TransactionInfoDTO transactionInfoDTO = (TransactionInfoDTO) o;
        if (transactionInfoDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionInfoDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionInfoDTO{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", noteToPayee='" + getNoteToPayee() + "'" +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            ", amount=" + getAmountId() +
            ", payee=" + getPayeeId() +
            "}";
    }
}
