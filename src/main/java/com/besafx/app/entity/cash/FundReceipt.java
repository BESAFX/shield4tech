package com.besafx.app.entity.cash;

import com.besafx.app.entity.admin.Receipt;
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
@Table
public class FundReceipt implements Serializable {

    private static final Logger log = LoggerFactory.getLogger(FundReceipt.class);

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "fundReceiptSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "FUND_RECEIPT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "fundReceiptSequenceGenerator")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "receipt")
    private Receipt receipt;

    @ManyToOne
    @JoinColumn(name = "fund")
    private Fund fund;

    @JsonCreator
    public static FundReceipt Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        FundReceipt fundReceipt = mapper.readValue(jsonString, FundReceipt.class);
        return fundReceipt;
    }
}
