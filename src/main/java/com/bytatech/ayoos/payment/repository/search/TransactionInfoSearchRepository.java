package com.bytatech.ayoos.payment.repository.search;

import com.bytatech.ayoos.payment.domain.TransactionInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the TransactionInfo entity.
 */
public interface TransactionInfoSearchRepository extends ElasticsearchRepository<TransactionInfo, Long> {
}
