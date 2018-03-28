package com.besafx.app.entity.buy;

import com.besafx.app.entity.admin.Person;
import com.besafx.app.entity.admin.Product;
import com.besafx.app.entity.admin.Supplier;
import com.besafx.app.entity.enums.ProductUnit;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
public class OrderBuyProduct implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(OrderBuyProduct.class);

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "orderBuyProductSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_BUY_PRODUCT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderBuyProductSequenceGenerator")
    private Long id;

    private Double quantity;

    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "orderBuy")
    private OrderBuy orderBuy;

    @JsonCreator
    public static OrderBuyProduct Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderBuyProduct orderBuyProduct = mapper.readValue(jsonString, OrderBuyProduct.class);
        return orderBuyProduct;
    }
}
