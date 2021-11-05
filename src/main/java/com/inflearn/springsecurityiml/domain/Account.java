package com.inflearn.springsecurityiml.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Account {

	@Id
	@GeneratedValue
	private Long id;

	@Column
	@Getter(value = AccessLevel.PACKAGE)
	private String username;

	@Embedded
	@Getter(value = AccessLevel.PACKAGE)
	private Password password;

	@Column
	private String email;

	@Column
	private String age;

	@Getter(value = AccessLevel.PUBLIC)
	@Embedded
	private Role role;

	public Account(AccountDto accountDto, PasswordService passwordService) {
		this.id = accountDto.getId();
		this.username = accountDto.getUsername();
		this.password = passwordService.encode(new Password(accountDto.getPassword()));
		this.email = accountDto.getEmail();
		this.age = accountDto.getAge();
		this.role = new Role(accountDto.getRole());
	}

}
