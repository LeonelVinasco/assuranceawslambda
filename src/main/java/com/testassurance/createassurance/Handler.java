/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testassurance.createassurance;

import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.*;

import java.io.*;
/**
 *
 * @author leo
 */
public class Handler implements RequestStreamHandler {
    
    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

        System.out.println("Initializing...");

        JSONParser parser = new JSONParser();

        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        JSONObject responseJson = new JSONObject ();

        try {
            JSONObject  event = (JSONObject ) parser.parse(reader);

            if (event.get("body") != null) {
                Assurance assurance = new Assurance((String) event.get("body"));
            }

            JSONObject responseBody = new JSONObject();
            responseBody.put("message", "New item created");

            JSONObject headerJson = new JSONObject();
            headerJson.put("x-custom-header", "my custom header value");

            responseJson.put("statusCode", 200);
            responseJson.put("headers", headerJson);
            responseJson.put("body", responseBody.toString());

        }catch (ParseException pex) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", pex);
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }
}
