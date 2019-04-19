package com.jiuy.core.service.admin;

import java.util.List;

public interface RoleAuthorityService {

	int remove(long roleId);

	int add(long roleId, List<Long> authorityIds);


}
