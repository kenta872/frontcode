package com.front.controller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.front.controller.entity.FileinfosubEntity;

@Repository
public interface FileinfosubRepository extends JpaRepository<FileinfosubEntity,Integer> {

}
