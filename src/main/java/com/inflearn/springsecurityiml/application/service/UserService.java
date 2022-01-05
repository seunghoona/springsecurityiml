package com.inflearn.springsecurityiml.application.service;

import com.inflearn.springsecurityiml.application.service.dto.UserDto;
import com.inflearn.springsecurityiml.domain.Account;
import com.inflearn.springsecurityiml.domain.AccountDto;
import java.util.List;

public interface UserService {
	void createUser(AccountDto accountdto);

	List<Account> getUsers();
	UserDto getUser(Long id);

	void deleteUser(Long idx);
}
