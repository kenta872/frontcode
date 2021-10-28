package com.front.controller.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.front.controller.entity.FavoriteEntity;

@Repository
public interface FavoriteinfoRepository extends JpaRepository<FavoriteEntity,Integer> {

}
