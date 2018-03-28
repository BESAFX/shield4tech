package com.besafx.app.entity.sell;

import com.besafx.app.entity.admin.Product;
import com.besafx.app.entity.sell.OrderSell;
import com.besafx.app.entity.enums.ProductUnit;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
public class OrderSellProduct implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(OrderSellProduct.class);

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "orderSellProductSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_SELL_PRODUCT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderSellProductSequenceGenerator")
    private Long id;

    private Double quantity;

    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "orderSell")
    private OrderSell orderSell;

    @JsonCreator
    public static OrderSellProduct Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderSellProduct orderSellProduct = mapper.readValue(jsonString, OrderSellProduct.class);
        return orderSellProduct;
    }
}
