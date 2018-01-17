package com.kzk.kmall.repository;

import com.kzk.kmall.domain.Shop;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the Shop entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    @Query("select shop from Shop shop where shop.master.login = ?#{principal.username}")
    List<Shop> findByMasterIsCurrentUser();

}
