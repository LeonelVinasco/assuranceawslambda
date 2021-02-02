/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testassurance.utils;

import com.testassurance.createassurance.Assurance;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
/**
 *
 * @author leo
 */
public class DatabaseQueries {
    
    public Connection connect(){
                String url = "jdbc:mysql://database-1.cluster-civqhsmx6dcp.us-east-2.rds.amazonaws.com:3306/assurancedb";
        String user = "admin";
        String passworddb = "admin12345";
        java.sql.Connection conn;
        try {
            conn = DriverManager.getConnection(url, user, passworddb);
            if (conn != null) {
                System.out.println("Connected to the database");
                Statement statement = conn.createStatement();
//                String SQL="CREATE DATABASE IF NOT EXISTS assurancedb" +
//                        "GO\n" +
//                        "\n" +
//                        "USE assurancedb\n" +
//                        "GO\n" +
//                        "CREATE TABLE IF NOT EXISTS assurances(\n" +
//                        "  id INT PRIMARY KEY NOT NULL,\n" +
//                        "  name VARCHAR(500),\n" +
//                        "  description VARCHAR(500),\n" +
//                        "  value DOUBLE;";
//                statement.executeUpdate(SQL);
                return conn;
            }
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
        return null;
    }
    
    public void InsertAssurance(Assurance assu){
        java.sql.Connection connec=null;
        connec=connect();
        Statement query = null;
        try{
             System.out.println("To insert "+assu.toString());
            query = connec.createStatement();
            System.out.println("After statement");
//            String SQL1=
//                    "INSERT INTO assurances (id, name, description, value)" +
//                    "VALUES (1,'b','b',1.2);";
            String SQL=
                    "INSERT INTO assurances (id, name, description, value)" +
                    "VALUES ("+assu.getId()+",'"+assu.getAssuranceName()+"','"+assu.getDescription()+"','"+
                    assu.getValue().toString()+"');";
            query.executeUpdate(SQL);
            System.out.println("Assurance inserted on db");
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
    }
    
    public void UpdateAssurance(Assurance assu){
        java.sql.Connection connec=null;
        connec=connect();
        Statement query = null;
        try{
             System.out.println("To insert "+assu.toString());
            query = connec.createStatement();
            System.out.println("After statement");
//            String SQL1=
//                    "INSERT INTO assurances (id, name, description, value)" +
//                    "VALUES (1,'b','b',1.2);";
            String SQL=
                    "UPDATE assurances SET name='"+assu.getAssuranceName()+"', description='" +assu.getDescription()+"', value='"+
                    assu.getValue().toString()+"' WHERE id="+assu.getId()+";";
            query.executeUpdate(SQL);
            System.out.println("Assurance inserted on db");
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
    }
    
     public void DeleteAssurance(Assurance assu){
        java.sql.Connection connec=null;
        connec=connect();
        Statement query = null;
        try{
             System.out.println("To insert "+assu.toString());
            query = connec.createStatement();
            System.out.println("After statement");
//            String SQL1=
//                    "INSERT INTO assurances (id, name, description, value)" +
//                    "VALUES (1,'b','b',1.2);";
            String SQL=
                    "DELETE from assurances where id="+assu.getId()+";";
            query.executeUpdate(SQL);
            System.out.println("Assurance inserted on db");
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
    }
     
    public JSONArray ListAssurances(Assurance assu){
        java.sql.Connection connec=null;
        connec=connect();
        Statement query = null;
        try{
             System.out.println("To insert "+assu.toString());
            query = connec.createStatement();
            System.out.println("After statement");
//            String SQL1=
//                    "INSERT INTO assurances (id, name, description, value)" +
//                    "VALUES (1,'b','b',1.2);";
            String SQL=
                    "SELECT * FROM assurances";
            ResultSet rs =query.executeQuery(SQL);
            
            JSONArray listAssurances = new JSONArray();
            while(rs.next() ) {
             JSONObject rowQuery = new JSONObject();
             rowQuery.put("id", rs.getString("id"));
             rowQuery.put("assuranceName", rs.getString("name"));
             rowQuery.put("description", rs.getString("description"));
             rowQuery.put("value", rs.getString("value"));
             
             listAssurances.add(rowQuery);
            }
            return listAssurances;
        } catch (SQLException ex) {
            System.out.println("An error occurred. Maybe user/password is invalid");
            ex.printStackTrace();
        }
        return null;
    }
}
