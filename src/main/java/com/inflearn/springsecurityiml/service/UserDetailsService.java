package com.inflearn.springsecurityiml.service;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.inflearn.springsecurityiml.domain.Account;
import com.inflearn.springsecurityiml.domain.AccountContext;
import com.inflearn.springsecurityiml.infrastructure.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<Account> findByUsername = userRepository.findByUsername(username);
		Account account = findByUsername.orElseThrow(() -> new UsernameNotFoundException("유저 정보가 존재하지 않습니다."));

		return new AccountContext(account, addAuthorities(account));
	}

	private Collection<? extends GrantedAuthority> addAuthorities(Account account) {
		Set<GrantedAuthority> grantedAuthority = new HashSet<>();
		grantedAuthority.add(new SimpleGrantedAuthority(account.getRole()));
		return Collections.unmodifiableSet(grantedAuthority);
	}
}
