package com.daoyintech.daoyin_release.repository.card;

import com.daoyintech.daoyin_release.entity.card.WxCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<WxCard, Long> {

    WxCard findByTypeAndStatus(int type, int status);

}
