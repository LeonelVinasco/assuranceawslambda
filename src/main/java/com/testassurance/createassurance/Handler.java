/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testassurance.createassurance;

import com.testassurance.utils.DatabaseQueries;
import com.testassurance.utils.SnsOps;
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
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;

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
        SnsOps snsOps = new SnsOps();

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
                responseBody.put("message", "New assurance created");
                headerJson.put("Content-Type", "application/json");

                responseJson.put("statusCode", 200);
                responseJson.put("body", responseBody.toString());
                DatabaseQueries db = new DatabaseQueries();
                snsOps.PubTopic(assurance.toString(),"arn:aws:sns:us-east-2:203810783092:newassurance");
                db.InsertAssurance(assurance);
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
