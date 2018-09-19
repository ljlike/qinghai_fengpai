package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.product.SubProductType;
import com.daoyintech.daoyin_release.repository.product.SubProductTypeRepository;
import com.daoyintech.daoyin_release.service.SubProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author pei on 2018/08/16
 */
@Service
public class SubProductTypeServiceImpl implements SubProductTypeService {

    @Autowired
    private SubProductTypeRepository subProductTypeRepository;

    @Override
    public SubProductType getOne(Long subProductTypeId) {
        return subProductTypeRepository.getOne(subProductTypeId);
    }
}
