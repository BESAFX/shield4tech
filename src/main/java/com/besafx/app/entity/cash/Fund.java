package com.besafx.app.entity.cash;

import com.besafx.app.component.BeanUtil;
import com.besafx.app.entity.admin.Person;
import com.besafx.app.service.cash.FundService;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Component
public class Fund implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    private static FundService fundService;

    @PostConstruct
    public void init() {
        try {
            fundService = BeanUtil.getBean(FundService.class);
        } catch (Exception ex) {

        }
    }

    @GenericGenerator(
            name = "fundSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "FUND_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "fundSequenceGenerator")
    private Long id;

    private Long code;

    private Double tempBalance;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdate;

    @ManyToOne
    @JoinColumn(name = "last_person")
    private Person lastPerson;

    @JsonCreator
    public static Fund Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Fund fund = mapper.readValue(jsonString, Fund.class);
        return fund;
    }
}
