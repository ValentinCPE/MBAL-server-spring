package com.worldgether.mbal.repository;

import com.worldgether.mbal.model.Family;
import com.worldgether.mbal.model.User;
import org.springframework.data.repository.CrudRepository;

public interface FamilyRepository extends CrudRepository<Family, Long> {

    Family findById(int id_family);

    Family findByName(String name);

}
