package com.besafx.app.service.buy;

import com.besafx.app.entity.buy.OrderBuy;
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
public interface OrderBuyService extends PagingAndSortingRepository<OrderBuy, Long>, JpaSpecificationExecutor<OrderBuy> {
    OrderBuy findTopByOrderByCodeDesc();

    OrderBuy findByCodeAndIdIsNot(Integer code, Long id);

    List<OrderBuy> findByIdIn(List<Long> ids);

    List<OrderBuy> findByDateBetween(@Temporal(TemporalType.TIMESTAMP) Date startDate, @Temporal(TemporalType.TIMESTAMP) Date endDate);
}
