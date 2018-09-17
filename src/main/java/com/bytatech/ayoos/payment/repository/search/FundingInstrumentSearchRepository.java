package com.bytatech.ayoos.payment.repository.search;

import com.bytatech.ayoos.payment.domain.FundingInstrument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the FundingInstrument entity.
 */
public interface FundingInstrumentSearchRepository extends ElasticsearchRepository<FundingInstrument, Long> {
}
