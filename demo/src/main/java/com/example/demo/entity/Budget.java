package com.example.demo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity(name = "db_budget")
@Table(name = "db_budget")
public class Budget implements Serializable {
	private static final long serialVersionUID = 6519736677877770708L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private Float monthlyAmount;

	@OneToMany(mappedBy = "budget", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<UpdateDate> update = new ArrayList();

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;

	public Budget() {
	}

	public Budget(UUID id, String title, String description, Float monthlyAmount) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.monthlyAmount = monthlyAmount;
	}

	public Budget(UUID id, String title, String description, Float monthlyAmount, List<UpdateDate> update) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.monthlyAmount = monthlyAmount;
		this.update = update;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Float getMonthlyAmount() {
		return monthlyAmount;
	}

	public void setMonthlyAmount(Float monthlyAmount) {
		this.monthlyAmount = monthlyAmount;
	}

	public List<UpdateDate> getUpdate() {
		return update;
	}

	public void setUpdate(List<UpdateDate> update) {
		this.update = update;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Budget [id=" + id + ", title=" + title + ", description=" + description + ", monthlyAmount="
				+ monthlyAmount + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Budget other = (Budget) obj;
		return Objects.equals(id, other.id);
	}

}
