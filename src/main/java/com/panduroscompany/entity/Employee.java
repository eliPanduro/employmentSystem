package com.panduroscompany.entity;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Entity
@Table(name = "employee")
public class Employee{

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotEmpty(message="First name is required")
	@Pattern(regexp="[a-zA-Z ]{2,50}", message="First name must contain letters and/or space")
	@Column(nullable = false)
	private String firstname;
	
	@Pattern(regexp="[a-zA-Z ]{0,50}", message="Middle name Must contain letters and/or space")
	private String middlename;

	@NotEmpty(message="Last name is required")
	@Pattern(regexp="[a-zA-Z ]{2,50}", message="Last name must contain letters and/or space")
	@Column(nullable = false)
	private String lastname;

	@Column(nullable = false)
	private Date birthdate;

	@NotEmpty(message="Position is required")
	@Pattern(regexp="[a-zA-Z ]{2,50}", message="Position must contain letters and/or space")
	@Column(nullable = false)
	private String position;

	public Employee(Long id, String firstname, String middlename, String lastname, Date birthdate, String position) {
		super();
		this.id = id;
		this.firstname = firstname;
		this.middlename = middlename;
		this.lastname = lastname;
		this.birthdate = birthdate;
		this.position = position;
	}
	
	public Employee() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getMiddlename() {
		return middlename;
	}

	public void setMiddlename(String middlename) {
		this.middlename = middlename;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	@Override
	public String toString() {
		return "Employee [id=" + id + ", firstname=" + firstname + ", middlename=" + middlename + ", lastname="
				+ lastname + ", birthdate=" + birthdate + ", position=" + position + "]";
	}
}
