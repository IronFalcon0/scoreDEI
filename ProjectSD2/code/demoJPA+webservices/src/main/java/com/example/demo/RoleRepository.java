package com.example.demo;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import com.example.data.Role;

public interface RoleRepository extends CrudRepository<Role, Integer>  {
    @Query("SELECT r FROM Role r WHERE r.name = :name")
    public Role findByName(@Param("name") String name);
}
