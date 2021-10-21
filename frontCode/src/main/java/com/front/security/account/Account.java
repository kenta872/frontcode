package com.front.security.account;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="account", schema="v1")
public class Account implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@Column
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int userid;
	
	@Column
	private String username;
	
	@Column
	private String password;

	//この下はインターフェースのメソッドを実装する
	
	//ユーザーに与えられる権限リストを返却するメソッド
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities(){
		return null;
	}
	
	//ユーザー名を返却するメソッド
	@Override
	public String getUsername() {
		return this.username;
	}
	
	//パスワードを返却するメソッド
	@Override
	public String getPassword() {
		return this.password;
	}

	//アカウントの有効期限の状態を判定するメソッド
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	//アカウントのロック状態を判定するメソッド
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	
	//資格情報の有効期限の状態を判定するメソッド
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	//有効なユーザーか判定するメソッド
	@Override
	public boolean isEnabled() {
		return true;
	}
}