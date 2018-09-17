package com.bytatech.ayoos.payment.repository;

import com.bytatech.ayoos.payment.domain.AmountDetails;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the AmountDetails entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AmountDetailsRepository extends JpaRepository<AmountDetails, Long> {

}
