package com.inflearn.springsecurityiml.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.inflearn.springsecurityiml.domain.Account;

public class AccountContext extends User {

	private Account account;

	public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
		super(account.getUsername(), String.valueOf(account.getPassword()), authorities);
	}

}
