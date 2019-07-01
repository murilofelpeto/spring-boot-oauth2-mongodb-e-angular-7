package br.com.murilo.domain;

import java.io.Serializable;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document
public class Role implements Serializable, GrantedAuthority {

	private static final long serialVersionUID = 1L;
	
	@Id
	private String id;
	private String roleName;
	
	public Role(String id, String roleName) {
		this.id = id;
		this.roleName = roleName;
	}
	
	public Role(String roleName) {
		this.roleName = roleName;
	}
	
	public Role() {}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String getAuthority() {
		return this.roleName;
	}
}
