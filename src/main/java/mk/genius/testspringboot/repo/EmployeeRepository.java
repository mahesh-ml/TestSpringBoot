package mk.genius.testspringboot.repo;

import mk.genius.testspringboot.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
   Optional<Employee> findEmployeeByEmail(String email);

   @Query("select e from Employee e where e.firstName = ?1 and e.lastName =?2")
   Employee findByFirstNameAndLastNameJPQL(String firstname, String lastName);

   @Query("select e from Employee e where e.firstName =:firstName and e.lastName =:lastName")
   Employee findByFirstNameAndLastNameJPQLWithNamedParams(@Param("firstName") String firstname, @Param("lastName") String lastName);

   @Query(value = "select * from employees e where e.first_name =?1 and e.last_name =?2", nativeQuery = true)
   Employee findByFirstNameAndLastNameJPQLWithNativeQuery(String firstname, String lastName);
}
