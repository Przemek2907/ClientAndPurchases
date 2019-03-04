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

import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ReportsAboutPurchases {
    DataGeneratorService purchases = new DataGeneratorService("xxx.json", "yyy.json", "zzz.json");


    // 1. FIND A CLIENT, WHO PAID THE MOST FOR ALL THE PRODUCTS
    public Client clientWithAMaximumSumOfExpenses() {
        return purchases.fromJson().entrySet()
                .stream()
                .max((e1, e2) -> e1.getValue().entrySet().stream().map(product -> product.getKey().getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .compareTo(e2.getValue().entrySet().stream().map(product -> product.getKey().getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add)))
                .map(e -> e.getKey())
                .get();

    }

    // 2. FIND A CLIENT, WHO PAID THE MOST FOR ALL THE PRODUCTS OF THE SELECTED CATEGORY
    public Client clientWithMaximumSumOfExpensesInSelectedCategory(String categoryName) {
        return purchases.fromJson().entrySet()
                .stream()
                .max((e1, e2) -> e1.getValue().entrySet().stream().filter(product -> product.getKey().getCategory().equals(categoryName)).map(
                        product -> product.getKey().getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add)
                        .compareTo(e2.getValue().entrySet().stream().filter(product -> product.getKey().getCategory().equals(categoryName)).map(
                                product -> product.getKey().getPrice()).reduce(BigDecimal.ZERO, BigDecimal::add))
                )
                .map(e -> e.getKey())
                .get();
    }

    // Wykonaj zestawienie (mapę), w którym pokażesz wiek klientów oraz
    //kategorie produktów, które najchętniej w tym wieku kupowano.

    // teraz mam: Map<Client, Map<Product, Long>>

    public Map<Integer, String> productCategoriesByAge() {

        return purchases.fromJson().entrySet()
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
                        ee -> ee.getValue().stream().collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()))
                                .entrySet()
                                .stream()
                                .peek((x) -> System.out.println(x.getKey() + "--" + x.getValue()))
                                .max(Comparator.comparingLong(Map.Entry::getValue))
                                .orElseThrow(() -> new MyException("AAA"))
                                .getKey(),
                        (x1, x2) -> x1 + " AND " + x2
                ));
    }


       /* return purchases.fromJson().entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue()
                                .entrySet()
                                .stream()
                                .flatMap(ee -> Collections.nCopies(ee.getValue().intValue(), ee.getKey()).stream())


                        )
                )
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        eee -> eee.getKey().getAge(),
                        eee -> eee.getValue()
                                .collect(Collectors.groupingBy(Product::getCategory, Collectors.counting()))
                                .entrySet()
                                .stream().min((e1, e2) -> Long.compare(e2.getValue(), e1.getValue()))
                                .orElseThrow(() -> new MyException("AAA"))
                                .getKey()
                ));*/


    // 3. Wykonaj zestawienie (mapę), w którym pokażesz średnią cenę produktów
    //w danej kategorii. Dodatkowo wyznacz dla każdej kategorii produkt
    //najdroższy oraz produkt najtańszy.


    //Map<String, BigDecimal>

    /*public void averagePriceInCategory (){
       Map<String, List<Product>> categoryAndAmountOfProducts =  purchases.fromJson()
                .entrySet()
                .stream()
                .map(
                        p -> p.getValue().keySet().stream().collect(Collectors.groupingBy(
                                e -> e.getCategory()
                        ))
                        .entrySet()
                        .stream()
                        .collect(
                                Collectors.toMap(
                                        p.getValue().keySet().stream().map(t->t.getCategory()).findFirst().get(),
                                        p.getValue().keySet().stream().collect(Collectors.toList())
                                )
                        )
                )
               .*/

    // 4. Wyznacz klientów, którzy kupowali najczęściej produkty danej
    //kategorii. Otrzymane zestawienie zwracaj w postaci mapy.

    public Map<Client,String> clientsBuyingMostOften (){
        return purchases.fromJson()
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e -> e.getKey(),
                        e -> e.getValue()
                                .entrySet()
                                .stream()
                                .flatMap(ee -> Collections.nCopies(ee.getValue().intValue(), ee.getKey()).stream())
                                .collect(Collectors.toList())

                )

    }


    // 5. Sprawdź, czy klient jest w stanie zapłacić za zakupy. Żeby
    //to stwierdzić, porównaj wartość pola przechowującego ilość gotówki,
    //którą posiada klient z sumaryczną ceną za zakupy klienta. Wykonaj
    //mapę, w której jako klucz podasz klienta, natomiast jako wartość
    //przechowasz dług, który klient musi spłacić za niezapłacone zakupy.
    //Dług stanowi różnica pomiędzy kwotą do zapłaty oraz gotówką, którą
    //posiada klient.


    public Map<Client, BigDecimal> doesClientHaveEnoughCash() {
        return purchases.fromJson()
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




