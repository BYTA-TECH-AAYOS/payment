package com.bytatech.ayoos.payment.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import org.springframework.data.elasticsearch.annotations.Document;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Payee.
 */
@Entity
@Table(name = "payee")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "payee")
public class Payee implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "merchand_id")
    private String merchandId;

    @Column(name = "email")
    private String email;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public Payee userId(String userId) {
        this.userId = userId;
        return this;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMerchandId() {
        return merchandId;
    }

    public Payee merchandId(String merchandId) {
        this.merchandId = merchandId;
        return this;
    }

    public void setMerchandId(String merchandId) {
        this.merchandId = merchandId;
    }

    public String getEmail() {
        return email;
    }

    public Payee email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
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
        Payee payee = (Payee) o;
        if (payee.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), payee.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Payee{" +
            "id=" + getId() +
            ", userId='" + getUserId() + "'" +
            ", merchandId='" + getMerchandId() + "'" +
            ", email='" + getEmail() + "'" +
            "}";
    }
}
