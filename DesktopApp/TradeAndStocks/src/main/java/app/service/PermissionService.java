package app.service;

import app.model.Permission;
import app.model.PermissionEnum;
import app.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    @Autowired
    private PermissionRepository permissionRepository;

    public Permission getPermissionByEnum(PermissionEnum name) {
        return permissionRepository.findFirstByName(name);
    }
}
