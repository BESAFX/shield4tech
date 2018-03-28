package com.besafx.app.entity.sell;

import com.besafx.app.entity.admin.Product;
import com.besafx.app.entity.sell.OrderSell;
import com.besafx.app.entity.enums.ProductUnit;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class BillSellProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "billSellProductSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "BILL_SELL_PRODUCT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "billSellProductSequenceGenerator")
    private Long id;

    private Integer code;

    private Double sellPrice;

    private Double quantity;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    @JoinColumn(name = "billSell")
    @ManyToOne
    private BillSell billSell;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @JsonCreator
    public static BillSellProduct Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        BillSellProduct billSellProduct = mapper.readValue(jsonString, BillSellProduct.class);
        return billSellProduct;
    }
}
