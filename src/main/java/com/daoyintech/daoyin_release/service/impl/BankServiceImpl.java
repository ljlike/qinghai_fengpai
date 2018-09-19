package com.daoyintech.daoyin_release.service.impl;

import com.daoyintech.daoyin_release.entity.bank.Bank;
import com.daoyintech.daoyin_release.repository.bank.BankRepository;
import com.daoyintech.daoyin_release.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author pei on 2018/08/22
 */
@Service
public class BankServiceImpl implements BankService {

    @Autowired
    private BankRepository bankRepository;

    @Override
    public List<Bank> findAll() {
        return bankRepository.findAll();
    }
}




