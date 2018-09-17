package com.bytatech.ayoos.payment.repository.search;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Configuration;

/**
 * Configure a Mock version of RelatedTransactionsSearchRepository to test the
 * application without starting Elasticsearch.
 */
@Configuration
public class RelatedTransactionsSearchRepositoryMockConfiguration {

    @MockBean
    private RelatedTransactionsSearchRepository mockRelatedTransactionsSearchRepository;

}
