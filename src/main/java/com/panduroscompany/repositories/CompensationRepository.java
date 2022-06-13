package com.panduroscompany.repositories;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.panduroscompany.entity.Compensation;

public interface CompensationRepository extends JpaRepository<Compensation, Long> {
	//To find if an employee with the same first name, middle name, last name and birth date already exists
		@Query("SELECT comp FROM Compensation comp WHERE comp.type = ?1 AND comp.id_employee = ?2")
		public Compensation findExistingSalary(String type, Long id_employee);
	
	//find compensations by start date and end date of X employee
	@Query(value="SELECT id, type, description, idEmployee, date, SUM(amount) as amount, MONTHNAME(date) as monthname, "
			+ "YEAR(date) as yearname FROM Compensation WHERE date >= :startDate AND date < :endDate and idEmployee = :idEmployee "
			+ "GROUP BY yearname, monthname ORDER by date asc", nativeQuery=true)
	List<Compensation> findCompensationByDate(@Param("startDate") Date startDate, @Param("endDate") Date endDate, @Param("idEmployee") int idEmployee);

	//find compensations grouped by year, month and totals of X employee
	@Query(value="SELECT id, type, description, id_employee, datec, SUM(amount) as amount, MONTHNAME(datec) as monthname, "
			+ "YEAR(date) as yearname FROM Compensation WHERE id_employee = :id_employee "
			+ "GROUP BY yearname, monthname ORDER by date asc", nativeQuery=true)
	List<Compensation> findCompensationsByEmployeeId(@Param("id_employee") Long id_employee);	
	
	//get the global total 
	@Query(value="SELECT SUM(amount) as total FROM Compensation WHERE id_employee = :id_employee", nativeQuery=true)
	Float getTotal(@Param("id_employee") Long id_employee);
	
	//get compensations by month
	@Query(value="SELECT * FROM Compensation WHERE MONTHNAME(date) = :month and idEmployee = :idEmployee and YEAR(date) = :year", nativeQuery=true)
	List<Compensation> findByMonth(@Param("idEmployee") int idEmployee, @Param("month") String month, @Param("year") int year);
}
