package com.besafx.app.entity.admin;

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
public class Contact implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "contactSequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "CONTACT_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "contactSequenceGenerator")
    private Long id;

    private String name;

    private String mobile1;

    private String mobile2;

    private String phone1;

    private String phone2;

    private String city;

    private String postalCode;

    private String country;

    @Temporal(TemporalType.TIMESTAMP)
    private Date registerDate;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String photo;

    private String email;

    private String website;

    @JsonCreator
    public static Contact Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        Contact contact = mapper.readValue(jsonString, Contact.class);
        return contact;
    }


}
