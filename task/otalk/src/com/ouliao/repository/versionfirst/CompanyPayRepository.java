package com.ouliao.repository.versionfirst;

import com.ouliao.domain.versionfirst.CompanyPay;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.data.repository.query.Param;

/**
 * Created by nessary on 16-5-30.
 */
@RepositoryDefinition(domainClass = CompanyPay.class, idClass = Long.class)
public interface CompanyPayRepository {
    @Modifying
    @Query("update CompanyPay set companyConsume =:companyConsume  where  isDeleted='0'")
    void updateComanyPayByConmanyConsume(@Param("companyConsume") Double companyConsume);

    @Modifying
    @Query("update CompanyPay set companyCost =:companyCost  where  isDeleted='0'")
    void updateComanyPayByCompanyCost(@Param("companyCost") Double companyCost);


    @Modifying
    @Query("update CompanyPay set isDeleted ='1' where  isDeleted='0'")
    void deleteComanyPayByIsDelted();

    @Query("from CompanyPay where isDeleted='0' ")
    CompanyPay queryCompyPayByIsDelted();

}
