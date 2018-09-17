package com.bytatech.ayoos.payment.service.dto;

import java.io.Serializable;
import java.util.Objects;
import com.bytatech.ayoos.payment.domain.enumeration.PaymentMethod;

/**
 * A DTO for the Payer entity.
 */
public class PayerDTO implements Serializable {

    private Long id;

    private PaymentMethod paymentMethod;

    private String status;

    private String userId;

    private String payerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayerId() {
        return payerId;
    }

    public void setPayerId(String payerId) {
        this.payerId = payerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PayerDTO payerDTO = (PayerDTO) o;
        if (payerDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payerDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PayerDTO{" +
            "id=" + getId() +
            ", paymentMethod='" + getPaymentMethod() + "'" +
            ", status='" + getStatus() + "'" +
            ", userId='" + getUserId() + "'" +
            ", payerId='" + getPayerId() + "'" +
            "}";
    }
}
