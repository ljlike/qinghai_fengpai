package com.daoyintech.daoyin_release.repository.bank;

import com.daoyintech.daoyin_release.entity.bank.Bank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author pei on 2018/08/22
 */
@Repository
public interface BankRepository extends JpaRepository<Bank,Long>{



}
