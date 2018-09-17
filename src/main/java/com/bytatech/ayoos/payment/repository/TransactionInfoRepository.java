package com.bytatech.ayoos.payment.repository;

import com.bytatech.ayoos.payment.domain.TransactionInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the TransactionInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TransactionInfoRepository extends JpaRepository<TransactionInfo, Long> {

}
