package com.example.demo.repositories;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Pot;
import com.example.demo.entity.UpdateDate;

public interface UpdateDateRepository extends JpaRepository<UpdateDate, UUID>, JpaSpecificationExecutor<UpdateDate> {

	Optional<UpdateDate> findTopByPotOrderByDateDesc(Pot pot);

}



/*
 
 
 
 
 
esse erro surgiu depoisque eu adicionei o seguinte código
 
public interface UpdateDateRepository extends JpaRepository<UpdateDate, UUID>, JpaSpecificationExecutor<UpdateDate> {
	
	@Query("SELECT u FROM tb_update_date u WHERE u.tb_pot = :tb_pot ORDER BY u.date DESC")
	UpdateDate findLatestUpdate(@Param("pot") Pot pot);

}

restante do código está assim
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
	
	....
	
	public List<UpdateDate> getUpdate() {
		return update;
	}
	
	public void setUpdate(List<UpdateDate> update) {
		this.update = update;
	}
	....
}


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
	@JsonBackReference
	private Pot pot;

	@ManyToOne
	@JoinColumn(name = "budget_id")
	@JsonBackReference
	private Budget budget;

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

	public void setPot(Pot pot) {
		this.pot = pot;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
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

  
 */

















