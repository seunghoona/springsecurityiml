package com.inflearn.springsecurityiml.service;

import com.inflearn.springsecurityiml.domain.Account;
import com.inflearn.springsecurityiml.domain.AccountDto;

public interface UserService {
	void createUser(AccountDto account);
}
