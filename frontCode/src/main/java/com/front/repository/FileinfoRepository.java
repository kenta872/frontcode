package com.front.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.front.entity.FileinfoEntity;

@Repository
public interface FileinfoRepository extends JpaRepository<FileinfoEntity,Integer> {

}
