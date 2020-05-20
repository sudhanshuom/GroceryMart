package com.app.grocerymart.Singelton;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Categories {
    private static Categories cg = null;
    JsonArray jsonArray = null;

    private Categories(){

    }

    public static Categories getInstance(){
        if(cg == null)
            cg = new Categories();

        return cg;
    }

    public JsonArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JsonArray jsonArray) {
        this.jsonArray = jsonArray;
    }
}
