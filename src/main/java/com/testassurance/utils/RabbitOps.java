/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testassurance.utils;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
/**
 *
 * @author leo
 */
public class RabbitOps {
    private static final String QUEUE_NAME = "hello";
    public void Publish(){
    
        
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("b-7346d7b9-b60d-41b5-8e72-fe5a481850bb.mq.us-east-2.amazonaws.com");
        factory.setUsername("admin");
        factory.setPassword("administrator12345");

        
        String message ="Jello World at  Rabbit";
               
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            channel.close();
            connection.close();
        }catch(Exception e){
          System.out.println(e);
        }
    
    }
}
