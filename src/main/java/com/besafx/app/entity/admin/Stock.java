package com.besafx.app.entity.admin;

import com.besafx.app.entity.enums.ProductUnit;
import com.fasterxml.jackson.annotation.JsonCreator;
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
public class Stock implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "stockSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "STOCK_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "stockSequenceGenerator")
    private Long id;

    @Enumerated(EnumType.STRING)
    private ProductUnit productUnit;

    @ManyToOne
    @JoinColumn(name = "product")
    private Product product;

    private Double reorderAmount;

    private Double realAmount;

    @ManyToOne
    @JoinColumn(name = "warehouse")
    private Warehouse warehouse;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String note;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "last_update")
    private Date lastUpdate;

    @ManyToOne
    @JoinColumn(name = "last_person")
    private Person lastPerson;

    @JsonCreator
    public static Stock Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Stock stock = mapper.readValue(jsonString, Stock.class);
        return stock;
    }


}
