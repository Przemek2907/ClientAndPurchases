package com.app.jsonParser;

import com.app.model.ClientWithProducts;

import java.util.List;

public class ClientWithProductsJsonConverter extends JsonConverter<List<ClientWithProducts>> {

    public ClientWithProductsJsonConverter(String jsonFilename) {
        super(jsonFilename);
    }
}
