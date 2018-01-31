package com.kzk.kmall.repository.search;

import com.kzk.kmall.domain.Goods;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Goods entity.
 */
public interface GoodsSearchRepository extends ElasticsearchRepository<Goods, Long> {
}
