package com.kzk.kmall.repository.search;

import com.kzk.kmall.domain.Shop;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Shop entity.
 */
public interface ShopSearchRepository extends ElasticsearchRepository<Shop, Long> {
}
