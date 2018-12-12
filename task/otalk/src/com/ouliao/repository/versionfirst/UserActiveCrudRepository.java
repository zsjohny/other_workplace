package com.ouliao.repository.versionfirst;

/**
 * Created by nessary on 16-5-10.
 */

import com.ouliao.domain.versionfirst.UserActive;
import org.springframework.data.repository.CrudRepository;

public interface UserActiveCrudRepository extends CrudRepository<UserActive, Long> {
}
