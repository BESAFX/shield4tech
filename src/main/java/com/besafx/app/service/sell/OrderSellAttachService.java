package com.besafx.app.service.sell;

import com.besafx.app.entity.sell.OrderSell;
import com.besafx.app.entity.sell.OrderSellAttach;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderSellAttachService extends PagingAndSortingRepository<OrderSellAttach, Long>, JpaSpecificationExecutor<OrderSellAttach> {
    List<OrderSellAttach> findByOrderSell(OrderSell id);

    List<OrderSellAttach> findByOrderSellIn(List<OrderSell> orders);

    List<OrderSellAttach> findByOrderSellId(Long id);

    List<OrderSellAttach> findByOrderSellIdIn(List<Long> orderIds);
}
