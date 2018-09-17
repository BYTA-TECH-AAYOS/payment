package com.bytatech.ayoos.payment.repository;

import com.bytatech.ayoos.payment.domain.Payee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the Payee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PayeeRepository extends JpaRepository<Payee, Long> {

}
