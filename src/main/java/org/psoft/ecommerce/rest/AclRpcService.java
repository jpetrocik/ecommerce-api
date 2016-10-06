package org.psoft.ecommerce.rest;

import org.psoft.ecommerce.service.AclService;

public class AclRpcService {

	AclService aclService;

	public void addGroup(Long userId, Long groupId) {
		aclService.addGroup(userId, groupId);
	}

	public void removeGroup(Long userId, Long groupId) {
		aclService.removeGroup(userId, groupId);
	}
	
	
	
}
