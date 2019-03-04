package com.app;

import com.app.model.Client;
import com.app.model.Product;
import com.app.model.Purchases;
import com.app.service.ReportsAboutPurchases;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {
    public static void main(String[] args) {

        /*ObjectToMapMapper objectToMapMapper = new ObjectToMapMapper("orders.json");
        Map<Client, List<Product>> clientListMap = objectToMapMapper.getClientWithProducts();
        clientListMap.forEach((k, v) -> System.out.println(k+"----"+v));*/
/*
        ReportsAboutPurchases reportsAbourPurchases = new ReportsAboutPurchases();

        //ObjectToMapMapper objectToMapMapper = new ObjectToMapMapper();
       //objectToMapMapper.mapOfClientsWhoBoughtProductsManyTimes().forEach((k, v) -> System.out.println(k+"----"+v));
       Client client1 = reportsAbourPurchases.clientWithAMaximumSumOfExpenses();
        System.out.println(client1);
        Client client2 = reportsAbourPurchases.clientWithMaximumSumOfExpensesInSelectedCategory("CAR");
        System.out.println(client2);
        *//*Map<Integer, Map<String, Long>> categoriesByAge = reportsAbourPurchases.productCategoriesByAge();
        categoriesByAge.forEach((k,v) -> System.out.println(k + " -> " + v));*//*
        reportsAbourPurchases.productCategoriesByAge2();
        reportsAbourPurchases.averageProductPricePerCategory();*/




  /*      String s = "ala MA Kota";
        System.out.println(s.replaceAll("[^AEYUIOaeyuio]", "").length());
*/
      /*  DataGeneratorService dataGeneratorService
                = new DataGeneratorService("xxx.json", "yyy.json", "zzz.json");
        dataGeneratorService.generateDataToJson();
        dataGeneratorService.fromJson().forEach((k, v) -> System.out.println(k + "\n" + v));*/





        ReportsAboutPurchases reportsAboutPurchases = new ReportsAboutPurchases();

       /* System.out.println(reportsAboutPurchases);
        Client client = reportsAboutPurchases.clientWithAMaximumSumOfExpenses();
        Client client1 = reportsAboutPurchases.clientWithMaximumSumOfExpensesInSelectedCategory("SWEETS");
        System.out.println(client);
        System.out.println(client1);*/

        /*Map<Integer, String>  aaa = reportsAboutPurchases.productCategoriesByAge();
        aaa.forEach((k,v) -> System.out.println(k + " ---> " + v));*/
        reportsAboutPurchases.productCategoriesByAge().forEach((k, v) -> {
            System.out.println(k);
            System.out.println(v);
        });

       /* Map<Client, BigDecimal> debtMap = reportsAboutPurchases.doesClientHaveEnoughCash();
       debtMap.forEach((k,v) -> System.out.println(k + " THIS CLIENT'S ACCOUNT BALANCE AFTER SHOPPING EQUALS TO: " + v));*/


        
    }
}
