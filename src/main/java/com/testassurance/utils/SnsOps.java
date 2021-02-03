/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.testassurance.utils;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
/**
 *
 * @author leo
 */
public class SnsOps {
    
    public static SnsClient Connect(){
        SnsClient snsClient = SnsClient.builder()
                .region(Region.US_EAST_2)
                .build();
        
        return snsClient;
    } 
    
    public void CloseConn(SnsClient snsCLient){
        snsCLient.close();
    } 
    
    public void PubTopic(String message, String topicArn) {
        
        try {
            SnsClient snsClient=Connect();
            PublishRequest request = PublishRequest.builder()
                .message(message)
                .topicArn(topicArn)
                .build();

            PublishResponse result = snsClient.publish(request);
            System.out.println(result.messageId() + " Message sent. Status was " + result.sdkHttpResponse().statusCode());
            
            CloseConn(snsClient);
         } catch (SnsException e) {
            System.err.println(e.awsErrorDetails().errorMessage());
              System.exit(1);
         }
    }
}
