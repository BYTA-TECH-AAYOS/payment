package com.bytatech.ayoos.payment.repository.search;

import com.bytatech.ayoos.payment.domain.CreditCard;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the CreditCard entity.
 */
public interface CreditCardSearchRepository extends ElasticsearchRepository<CreditCard, Long> {
}
