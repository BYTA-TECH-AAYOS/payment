package com.bytatech.ayoos.payment.repository;

import com.bytatech.ayoos.payment.domain.Payment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Payment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Payment findByPaymentId(String paymentId);

}
