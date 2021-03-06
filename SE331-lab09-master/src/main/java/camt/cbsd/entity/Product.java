package camt.cbsd.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Entity
@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    //comment id as a String to use mongoDB
    Long id;
    String productId;
    String productName;
    double productPrice;
    int productQuantity;
    String image;
    String description;
    String color;
    String size;

}
