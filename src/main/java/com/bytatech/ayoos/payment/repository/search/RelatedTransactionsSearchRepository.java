package com.bytatech.ayoos.payment.repository.search;

import com.bytatech.ayoos.payment.domain.RelatedTransactions;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the RelatedTransactions entity.
 */
public interface RelatedTransactionsSearchRepository extends ElasticsearchRepository<RelatedTransactions, Long> {
}
