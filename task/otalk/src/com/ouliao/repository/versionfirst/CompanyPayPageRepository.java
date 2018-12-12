package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.CompanyPay;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by nessary on 16-5-30.
 */
public interface CompanyPayPageRepository extends JpaRepository<CompanyPay, Long> {
}
