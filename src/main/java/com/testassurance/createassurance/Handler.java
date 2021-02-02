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
         Connection conn1 = null;
        JSONObject responseJson = new JSONObject ();
        /*try {
            // connect way #1
            String url1 = "jdbc:mysql://database.cluster-civqhsmx6dcp.us-east-2.rds.amazonaws.com/testdb";
            String user = "admin";
            String password = "admin123";
 
            conn1 = DriverManager.getConnection(url1, user, password);
            if (conn1 != null) {
                System.out.println("Connected to the database test1");
            }
              System.out.println("Connected to the database test3");
            
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }*/
        String url = "jdbc:mysql://database-1.cluster-civqhsmx6dcp.us-east-2.rds.amazonaws.com";
        String user = "admin";
        String password = "admin123";
        try (Connection conn = DriverManager.getConnection(url, user, password)) {
 
            if (conn != null) {
                System.out.println("Connected to the database");
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
        try {
            Jedis jedis = new Jedis("assurance-inmem.gfmdlf.0001.use2.cache.amazonaws.com");
            // prints out "Connection Successful" if Java successfully connects to Redis server.
            System.out.println("Connection Successful");
            System.out.println("The server is running " + jedis.ping());
            jedis.set("company-name", "500Rockets.io");
            System.out.println("Stored string in redis:: "+ jedis.get("company-name"));
        }catch(Exception e) {
        System.out.println(e);
        }

        try {
            JSONObject  event = (JSONObject ) parser.parse(reader);

            if (event.get("body") != null) {
                Assurance assurance = new Assurance((String) event.get("body").toString());
                 System.out.println(assurance.getAssuranceName());
            }
            System.out.println(event.toJSONString());
            JSONObject responseBody = new JSONObject();
            responseBody.put("message", "New item created");

            JSONObject headerJson = new JSONObject();
            headerJson.put("Content-Type", "application/json");

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
