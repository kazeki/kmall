package com.kzk.kmall.repository;

import com.kzk.kmall.domain.SystemNotice;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the SystemNotice entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemNoticeRepository extends JpaRepository<SystemNotice, Long>, JpaSpecificationExecutor<SystemNotice> {

    @Query("select system_notice from SystemNotice system_notice where system_notice.sendTo.login = ?#{principal.username}")
    List<SystemNotice> findBySendToIsCurrentUser();

}
