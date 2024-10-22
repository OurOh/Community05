package net.musecom.community.model;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

public class CustomUserDetails implements UserDetails {

	private Member member;
	private Collection<? extends GrantedAuthority> authorities;
	
	public CustomUserDetails(Member member, Collection<MemberRole> roles) {
		this.member = member;
		this.authorities = roles.stream()
				                .map(role -> new SimpleGrantedAuthority(role.getUserRole()))
				                .collect(Collectors.toList());
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return member.getUserpass();
	}

	@Override
	public String getUsername() {
		return member.getUserid();
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	public Member getMember() {
		return member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = authorities;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
    	
}
