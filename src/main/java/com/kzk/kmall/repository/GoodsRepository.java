package com.kzk.kmall.repository;

import com.kzk.kmall.domain.Goods;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Goods entity.
 */
@SuppressWarnings("unused")
@Repository
public interface GoodsRepository extends JpaRepository<Goods, Long>, JpaSpecificationExecutor<Goods> {

    @Query("select goods from Goods goods where goods.createBy.login = ?#{principal.username}")
    List<Goods> findByCreateByIsCurrentUser();

}
