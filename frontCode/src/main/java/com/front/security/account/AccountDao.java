package com.front.security.account;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Service;
/**
 * EntityManagerを用いたDAO
 * */
@Service
public class AccountDao {
	@PersistenceContext
	EntityManager entityManager;
	
	public String getName(int id) {
		String testdb = (String)entityManager.createQuery("select username from Account where userid =" + id).getSingleResult();
		return testdb;
	}
	
	public Account getUserData(String username) {
		Query query = entityManager.createQuery("from Account where username = :username");
		query.setParameter("username",username);
		Account userdata = (Account)query.getSingleResult();
		return userdata;
	}
	
}
