package com.besafx.app.service.sell;

import com.besafx.app.entity.sell.OrderSell;
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
public interface OrderSellService extends PagingAndSortingRepository<OrderSell, Long>, JpaSpecificationExecutor<OrderSell> {
    OrderSell findTopByOrderByCodeDesc();

    OrderSell findByCodeAndIdIsNot(Integer code, Long id);

    List<OrderSell> findByIdIn(List<Long> ids);

    List<OrderSell> findByDateBetween(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);
}
