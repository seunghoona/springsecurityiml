package com.inflearn.springsecurityiml.application.service;

import com.inflearn.springsecurityiml.domain.Account;
import com.inflearn.springsecurityiml.domain.AccountContext;
import com.inflearn.springsecurityiml.domain.Roles;
import com.inflearn.springsecurityiml.infrastructure.UserRepository;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	private final UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		Optional<Account> findByUsername = userRepository.findByUsername(username);
		Account account = findByUsername.orElseThrow(() -> new UsernameNotFoundException("유저 정보가 존재하지 않습니다."));

		return new AccountContext(account, addAuthorities(account));
	}

	private Collection<? extends GrantedAuthority> addAuthorities(Account account) {
		Set<GrantedAuthority> grantedAuthority = new HashSet<>();
		Set<Roles> userRoles = account.getUserRoles();
		for (Roles userRole : userRoles) {
			grantedAuthority.add(new SimpleGrantedAuthority(userRole.getRoleName()));
		}
		return Collections.unmodifiableSet(grantedAuthority);
	}
}
