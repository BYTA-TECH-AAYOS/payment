package com.bytatech.ayoos.payment.repository;

import com.bytatech.ayoos.payment.domain.FundingInstrument;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FundingInstrument entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FundingInstrumentRepository extends JpaRepository<FundingInstrument, Long> {

}
