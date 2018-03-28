package com.besafx.app.service.buy;

import com.besafx.app.entity.buy.BillBuyProduct;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public interface BillBuyProductService extends PagingAndSortingRepository<BillBuyProduct, Long>, JpaSpecificationExecutor<BillBuyProduct> {
    List<BillBuyProduct> findByBillBuyId(Long id);
}

