package com.bytatech.ayoos.payment.repository;

import com.bytatech.ayoos.payment.domain.RelatedTransactions;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RelatedTransactions entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RelatedTransactionsRepository extends JpaRepository<RelatedTransactions, Long> {

}
