package com.panduroscompany.repositories;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.panduroscompany.entity.Compensation;

public interface CompensationRepository extends JpaRepository<Compensation, Long> {
	//To find if an employee with the same first name, middle name, last name and birth date already exists
	@Query("SELECT comp FROM Compensation comp WHERE comp.type = ?1 AND comp.id_employee = ?2")
	public Compensation findExistingSalary(String type, Long id_employee);
	
	/*Extract no lo devuleve con nombre del mes - invalid sintaxis*/
	@Query(value="SELECT id, type, description, datec, id_employee, SUM(amount) AS amount,"
			+ "YEAR(datec) AS year, MONTHNAME(datec) AS month "//monthname to convert month number to month name
			+ "FROM Compensation WHERE id_employee = :id_employee AND "
			+ "GROUP BY year, month ORDER by datec asc", nativeQuery=true)
	List<Compensation> findCompensationsById(@Param("id_employee") Long id_employee);
	
	/*https://community.claris.com/en/s/question/0D50H00006h8yYuSAI/sql-how-to-order-by-calc-number-type
	 * https://stackoverflow.com/questions/17271316/order-by-month-and-year-in-sql-with-sum*/
	
	//Get all employee compensation with id_employee	
	/*@Query(value="SELECT id, type, description, datec, id_employee, SUM(amount) as amount, MONTHNAME(datec) as monthname, YEAR(datec) as year "
				+ "FROM Compensation WHERE id_employee = :id_employee "
				+ "GROUP BY year, monthname ORDER by datec asc", nativeQuery=true)*/
}
