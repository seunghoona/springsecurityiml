package com.inflearn.springsecurityiml.domain;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class AccountContext extends User {

	private Account account;

	private String groupInfo;

	public AccountContext(Account account, Collection<? extends GrantedAuthority> authorities) {
		super(account.getUsername(), String.valueOf(account.getPassword()), authorities);
	}

	public Account getAccount() {
		return account;
	}

	public String getGroupInfo() {
		return "TL";
	}
}
