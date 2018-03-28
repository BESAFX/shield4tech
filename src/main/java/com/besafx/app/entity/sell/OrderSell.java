package com.besafx.app.entity.sell;

import com.besafx.app.entity.admin.Customer;
import com.besafx.app.entity.admin.Person;
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
public class OrderSell implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(OrderSell.class);

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "orderBuySequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_SELL_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderBuySequenceGenerator")
    private Long id;

    private Integer code;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    private Double discount;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @ManyToOne
    @JoinColumn(name = "customer")
    private Customer customer;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update")
    private Date lastUpdate;

    @ManyToOne
    @JoinColumn(name = "last_person")
    private Person lastPerson;

    @JsonCreator
    public static OrderSell Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderSell orderBuy = mapper.readValue(jsonString, OrderSell.class);
        return orderBuy;
    }
}
