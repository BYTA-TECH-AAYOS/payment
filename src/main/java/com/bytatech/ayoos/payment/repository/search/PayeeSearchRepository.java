package com.bytatech.ayoos.payment.repository.search;

import com.bytatech.ayoos.payment.domain.Payee;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Payee entity.
 */
public interface PayeeSearchRepository extends ElasticsearchRepository<Payee, Long> {
}
