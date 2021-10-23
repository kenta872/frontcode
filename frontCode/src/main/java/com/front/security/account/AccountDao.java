package com.front.security.account;

import java.util.List;

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
	
	/**
	 * IDによるアカウント検索
	 * @param id アカウントID
	 * @return アカウントネーム
	 */
	public String getName(int id) {
		String testdb = (String)entityManager.createQuery("select username from Account where userid =" + id).getSingleResult();
		return testdb;
	}
	
	/**
	 * ユーザーネームによるアカウント検索
	 * @param username アカウント情報
	 * @return
	 */
	public Account getUserData(String username) {
		Query query = entityManager.createQuery("from Account where username = :username");
		query.setParameter("username",username);
		Account userdata = (Account)query.getSingleResult();
		return userdata;
	}
	
	
	
	public List<Account> findAccountByName(String username) {
		Query query = entityManager.createQuery("from Account where username = '" + username + "'");
		return query.getResultList();
	}
	/**
	 * メールアドレスによるアカウント検索
	 * @param mail メールアドレス
	 * @return アカウント情報
	 */
	public List<Account> findAccountByMail(String mail) {
		Query query = entityManager.createQuery("from Account where mail = '" + mail + "'");
		return query.getResultList();
	}
	
	
	
}
