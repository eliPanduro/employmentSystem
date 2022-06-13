package com.panduroscompany.entity;

import java.sql.Date;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "compensation")
public class Compensation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(nullable = false)
	private String type;

	@Column(nullable = false)
	private double amount;

	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	private Date datec;

	public Long id_employee;

	public Compensation(Long id, String type, double amount, String description, Date datec, Long id_employee) {
		super();
		this.id = id;
		this.type = type;
		this.amount = amount;
		this.description = description;
		this.datec = datec;
		this.id_employee = id_employee;
	}

	public Compensation() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDatec() {
		return datec;
	}

	public void setDatec(Date datec) {
		this.datec = datec;
	}

	public Long getId_employee() {
		return id_employee;
	}

	public void setId_employee(Long id_employee) {
		this.id_employee = id_employee;
	}

	public Optional<Compensation> findById(Long idCompensation) {
		return null;
	}

}
