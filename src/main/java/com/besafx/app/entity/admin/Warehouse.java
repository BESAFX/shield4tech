package com.besafx.app.entity.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
public class Warehouse implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "warehouseSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "WAREHOUSE_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "warehouseSequenceGenerator")
    private Long id;

    private Integer code;

    @Column(columnDefinition = "boolean default true")
    private Boolean enabled;

    @ManyToOne
    @JoinColumn(name = "contact")
    private Contact contact;

    @JsonCreator
    public static Warehouse Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Warehouse warehouse = mapper.readValue(jsonString, Warehouse.class);
        return warehouse;
    }


}
