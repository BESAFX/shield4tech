package com.besafx.app.service.hr;

import com.besafx.app.entity.hr.Employee;
import com.besafx.app.entity.admin.Person;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface EmployeeService extends PagingAndSortingRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    Employee findTopByOrderByCodeDesc();

    Employee findByCodeAndIdIsNot(Integer code, Long id);

    Employee findByPerson(Person person);
}

