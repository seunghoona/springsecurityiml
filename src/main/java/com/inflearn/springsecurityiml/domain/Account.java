package com.inflearn.springsecurityiml.domain;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@AllArgsConstructor
@Builder
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String username;

	@Embedded
	private Password password;

	@Column
	private String email;

	@Column
	private int age;

	@ManyToMany(fetch = FetchType.LAZY, cascade={CascadeType.ALL})
	@JoinTable(name = "account_roles", joinColumns = { @JoinColumn(name = "user_id") }, inverseJoinColumns = {
		@JoinColumn(name = "role_id") })
	@Builder.Default
	private Set<Roles> userRoles = new HashSet<>();

	public Account(AccountDto accountDto, PasswordService passwordService) {
		this.id = accountDto.getId();
		this.username = accountDto.getUsername();
		this.password = passwordService.encode(new Password(accountDto.getPassword()));
		this.email = accountDto.getEmail();
		this.age = accountDto.getAge();
	}

}
