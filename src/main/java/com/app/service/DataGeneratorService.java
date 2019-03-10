package com.app.service;

import com.app.exceptions.MyException;
import com.app.jsonParser.ClientWithProductsJsonConverter;
import com.app.jsonParser.JsonConverter;
import com.app.model.Client;
import com.app.model.ClientWithProducts;
import com.app.model.Product;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class DataGeneratorService {

    private final List<String> jsonFilenames;

    public DataGeneratorService(String ... jsonFilenames) {
        this.jsonFilenames = Arrays.asList(jsonFilenames);
    }

    public void generateDataToJson() {
        jsonFilenames.forEach(jsonFilename -> {
            List<ClientWithProducts> clientWithProducts = Arrays.asList(
                    ClientWithProducts.builder()
                            .client(Client.builder().name(DataGenerator.generateString(1)).surname(DataGenerator.generateString(1)).age(10).cash(new BigDecimal("100")).build())
                            .products(Arrays.asList(
                                    Product.builder()
                                            .name(DataGenerator.generateString(1))
                                            .price(new BigDecimal("100"))
                                            .category(DataGenerator.generateString(1))
                                            .build(),
                                    Product.builder()
                                            .name(DataGenerator.generateString(1))
                                            .price(new BigDecimal("110"))
                                            .category(DataGenerator.generateString(1))
                                            .build()
                                    )
                            ).build(),
                    ClientWithProducts.builder()
                            .client(Client.builder().name(DataGenerator.generateString(1)).surname(DataGenerator.generateString(1)).age(10).cash(new BigDecimal("100")).build())
                            .products(Arrays.asList(
                                    Product.builder()
                                            .name(DataGenerator.generateString(1))
                                            .price(new BigDecimal("100"))
                                            .category(DataGenerator.generateString(1))
                                            .build(),
                                    Product.builder()
                                            .name(DataGenerator.generateString(1))
                                            .price(new BigDecimal("110"))
                                            .category(DataGenerator.generateString(1))
                                            .build()
                                    )
                            ).build()
            );
            new ClientWithProductsJsonConverter(jsonFilename).toJson(clientWithProducts);
        });

    }

    public Map<Client, Map<Product, Long>> fromJson() {
        return jsonFilenames
                .stream()
                .flatMap(jsonFilenames -> new ClientWithProductsJsonConverter(jsonFilenames).fromJson().orElseThrow(IllegalStateException::new).stream())
                .collect(Collectors.groupingBy(ClientWithProducts::getClient))
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue().stream().flatMap(cwp -> cwp.getProducts().stream()).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                ));
    }

}
