package com.inflearn.springsecurityiml.application.service;

import com.inflearn.springsecurityiml.application.service.dto.UserDto;
import java.util.List;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;

import com.inflearn.springsecurityiml.annotation.DomainService;
import com.inflearn.springsecurityiml.domain.Account;
import com.inflearn.springsecurityiml.domain.AccountDto;
import com.inflearn.springsecurityiml.domain.PasswordService;
import com.inflearn.springsecurityiml.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;

@DomainService
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final PasswordService passwordService;

	public void createUser(AccountDto accountDto) {
		userRepository.save(new Account(accountDto, passwordService));
	}

	@Transactional
	public UserDto getUser(Long id) {
		Account account = userRepository.findById(id).orElse(null);
		ModelMapper modelMapper = new ModelMapper();
		UserDto userDto = modelMapper.map(account, UserDto.class);

		List<String> roles = account.getUserRoles()
			.stream()
			.map(role -> role.getRoleName())
			.collect(Collectors.toList());

		userDto.setRoles(roles);
		return userDto;
	}

	@Transactional
	public List<Account> getUsers() {
		return userRepository.findAll();
	}

	@Override
	public void deleteUser(Long id) {
		userRepository.deleteById(id);
	}
	
	@Secured("ROLE_MANAGER")
	public void order() {
		System.out.println("ORDER 호출");
	}
}
