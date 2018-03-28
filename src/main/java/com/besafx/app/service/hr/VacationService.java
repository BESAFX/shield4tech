package com.besafx.app.service.hr;

import com.besafx.app.entity.hr.Vacation;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface VacationService extends PagingAndSortingRepository<Vacation, Long>, JpaSpecificationExecutor<Vacation> {
}
