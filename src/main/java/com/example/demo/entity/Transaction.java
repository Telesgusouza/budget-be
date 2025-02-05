package com.example.demo.entity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Table(name = "tb_transaction")
@Entity(name = "tb_transaction")
public class Transaction implements Serializable {
	private static final long serialVersionUID = -9048907067756099806L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private UUID id;

	private String img;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, scale = 2)
	private Float transferValue;

	@OneToMany(mappedBy = "transaction", fetch = FetchType.EAGER)
	private List<UpdateDate> updateDates;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	public Transaction() {
	}

	public Transaction(UUID id, String img, String name, Float transferValue, List<UpdateDate> updateDates) {
		super();
		this.id = id;
		this.img = img;
		this.name = name;
		this.transferValue = transferValue;
		this.updateDates = updateDates;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Float getTransferValue() {
		return transferValue;
	}

	public void setTransferValue(Float transferValue) {
		this.transferValue = transferValue;
	}

	public List<UpdateDate> getUpdateDates() {
		return updateDates;
	}

	public void setUpdateDates(List<UpdateDate> updateDates) {
		this.updateDates = updateDates;
	}

	@Override
	public String toString() {
		return "Transaction [id=" + id + ", img=" + img + ", name=" + name + ", transferValue=" + transferValue
				+ ", updateDates=" + updateDates + "]";
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
		Transaction other = (Transaction) obj;
		return Objects.equals(id, other.id);
	}

}
