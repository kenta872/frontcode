package com.front.controller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.front.controller.entity.PostinfosubEntity;

@Repository
public interface PostinfosubRepository extends JpaRepository<PostinfosubEntity,Integer> {

}
