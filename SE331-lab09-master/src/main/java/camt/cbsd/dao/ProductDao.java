package camt.cbsd.dao;

import camt.cbsd.entity.Product;

import java.util.List;


public interface ProductDao {
    Product add(Product course);
    List<Product> list();

    Product findById(long id);


}