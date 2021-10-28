package com.front.security.account;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AccountService implements UserDetailsService {
	@PersistenceContext
	EntityManager entityManager;
	@Autowired
	AccountRepository accountRepos;
    @Autowired
    private AccountRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		//データベースからアカウント情報を検索
		Account account = getUserData(username);
		return account;
	}
	

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
	
	
	
	public void insertAccount(String username, String pass, String mail) {
		
		String hashPass = bCryptPasswordEncoder.encode(pass);
		
		Account newAccount = new Account();
		newAccount.setUsername(username);
		newAccount.setPassword(hashPass);
		newAccount.setMail(mail);
		newAccount.setRole(newAccount.getRoleUser());
		accountRepos.saveAndFlush(newAccount);
		logger.info("アカウントを登録しました：" + newAccount.getUsername());
	}

}
