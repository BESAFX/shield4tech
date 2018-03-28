package com.besafx.app.service.buy;

import com.besafx.app.entity.buy.OrderBuy;
import com.besafx.app.entity.buy.OrderBuyAttach;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderBuyAttachService extends PagingAndSortingRepository<OrderBuyAttach, Long>, JpaSpecificationExecutor<OrderBuyAttach> {
    List<OrderBuyAttach> findByOrderBuy(OrderBuy id);

    List<OrderBuyAttach> findByOrderBuyIn(List<OrderBuy> orders);

    List<OrderBuyAttach> findByOrderBuyId(Long id);

    List<OrderBuyAttach> findByOrderBuyIdIn(List<Long> orderIds);
}
