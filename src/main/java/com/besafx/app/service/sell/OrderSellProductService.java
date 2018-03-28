package com.besafx.app.service.sell;

import com.besafx.app.entity.sell.OrderSell;
import com.besafx.app.entity.sell.OrderSellProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderSellProductService extends PagingAndSortingRepository<OrderSellProduct, Long>, JpaSpecificationExecutor<OrderSellProduct> {
    List<OrderSellProduct> findByOrderSell(OrderSell id);

    List<OrderSellProduct> findByOrderSellIn(List<OrderSell> orders);

    List<OrderSellProduct> findByOrderSellId(Long id);

    List<OrderSellProduct> findByOrderSellIdIn(List<Long> orderIds);
}
