package com.bytatech.ayoos.payment.repository.search;

import com.bytatech.ayoos.payment.domain.Payer;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Payer entity.
 */
public interface PayerSearchRepository extends ElasticsearchRepository<Payer, Long> {
}
