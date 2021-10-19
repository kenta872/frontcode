package com.front.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.front.entity.CodeinfosubEntity;

@Repository
public interface CodeinfosubRepository extends JpaRepository<CodeinfosubEntity,Integer> {

}
