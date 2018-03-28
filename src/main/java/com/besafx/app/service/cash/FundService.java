package com.besafx.app.service.cash;

import com.besafx.app.entity.cash.Fund;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface FundService extends PagingAndSortingRepository<Fund, Long>, JpaSpecificationExecutor<Fund> {
    Fund findFirstBy();
}

