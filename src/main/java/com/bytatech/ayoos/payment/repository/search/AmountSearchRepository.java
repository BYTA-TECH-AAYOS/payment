package com.bytatech.ayoos.payment.repository.search;

import com.bytatech.ayoos.payment.domain.Amount;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Amount entity.
 */
public interface AmountSearchRepository extends ElasticsearchRepository<Amount, Long> {
}
