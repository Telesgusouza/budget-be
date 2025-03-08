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

//@Entity(name = "tb_account")
public class Account implements Serializable {
//
//	private static final long serialVersionUID = -7812602004043649113L;
//
//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private UUID id;
//
//	@Column(nullable = false, scale = 2)
//	private Float currentBalance;
//
//	@Column(nullable = false, scale = 2)
//	private Float income;
//
//	@Column(nullable = false, scale = 2)
//	private Float expenses;
//
//	@OneToMany(mappedBy = "old_current_balance", fetch = FetchType.EAGER)
//	private List<UpdateDate> oldCurrentBalance;
//
//	@OneToMany(mappedBy = "old_income", fetch = FetchType.EAGER)
//	private List<UpdateDate> oldIncome;
//
//	@OneToMany(mappedBy = "old_expenses", fetch = FetchType.EAGER)
//	private List<UpdateDate> oldExpenses;
//	
//	@ManyToOne
//	@JoinColumn(name = "user_id")
//	private User user;
//
//	public Account() {
//	}
//
//	public Account(UUID id, Float currentBalance, Float income, Float expenses, List<UpdateDate> oldCurrentBalance,
//			List<UpdateDate> oldIncome, List<UpdateDate> oldExpenses) {
//		super();
//		this.id = id;
//		this.currentBalance = currentBalance;
//		this.income = income;
//		this.expenses = expenses;
//		this.oldCurrentBalance = oldCurrentBalance;
//		this.oldIncome = oldIncome;
//		this.oldExpenses = oldExpenses;
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
//	public Float getCurrentBalance() {
//		return currentBalance;
//	}
//
//	public void setCurrentBalance(Float currentBalance) {
//		this.currentBalance = currentBalance;
//	}
//
//	public Float getIncome() {
//		return income;
//	}
//
//	public void setIncome(Float income) {
//		this.income = income;
//	}
//
//	public Float getExpenses() {
//		return expenses;
//	}
//
//	public void setExpenses(Float expenses) {
//		this.expenses = expenses;
//	}
//
//	public List<UpdateDate> getOldCurrentBalance() {
//		return oldCurrentBalance;
//	}
//
//	public void setOldCurrentBalance(List<UpdateDate> oldCurrentBalance) {
//		this.oldCurrentBalance = oldCurrentBalance;
//	}
//
//	public List<UpdateDate> getOldIncome() {
//		return oldIncome;
//	}
//
//	public void setOldIncome(List<UpdateDate> oldIncome) {
//		this.oldIncome = oldIncome;
//	}
//
//	public List<UpdateDate> getOldExpenses() {
//		return oldExpenses;
//	}
//
//	public void setOldExpenses(List<UpdateDate> oldExpenses) {
//		this.oldExpenses = oldExpenses;
//	}
//
//	@Override
//	public String toString() {
//		return "Account [id=" + id + ", currentBalance=" + currentBalance + ", income=" + income + ", expenses="
//				+ expenses + ", oldCurrentBalance=" + oldCurrentBalance + ", oldIncome=" + oldIncome + ", oldExpenses="
//				+ oldExpenses + "]";
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
//		Account other = (Account) obj;
//		return Objects.equals(id, other.id);
//	}

}
