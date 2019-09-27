package com.hs.pojo;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * users实体类
 * @author Hooxm
 *
 */
@Table(name="users")
public class Users implements Serializable{

	@Id
	private String id;//id


	
	private String username;//username
	private String passwd;//passwd
	private String email;//email
	private String usertype;//usertype

	public Users() {
	}

	public Users(String username, String passwd, String email, String usertype) {
		this.username = username;
		this.passwd = passwd;
		this.email = email;
		this.usertype = usertype;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}
	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsertype() {
		return usertype;
	}
	public void setUsertype(String usertype) {
		this.usertype = usertype;
	}


	
}
