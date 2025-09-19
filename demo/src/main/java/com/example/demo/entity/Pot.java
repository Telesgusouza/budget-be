package com.example.demo.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

@Table(name = "tb_pot")
@Entity(name = "tb_pot")
public class Pot implements Serializable {
	private static final long serialVersionUID = -7998685760951034718L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	@Column(nullable = false)
	private String title;

	@Column(nullable = false)
	private String description;

	@Column(nullable = false)
	private Float monthlyAmount;

	@Column(nullable = false)
	private String color;

	@OneToMany(mappedBy = "pot", cascade = CascadeType.ALL, orphanRemoval = true)
	@JsonManagedReference
	private List<UpdateDate> update = new ArrayList();

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonBackReference
	private User user;

	public Pot() {
	}

	public Pot(UUID id, String title, String description, Float monthlyAmount, String color, List<UpdateDate> update) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.monthlyAmount = monthlyAmount;
		this.color = color;
		this.update = update;
	}

	public Pot(UUID id, String title, String description, Float monthlyAmount, String color, List<UpdateDate> update,
			User user) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.monthlyAmount = monthlyAmount;
		this.color = color;
		this.update = update;
		this.user = user;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@Override
	public String toString() {
		return "Pot [id=" + id + ", title=" + title + ", description=" + description + ", monthlyAmount="
				+ monthlyAmount + ", color=" + color + ", update=" + update + "]";
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
		Pot other = (Pot) obj;
		return Objects.equals(id, other.id);
	}

}
