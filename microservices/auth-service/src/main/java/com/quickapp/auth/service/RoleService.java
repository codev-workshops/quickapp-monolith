package com.quickapp.auth.service;

import com.quickapp.auth.dto.RoleVM;
import com.quickapp.auth.model.Role;
import com.quickapp.auth.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<RoleVM> getAllRoles() {
        return roleRepository.findAll().stream().map(this::toRoleVM).collect(Collectors.toList());
    }

    public RoleVM getRoleById(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        return toRoleVM(role);
    }

    public RoleVM createRole(RoleVM request) {
        if (roleRepository.existsByName(request.getName())) {
            throw new RuntimeException("Role already exists");
        }

        Role role = new Role();
        role.setName(request.getName());
        role.setDescription(request.getDescription());
        if (request.getPermissions() != null) {
            role.setPermissions(new HashSet<>(request.getPermissions()));
        }

        role = roleRepository.save(role);
        return toRoleVM(role);
    }

    public RoleVM updateRole(String id, RoleVM request) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        if (request.getName() != null) role.setName(request.getName());
        if (request.getDescription() != null) role.setDescription(request.getDescription());
        if (request.getPermissions() != null) {
            role.setPermissions(new HashSet<>(request.getPermissions()));
        }

        role = roleRepository.save(role);
        return toRoleVM(role);
    }

    public void deleteRole(String id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found");
        }
        roleRepository.deleteById(id);
    }

    private RoleVM toRoleVM(Role role) {
        RoleVM vm = new RoleVM();
        vm.setId(role.getId());
        vm.setName(role.getName());
        vm.setDescription(role.getDescription());
        vm.setUsersCount(role.getUsers() != null ? role.getUsers().size() : 0);
        vm.setPermissions(role.getPermissions() != null ? new ArrayList<>(role.getPermissions()) : new ArrayList<>());
        return vm;
    }
}
