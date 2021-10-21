package com.front.controller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.front.controller.entity.PostinfoEntity;

@Repository
public interface PostinfoRepository extends JpaRepository<PostinfoEntity,Integer> {

}
