package com.besafx.app.service.admin;

import com.besafx.app.entity.admin.Attach;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface AttachService extends PagingAndSortingRepository<Attach, Long>, JpaSpecificationExecutor<Attach> {

}
