package com.besafx.app.entity.admin;

import com.besafx.app.entity.admin.listener.CustomerListener;
import com.besafx.app.entity.enums.CustomerType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.IOException;
import java.io.Serializable;

@Data
@Entity
@EntityListeners(CustomerListener.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String SCREEN_NAME = "العملاء";

    @GenericGenerator(
            name = "customerSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "CUSTOMER_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "customerSequenceGenerator")
    private Long id;

    private Integer code;

    @Column(columnDefinition = "boolean default true")
    private Boolean enabled;

    private String companyName;

    private String managerName;

    private String agentName;

    @Enumerated(EnumType.STRING)
    private CustomerType customerType;

    @ManyToOne
    @JoinColumn(name = "contact")
    private Contact contact;

    @JsonCreator
    public static Customer Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Customer customer = mapper.readValue(jsonString, Customer.class);
        return customer;
    }

    public String getEnabledInArabic() {
        try {
            return this.enabled ? "مفعل" : "معطل";
        } catch (Exception ex) {
            return "";
        }
    }

    public String getEnabledInEnglish() {
        try {
            return this.enabled ? "Enabled" : "Disabled";
        } catch (Exception ex) {
            return "";
        }
    }

    public String getCustomerTypeInArabic() {
        try {
            return this.customerType.getName();
        } catch (Exception ex) {
            return "";
        }
    }


}
