package com.besafx.app.service.buy;

import com.besafx.app.entity.buy.OrderBuy;
import com.besafx.app.entity.buy.OrderBuyProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface OrderBuyProductService extends PagingAndSortingRepository<OrderBuyProduct, Long>, JpaSpecificationExecutor<OrderBuyProduct> {
    List<OrderBuyProduct> findByOrderBuy(OrderBuy id);

    List<OrderBuyProduct> findByOrderBuyIn(List<OrderBuy> orders);

    List<OrderBuyProduct> findByOrderBuyId(Long id);

    List<OrderBuyProduct> findByOrderBuyIdIn(List<Long> orderIds);
}
