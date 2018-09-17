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

/**
 * A TransactionInfo.
 */
@Entity
@Table(name = "transaction_info")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "transactioninfo")
public class TransactionInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "description")
    private String description;

    @Column(name = "note_to_payee")
    private String noteToPayee;

    @Column(name = "invoice_number")
    private String invoiceNumber;

    @OneToOne(cascade = CascadeType.PERSIST,fetch=FetchType.EAGER)
    @JoinColumn(unique = true)
    private Amount amount;

    @OneToOne(cascade = CascadeType.PERSIST,fetch=FetchType.EAGER)
    @JoinColumn(unique = true)
    private Payee payee;

    @OneToMany(mappedBy = "transactionInfo",cascade = CascadeType.PERSIST,fetch=FetchType.EAGER)
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<RelatedTransactions> transactionDetails = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public TransactionInfo description(String description) {
        this.description = description;
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNoteToPayee() {
        return noteToPayee;
    }

    public TransactionInfo noteToPayee(String noteToPayee) {
        this.noteToPayee = noteToPayee;
        return this;
    }

    public void setNoteToPayee(String noteToPayee) {
        this.noteToPayee = noteToPayee;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public TransactionInfo invoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
        return this;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Amount getAmount() {
        return amount;
    }

    public TransactionInfo amount(Amount amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Amount amount) {
        this.amount = amount;
    }

    public Payee getPayee() {
        return payee;
    }

    public TransactionInfo payee(Payee payee) {
        this.payee = payee;
        return this;
    }

    public void setPayee(Payee payee) {
        this.payee = payee;
    }

    public Set<RelatedTransactions> getTransactionDetails() {
        return transactionDetails;
    }

    public TransactionInfo transactionDetails(Set<RelatedTransactions> relatedTransactions) {
        this.transactionDetails = relatedTransactions;
        return this;
    }

    public TransactionInfo addTransactionDetails(RelatedTransactions relatedTransactions) {
        this.transactionDetails.add(relatedTransactions);
        relatedTransactions.setTransactionInfo(this);
        return this;
    }

    public TransactionInfo removeTransactionDetails(RelatedTransactions relatedTransactions) {
        this.transactionDetails.remove(relatedTransactions);
        relatedTransactions.setTransactionInfo(null);
        return this;
    }

    public void setTransactionDetails(Set<RelatedTransactions> relatedTransactions) {
        this.transactionDetails = relatedTransactions;
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
        TransactionInfo transactionInfo = (TransactionInfo) o;
        if (transactionInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), transactionInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TransactionInfo{" +
            "id=" + getId() +
            ", description='" + getDescription() + "'" +
            ", noteToPayee='" + getNoteToPayee() + "'" +
            ", invoiceNumber='" + getInvoiceNumber() + "'" +
            "}";
    }
}
