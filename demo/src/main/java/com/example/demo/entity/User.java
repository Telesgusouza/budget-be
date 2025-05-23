package com.example.demo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.example.demo.enums.UserRole;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "tb_user")
@Entity(name = "tb_user")
public class User implements UserDetails, Serializable {
	private static final long serialVersionUID = -3324584769246650362L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(nullable = false)
	private String img;

	@Column(nullable = false)
	private String login;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private UserRole role;

//	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//	@BatchSize(size = 10)
//	private List<Transaction> transaction = new ArrayList<>();
//
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@BatchSize(size = 10)
	private List<Pot> pots = new ArrayList<>();

	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, orphanRemoval = true)
	@BatchSize(size = 10)
	private List<Friend> friends = new ArrayList<>();

//	@OneToOne(mappedBy = "user", fetch = FetchType.LAZY)
//	private Account account;
//
	@OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	@BatchSize(size = 10)
	private List<Budget> budget;

	public User() {
	}

	public User(UUID id, String img, String login, String password, String name, UserRole role) {
		super();
		this.id = id;
		this.img = img;
		this.login = login;
		this.password = password;
		this.name = name;
		this.role = role;
	}

//	public User(UUID id, String img, String login, String password, String name, UserRole role,
//			List<Transaction> transaction, List<Pot> pots, Account account, Budget budget) {
//		super();
//		this.id = id;
//		this.img = img;
//		this.login = login;
//		this.password = password;
//		this.name = name;
//		this.role = role;
//		this.transaction = transaction;
//		this.pots = pots;
//		this.account = account;
//		this.budget = budget;
//	}

	public User(UUID id, String img, String login, String password, String name, UserRole role, List<Pot> pots,
			List<Friend> friends) {
		super();
		this.id = id;
		this.img = img;
		this.login = login;
		this.password = password;
		this.name = name;
		this.role = role;
		this.pots = pots;
		this.friends = friends;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		if (this.role == UserRole.ADMIN)
			return List.of(new SimpleGrantedAuthority("ROLE_ADMIN"), new SimpleGrantedAuthority("ROLE_USER"));

		return List.of(new SimpleGrantedAuthority("ROLE_USER"));
	}

	@Override
	public String getPassword() {

		return this.password;
	}

	@Override
	public String getUsername() {

		return this.login;
	}

	public void setlogin(String login) {
		this.login = login;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
//
//	public List<Transaction> getTransaction() {
//		return transaction;
//	}
//
//	public void setTransaction(List<Transaction> transaction) {
//		this.transaction = transaction;
//	}

	public List<Pot> getPots() {
		return pots;
	}

	public void setPots(List<Pot> pots) {
		this.pots = pots;
	}

//	public Account getAccount() {
//		return account;
//	}
//
//	public void setAccount(Account account) {
//		this.account = account;
//	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Budget> getBudget() {
		return budget;
	}

	public void setBudget(List<Budget> budget) {
		this.budget = budget;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public List<Friend> getFriends() {
		return friends;
	}

	public void setFriends(List<Friend> friends) {
		this.friends = friends;
	}

//	@Override
//	public String toString() {
//		return "User [id=" + id + ", img=" + img + ", login=" + login + ", password=" + password + ", name=" + name
//				+ ", role=" + role + ", transaction=" + transaction + ", pots=" + pots + ", friends=" + friends
//				+ ", account=" + account + ", budget=" + budget + "]";
//	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", img=" + img + ", login=" + login + ", password=" + password + ", name=" + name
				+ ", role=" + role + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		return Objects.equals(id, other.id);
	}

}
