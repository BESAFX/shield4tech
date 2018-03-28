package com.besafx.app.entity.buy;

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
public class OrderBuyAttach implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "orderBuyAttachSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "ORDER_BUY_ATTACH_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "orderBuyAttachSequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "attach")
    private Attach attach;

    @ManyToOne
    @JoinColumn(name = "orderBuy")
    private OrderBuy orderBuy;

    @JsonCreator
    public static OrderBuyAttach Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        OrderBuyAttach attachment = mapper.readValue(jsonString, OrderBuyAttach.class);
        return attachment;
    }
}
