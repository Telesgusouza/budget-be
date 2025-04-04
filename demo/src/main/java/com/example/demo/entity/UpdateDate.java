package com.example.demo.entity;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Table(name = "tb_update_date")
@Entity(name = "tb_update_date")
public class UpdateDate implements Serializable {
	private static final long serialVersionUID = -6861959975761079593L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;
	
	@Column(nullable = false)
	private Instant date;
	
	@Column(nullable = false, scale = 2)
	private Float value;
	
	// connections
	@ManyToOne
	@JoinColumn(name = "pot_id")
	private Pot pot; 

	public UpdateDate() {
	}
	
	public UpdateDate(UUID id, Instant date, Float value) {
		super();
		this.id = id;
		this.date = date;
		this.value = value;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "UpdateDate [id=" + id + ", date=" + date + ", value=" + value + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(date, id, value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UpdateDate other = (UpdateDate) obj;
		return Objects.equals(date, other.date) && Objects.equals(id, other.id) && Objects.equals(value, other.value);
	}

}

































