package com.app.service;

import com.app.averageForBigDecimal.AverageCollector;
import com.app.exceptions.MyException;
import com.app.jsonParser.ClientWithProductsJsonConverter;
import com.app.model.Client;
import com.app.model.ClientWithProducts;
import com.app.model.Product;
import com.app.model.Purchases;
import com.app.jsonParser.ObjectToMapMapper;
import org.eclipse.collections.impl.collector.Collectors2;
import org.eclipse.collections.impl.map.mutable.ConcurrentHashMap;

import java.beans.Customizer;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReportsAboutPurchases {
    Map<Client, Map<Product, Long>> purchaseMap = new Purchases().getProductOrderNumber();

    // 1. FIND A CLIENT, WHO PAID THE MOST FOR ALL THE PRODUCTS
    public Client clientWithAMaximumSumOfExpenses() {
        return purchaseMap.entrySet()
                .stream()
                .max((e1, e2) -> e1.getValue().entrySet().stream().map(product -> product.getKey().getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .compareTo(e2.getValue().entrySet().stream().map(product -> product.getKey().getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add)))
                .map(e -> e.getKey())
                .get();

    }

    // 2. FIND A CLIENT, WHO PAID THE MOST FOR ALL THE PRODUCTS OF THE SELECTED CATEGORY
    public Client clientWithMaximumSumOfExpensesInSelectedCategory(String categoryName) {
        return purchaseMap.entrySet()
                .stream()
                .max((e1, e2) -> e1.getValue().entrySet().stream().filter(product -> product.getKey().getCategory().equals(categoryName)).map(
                        product -> product.getKey().getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .compareTo(e2.getValue().entrySet().stream().filter(product -> product.getKey().getCategory().equals(categoryName)).map(
                                product -> product.getKey().getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add))
                )
                .map(e -> e.getKey())
                .get();
    }



    // A Map showing the most popular category or products grouped by age of a customer

    // teraz mam: Map<Client, Map<Product, Long>>

    public Map<Integer, List<String>> productCategoriesByAge() {

        Map<Integer, Map<String, Long>> grouped = purchaseMap.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue()
                                .entrySet()
                                .stream()
                                .flatMap(ee -> Collections.nCopies(ee.getValue().intValue(), ee.getKey()).stream())
                                .collect(Collectors.toList())

                        )
                )

                .entrySet()
                .stream()
                .peek((x) -> System.out.println(x.getKey() + "111--" + x.getValue()))
                .collect(Collectors.toMap(
                        ee -> ee.getKey().getAge(),
                        ee -> {
                            Map<String, Long> groupedCategories = ee.getValue().stream().map(Product::getCategory).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                            return groupedCategories;
                        },
                        (v1, v2) -> {
                            Map<String, Long> m3 = new HashMap<>();
                            m3.putAll(v1);
                            v2.forEach((k, v) -> {
                                if (m3.containsKey(k)) {
                                    m3.put(k, m3.get(k) + v);
                                } else {
                                    m3.put(k, v);
                                }
                            });
                            return m3;
                        })
                );

        return grouped
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> {
                            Long maxVal = e.getValue().entrySet().stream().sorted((e1, e2) -> Long.compare(e2.getValue(), e1.getValue())).findFirst().orElse(null).getValue();
                            return e.getValue().entrySet().stream().filter(ee -> ee.getValue().equals(maxVal)).map(Map.Entry::getKey).collect(Collectors.toList());
                        })
                );
    }



    // 3. A map with the average price in given category.
    // Additionally it depicts the most expensive and the cheapest product per each category


    //Map<String, BigDecimal>

    public void averagePriceInCategory() {
        Map<String, List<Product>> categoryAndAmountOfProducts = purchaseMap
                .entrySet()
                .stream()
                .flatMap(e -> e.getValue().entrySet().stream().flatMap(ee -> Collections.nCopies(ee.getValue().intValue(), ee.getKey()).stream()))
                .collect(Collectors.groupingBy(Product::getCategory));

        Map<String, BigDecimal> averagePrices = categoryAndAmountOfProducts
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue().stream().collect(Collectors2.summarizingBigDecimal(Product::getPrice)).getAverage())
                );
        System.out.println(averagePrices);

        Map<String, Product> maxPricesProducts = categoryAndAmountOfProducts
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue().stream().max(Comparator.comparing(Product::getPrice)).orElseThrow(() -> new MyException("EX")))
                );
        System.out.println(maxPricesProducts);

        Map<String, Product> minPricesProducts = categoryAndAmountOfProducts
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue().stream().min(Comparator.comparing(Product::getPrice)).orElseThrow(() -> new MyException("EX")))
                );
        System.out.println(minPricesProducts);
    }


    // 4. Wyznacz klientów, którzy kupowali najczęściej produkty danej
    //    //kategorii. Otrzymane zestawienie zwracaj w postaci mapy.

    // 4. Clients buying products the most often in selected category.


    public List<Client> clientsBuyingMostOften(String category) {
        Map<Client, Long> categoriesCounter = purchaseMap
                .entrySet()
                .stream()
                .filter(e -> e.getValue().entrySet().stream().anyMatch(ee -> ee.getKey().getCategory().equals(category)))
                .collect(Collectors.toMap(e -> e.getKey(), e -> e.getValue()
                        .entrySet()
                        .stream()
                        .flatMap(ee -> Collections.nCopies(ee.getValue().intValue(), ee.getKey()).stream())
                        .filter(p -> p.getCategory().equals(category)).count()
                ));
        Long maxVal = categoriesCounter.entrySet().stream().max(Comparator.comparingLong(e -> e.getValue())).orElse(null).getValue();

        return categoriesCounter
                .entrySet()
                .stream()
                .filter(e -> e.getValue().equals(maxVal))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());

    }




    // 5. Checking if client is able to pay for his purchases. Returns a map
    // with a client and the result of this calculation:
    // minus if client has a debt - not able to pay;
    // and the the account balance when he is able to pay;


    public Map<Client, BigDecimal> doesClientHaveEnoughCash() {
        return purchaseMap
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue().entrySet().stream()
                                .map(p -> p.getKey().getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add)
                        )
                )
                .entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                e -> e.getKey(),
                                e -> e.getKey().getCash().subtract(e.getValue())
                        )
                );
    }
}





