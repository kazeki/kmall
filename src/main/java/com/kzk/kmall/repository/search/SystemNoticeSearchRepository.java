package com.kzk.kmall.repository.search;

import com.kzk.kmall.domain.SystemNotice;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SystemNotice entity.
 */
public interface SystemNoticeSearchRepository extends ElasticsearchRepository<SystemNotice, Long> {
}
