package com.example.demo;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import java.util.ArrayList;
import java.util.Optional;

import com.example.data.Role;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getAllRoles() {
        List<Role> userRecords = new ArrayList<>();
        roleRepository.findAll().forEach(userRecords::add);
        return userRecords;
    }

    public void addRole(Role role) {
        roleRepository.save(role);
    }

    public Optional<Role> getRole(int id) {
        return roleRepository.findById(id);
    }

    public Role getRoleByName(String name) {
        return roleRepository.findByName(name);
    }
    
}
