package com.app.jsonParser;

import com.app.model.Client;
import com.app.model.Product;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ObjectToMapMapper {
   private static Map<Client, List<Product>> clientWithProducts;


    public ObjectToMapMapper() {
        clientWithProducts = new HashMap<>();
        clientWithProducts.put(new Client("TOM", "HANKS", 40, new BigDecimal(250000)), new ArrayList<Product>(Arrays.asList(
                new Product("FIAT", "CAR", new BigDecimal(3400)),
                new Product("APPLE", "ELECTRONICS", new BigDecimal(10)),
                new Product("PS4", "ELECTRONICS", new BigDecimal(2400)),
                new Product("FIAT", "CAR", new BigDecimal(230300)),
                new Product("PS4", "ELECTRONICS", new BigDecimal(2400)),
                new Product("PS4", "ELECTRONICS", new BigDecimal(2400))
        )));
        clientWithProducts.put(new Client("DAVID", "BECKHAM", 42, new BigDecimal(450000)), new ArrayList<Product>(Arrays.asList(
                new Product("PORSCHE", "CAR", new BigDecimal(130400)),
                new Product("APPLE", "ELECTRONICS", new BigDecimal(8100)),
                new Product("COMPUTER", "ELECTRONICS", new BigDecimal(12000)))
        ));
        clientWithProducts.put(new Client("JOHN", "MAYER", 40, new BigDecimal(150000)), new ArrayList<Product>(Arrays.asList(
                new Product("FENDER", "GUITAR", new BigDecimal(6500)),
                new Product("APPLE", "ELECTRONICS", new BigDecimal(8100)),
                new Product("COMPUTER", "ELECTRONICS", new BigDecimal(12000)))
        ));
    }

    //THIS METHOD TRANSFORMS THE map of <Client, List<Products>> into a Map<Client, Map<Product,Long>>

    public static Map<Client, Map<Product,Long>> mapOfClientsWhoBoughtProductsManyTimes(){
       /* ObjectToMapMapper objectToMapMapper = new ObjectToMapMapper();*/
        return clientWithProducts.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue().stream().collect(Collectors.groupingBy(
                        product -> new Product(product.getName(), product.getCategory(), product.getPrice()), Collectors.counting()
                        )
                )));

    }

    public static Map<Client, List<Product>> getClientWithProducts() {
        return clientWithProducts;
    }

    public static void setClientWithProducts(Map<Client, List<Product>> clientWithProducts) {
        ObjectToMapMapper.clientWithProducts = clientWithProducts;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ObjectToMapMapper that = (ObjectToMapMapper) o;
        return Objects.equals(clientWithProducts, that.clientWithProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientWithProducts);
    }

    @Override
    public String toString() {
        return "ObjectToMapMapper{" +
                "clientWithProducts=" + clientWithProducts +
                '}';
    }
}
