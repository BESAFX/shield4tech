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
public class Supplier implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "supplierSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "SUPPLIER_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "supplierSequenceGenerator")
    private Long id;

    private Integer code;

    @Column(columnDefinition = "boolean default true")
    private Boolean enabled;

    private String companyName;

    private String managerName;

    private String agentName;

    private String taxCode;

    @ManyToOne
    @JoinColumn(name = "contact")
    private Contact contact;

    @JsonCreator
    public static Supplier Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Supplier supplier = mapper.readValue(jsonString, Supplier.class);
        return supplier;
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
}
