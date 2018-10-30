package app.service;

import app.model.Permission;
import app.model.PermissionEnum;
import app.repository.PermissionRepository;

public class PermissionService {
    private static boolean initialized = false;
    private static PermissionRepository permissionRepository;
    public static void initialize(PermissionRepository repo) {
        if(!initialized) {
            permissionRepository = repo;
            permissionRepository.save(new Permission(PermissionEnum.admin));
            permissionRepository.save(new Permission(PermissionEnum.client));
            initialized = true;
        }
    }

    public static PermissionRepository getRepo() {
        return permissionRepository;
    }
}
