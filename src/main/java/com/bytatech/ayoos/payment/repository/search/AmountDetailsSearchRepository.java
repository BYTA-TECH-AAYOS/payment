package com.bytatech.ayoos.payment.repository.search;

import com.bytatech.ayoos.payment.domain.AmountDetails;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the AmountDetails entity.
 */
public interface AmountDetailsSearchRepository extends ElasticsearchRepository<AmountDetails, Long> {
}
