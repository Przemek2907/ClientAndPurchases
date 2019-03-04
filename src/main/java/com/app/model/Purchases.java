package com.app.model;

import com.app.jsonParser.ObjectToMapMapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Purchases {
    private Map<Client,Map<Product,Long>> productOrderNumber = new HashMap<>();

    public Purchases() {
        try {
            productOrderNumber = ObjectToMapMapper.mapOfClientsWhoBoughtProductsManyTimes();
            //productOrderNumber.forEach((k, v) -> System.out.println(k + "----" + v));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Map<Client, Map<Product, Long>> getProductOrderNumber() {
        return productOrderNumber;
    }

    public void setProductOrderNumber(Map<Client, Map<Product, Long>> productOrderNumber) {
        this.productOrderNumber = productOrderNumber;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchases purchases = (Purchases) o;
        return Objects.equals(productOrderNumber, purchases.productOrderNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productOrderNumber);
    }

    @Override
    public String toString() {
        return "Purchases{" +
                "productOrderNumber=" + productOrderNumber +
                '}';
    }
}
