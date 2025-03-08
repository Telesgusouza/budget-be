package com.example.demo.entity;

import java.io.Serializable;

//@Table(name = "tb_transaction")
//@Entity(name = "tb_transaction")
public class Transaction implements Serializable {
//	private static final long serialVersionUID = -9048907067756099806L;
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private UUID id;
//
//	private String img;
//
//	@Column(nullable = false)
//	private String name;
//
//	@Column(nullable = false)
//	private String category;
//
//	@Column(nullable = false)
//	private Instant transferDate;
//
//	@Column(nullable = false, scale = 2)
//	private Float transferValue;
//
//	@ManyToOne
//	@JoinColumn(name = "user_id")
//	private User user;
//
//	public Transaction() {
//	}
//
//	public Transaction(UUID id, String img, String name, Float transferValue, List<UpdateDate> updateDates,
//			String category) {
//		super();
//		this.id = id;
//		this.img = img;
//		this.name = name;
//		this.transferValue = transferValue;
//		this.category = category;
//	}
//
//	public UUID getId() {
//		return id;
//	}
//
//	public void setId(UUID id) {
//		this.id = id;
//	}
//
//	public String getImg() {
//		return img;
//	}
//
//	public void setImg(String img) {
//		this.img = img;
//	}
//
//	public String getName() {
//		return name;
//	}
//
//	public void setName(String name) {
//		this.name = name;
//	}
//
//	public Float getTransferValue() {
//		return transferValue;
//	}
//
//	public void setTransferValue(Float transferValue) {
//		this.transferValue = transferValue;
//	}
//
//	public String getCategory() {
//		return category;
//	}
//
//	public void setCategory(String category) {
//		this.category = category;
//	}
//
//	public void setUser(User user) {
//		this.user = user;
//	}
//
//	@Override
//	public String toString() {
//		return "Transaction [id=" + id + ", img=" + img + ", name=" + name + ", transferValue=" + transferValue
//				+ ", category=" + category + "]";
//	}
//
//	@Override
//	public int hashCode() {
//		return Objects.hash(id);
//	}
//
//	@Override
//	public boolean equals(Object obj) {
//		if (this == obj)
//			return true;
//		if (obj == null)
//			return false;
//		if (getClass() != obj.getClass())
//			return false;
//		Transaction other = (Transaction) obj;
//		return Objects.equals(id, other.id);
//	}

}
