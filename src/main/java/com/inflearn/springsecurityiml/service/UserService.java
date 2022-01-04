package com.inflearn.springsecurityiml.service;

import com.inflearn.springsecurityiml.domain.Account;
import com.inflearn.springsecurityiml.domain.AccountDto;
import com.inflearn.springsecurityiml.service.dto.UserDto;
import java.util.List;

public interface UserService {
	void createUser(AccountDto account);
	List<Account> getUsers();
	void deleteUser(Long idx);
}
