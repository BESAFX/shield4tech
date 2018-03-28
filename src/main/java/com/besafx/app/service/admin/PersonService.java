package com.besafx.app.service.admin;

import com.besafx.app.entity.admin.Person;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface PersonService extends PagingAndSortingRepository<Person, Long>, JpaSpecificationExecutor<Person> {
    Person findByEmail(String email);
}

