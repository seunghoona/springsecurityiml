package com.inflearn.springsecurityiml.domain;

import javax.persistence.Embeddable;

@Embeddable
public class Password implements CharSequence {

	private String password;

	public Password() {
	}

	public Password(String password) {
		this.password = password;
	}

	@Override
	public int length() {
		return password.length();
	}

	@Override
	public char charAt(int index) {
		return password.charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return password.subSequence(start, end);
	}

	@Override
	public String toString() {
		return password;
	}
}
