package com.inflearn.springsecurityiml.application.service;

import com.inflearn.springsecurityiml.domain.Account;
import com.inflearn.springsecurityiml.domain.AccountDto;
import java.util.List;

public interface UserService {
	void createUser(AccountDto account);
	List<Account> getUsers();
	void deleteUser(Long idx);
}
