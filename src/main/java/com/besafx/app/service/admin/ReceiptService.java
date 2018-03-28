package com.besafx.app.service.admin;

import com.besafx.app.entity.admin.Receipt;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public interface ReceiptService extends PagingAndSortingRepository<Receipt, Long>, JpaSpecificationExecutor<Receipt> {
    Receipt findTopByOrderByCodeDesc();

    Receipt findByCodeAndIdIsNot(Long code, Long id);
}

