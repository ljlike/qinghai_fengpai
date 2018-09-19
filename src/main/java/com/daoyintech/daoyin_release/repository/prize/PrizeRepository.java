package com.daoyintech.daoyin_release.repository.prize;

import com.daoyintech.daoyin_release.entity.draw.Prize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrizeRepository extends JpaRepository<Prize, Long> {

    List<Prize> findAllByOrderByMaxPrizePersonsCountDesc();

}
