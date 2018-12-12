package com.ouliao.repository.versionsecond;

import com.ouliao.domain.versionsecond.UserMissCallRecord;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by nessary on 16-5-14.
 */
public interface UserMissCallRecordCrudRepository extends JpaRepository<UserMissCallRecord, Long> {
}
