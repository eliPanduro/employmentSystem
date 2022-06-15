package com.panduroscompany.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.panduroscompany.entity.Compensation;

public interface CompensationRepository extends JpaRepository<Compensation, Long> {
	// To find if an employee with the same first name, middle name, last name and birth date already exists
	@Query("SELECT comp FROM Compensation comp WHERE comp.type = ?1 AND comp.id_employee = ?2")
	public Compensation findExistingSalary(String type, Long id_employee);

	/* Extract no lo devuleve con nombre del mes - invalid sintaxis */

	// To find all the compensations for that employee
	@Query(value = "SELECT id, type, description, datec, id_employee, SUM(amount) AS amount,"
			+ "YEAR(datec) AS year, MONTHNAME(datec) AS month "// monthname to convert month number to month name
			+ "FROM Compensation WHERE id_employee = :id_employee "
			+ "GROUP BY year, month ORDER BY datec ASC", nativeQuery = true)
	List<Compensation> findCompensationsById(@Param("id_employee") Long id_employee);

	// To find compensations for a date range
	@Query(value = "SELECT id, type, description, id_employee, datec, SUM(amount) AS amount,"
			+ "YEAR(datec) AS year, MONTHNAME(datec) AS month "
			+ "FROM Compensation WHERE id_employee = :id_employee AND datec BETWEEN :startDate AND :endDate "
			+ "GROUP BY year, month ORDER BY datec ASC", nativeQuery = true)
	public List<Compensation> findCompensationByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate,
			@Param("id_employee") Long id_employee);

	//To show the details for a specific month
	@Query(value = "SELECT * FROM Compensation WHERE MONTH(datec) = :month AND YEAR(datec) = :year "
			+ "AND id_employee = :id", nativeQuery = true)
	public List<Compensation> findDetailsByMonth(Long id, String month, int year);
}
