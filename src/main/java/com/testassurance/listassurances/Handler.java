/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testassurance.listassurances;

import com.testassurance.createassurance.Assurance;
import com.testassurance.utils.DatabaseQueries;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.*;
import redis.clients.jedis.Jedis;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

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
        JSONObject headerJson = new JSONObject();
        JSONObject responseBody = new JSONObject();

        try {
            JSONObject  event = (JSONObject ) parser.parse(reader);
            Jedis jedis = new Jedis("assurance-inmem.gfmdlf.0001.use2.cache.amazonaws.com");
            headerJson.put("Content-Type", "application/json");
            responseJson.put("headers", headerJson);
            
            if (event.get("body") != null) {
                Assurance assurance = new Assurance((String) event.get("body").toString());
                System.out.println(assurance.getAssuranceName());

                System.out.println("Connection Successful");
                String value= "'"+assurance.toString()+"'";
                System.out.println("The server is running " + jedis.ping());
                jedis.set(assurance.getId(), value );

                System.out.println("Stored string in redis:: "+ jedis.get(assurance.getId()));
                
                System.out.println(event.toJSONString());
                responseBody.put("message", "Assurance updated");
                headerJson.put("Content-Type", "application/json");

                responseJson.put("statusCode", 200);
                DatabaseQueries db = new DatabaseQueries();
                JSONArray listAssurances=db.ListAssurances(assurance);
                responseJson.put("body", listAssurances.toString());
        }else{
                responseJson.put("statusCode", 400);
        }
        }catch (ParseException pex) {
            responseJson.put("statusCode", 400);
            responseJson.put("exception", pex);
        }

        OutputStreamWriter writer = new OutputStreamWriter(outputStream, "UTF-8");
        writer.write(responseJson.toString());
        writer.close();
    }
}
