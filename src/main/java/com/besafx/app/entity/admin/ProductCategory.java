package com.besafx.app.entity.admin;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
public class ProductCategory implements Serializable {

    private static final long serialVersionUID = 1L;

    @GenericGenerator(
            name = "productCategorySequenceGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @org.hibernate.annotations.Parameter(name = "sequence_name", value = "PRODUCT_CATEGORY_SEQUENCE"),
                    @org.hibernate.annotations.Parameter(name = "initial_value", value = "1"),
                    @org.hibernate.annotations.Parameter(name = "increment_size", value = "1")
            }
    )
    @Id
    @GeneratedValue(generator = "productCategorySequenceGenerator")
    private Long id;

    private Integer code;

    private String nameArabic;

    private String nameEnglish;

    private String descriptionArabic;

    private String descriptionEnglish;

    @OneToMany(mappedBy = "productCategory")
    private List<Product> products = new ArrayList<>();

    @JsonCreator
    public static ProductCategory Create(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ProductCategory productCategory = mapper.readValue(jsonString, ProductCategory.class);
        return productCategory;
    }
}
