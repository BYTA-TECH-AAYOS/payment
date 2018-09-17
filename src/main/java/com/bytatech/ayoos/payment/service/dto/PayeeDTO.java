package com.bytatech.ayoos.payment.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the Payee entity.
 */
public class PayeeDTO implements Serializable {

    private Long id;

    private String userId;

    private String merchandId;

    private String email;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchandId() {
        return merchandId;
    }

    public void setMerchandId(String merchandId) {
        this.merchandId = merchandId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        PayeeDTO payeeDTO = (PayeeDTO) o;
        if (payeeDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payeeDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PayeeDTO{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", merchandId='" + getMerchandId() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
