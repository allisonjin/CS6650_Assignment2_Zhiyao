/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.neu.zhiyao.assignment2.client;

import edu.neu.zhiyao.assignment2.client.entity.RFIDLiftData;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author allisonjin
 */
public class SkierClient {
 
    final private WebTarget webTarget;
    final private Client client;
    private static final String BASE_URI = 
            "http://ec2-54-193-122-64.us-west-1.compute.amazonaws.com:8080/Assignment2/webresources";

    public SkierClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
        webTarget = client.target(BASE_URI).path("skierserver");
    }

    public String getVert(String skierId, String dayNum) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("myvert/{0}/{1}", 
                                new Object[]{skierId, dayNum}))
                        .request().get(String.class);
    }

    public Response postRFIDLiftData(RFIDLiftData data) throws ClientErrorException {
        return webTarget.path("load")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.entity(data, MediaType.APPLICATION_JSON));
    }

    public void close() {
        client.close();
    }
    
}
