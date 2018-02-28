package com.worldgether.mbal.repository;

import com.worldgether.mbal.model.Role;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RoleRepository extends CrudRepository<Role, Long> {

    Role findByName(String name);

    List<Role> findAllByName(String name);

}
