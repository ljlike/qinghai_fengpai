package com.daoyintech.daoyin_release.repository.order;

import com.daoyintech.daoyin_release.entity.order.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart,Long> {

    Cart findByUserId(Long userId);

}
