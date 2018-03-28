package com.besafx.app.entity.sell;

import com.besafx.app.entity.admin.Attach;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
public class OrderSellAttach implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "orderSellAttachSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_SELL_ATTACH_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderSellAttachSequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attach")
    private Attach attach;

    @ManyToOne
    @JoinColumn(name = "orderSell")
    private OrderSell orderSell;

    @JsonCreator
    public static OrderSellAttach Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderSellAttach attachment = mapper.readValue(jsonString, OrderSellAttach.class);
        return attachment;
    }
}
