package com.inflearn.springsecurityiml.domain;

import javax.persistence.Embeddable;

import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
public class Role {

	private String role;

	public Role(String role) {
		this.role = role;
	}

	@Override
	public String toString() {
		return role;
	}
}
