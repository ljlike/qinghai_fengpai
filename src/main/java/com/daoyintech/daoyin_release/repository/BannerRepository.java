package com.daoyintech.daoyin_release.repository;

import com.daoyintech.daoyin_release.entity.banner.Banner;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends CrudRepository<Banner,Long> {

    List<Banner> findAllByIsSell(Boolean isSell);

}
