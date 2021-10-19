package com.front.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.front.entity.CodeinfoEntity;

@Repository
public interface CodeinfoRepository extends JpaRepository<CodeinfoEntity,Integer> {

}
