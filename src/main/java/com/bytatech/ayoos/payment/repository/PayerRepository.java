package com.bytatech.ayoos.payment.repository;

import com.bytatech.ayoos.payment.domain.Payer;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Payer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayerRepository extends JpaRepository<Payer, Long> {

}
