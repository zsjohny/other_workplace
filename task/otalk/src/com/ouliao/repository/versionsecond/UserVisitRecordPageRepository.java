package com.ouliao.repository.versionsecond;

import com.ouliao.domain.versionsecond.UserVisitRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by nessary on 16-5-17.
 */
public interface UserVisitRecordPageRepository extends JpaRepository<UserVisitRecord, Long> {
}
