package com.besafx.app.service.sell;

import com.besafx.app.entity.sell.BillSell;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TemporalType;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public interface BillSellService extends PagingAndSortingRepository<BillSell, Long>, JpaSpecificationExecutor<BillSell> {
    BillSell findTopByOrderByCodeDesc();

    BillSell findByCodeAndIdIsNot(Integer code, Long id);

    List<BillSell> findByIdIn(List<Long> ids);

    List<BillSell> findByDateBetween(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);

    List<BillSell> findByDateBetweenAndOrderSellIsNull(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);
    
    List<BillSell> findByDateBetweenAndOrderSellIsNotNull(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);
}
