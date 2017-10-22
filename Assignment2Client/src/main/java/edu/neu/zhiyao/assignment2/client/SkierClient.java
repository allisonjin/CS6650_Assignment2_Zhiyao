package edu.neu.zhiyao.assignment2.client;

import edu.neu.zhiyao.assignment2.client.entity.RFIDLiftData;
import edu.neu.zhiyao.assignment2.client.entity.SkierDailyStat;
import javax.json.JsonObject;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * @author allisonjin
 */
public class SkierClient {
 
    private WebTarget webTarget;
    private Client client;
//    private static final String BASE_URI = 
//            "http://ec2-54-193-122-64.us-west-1.compute.amazonaws.com:8080/Assignment2/webresources";
    private static final String BASE_URI = "http://localhost:8081/Assignment2Server/webresources";

    public SkierClient() {
        client = ClientBuilder.newClient();
        webTarget = client.target(BASE_URI);
    }

    public SkierDailyStat getSkierDailyStat(int skierId, int dayNum) throws ClientErrorException {
        return webTarget.path(java.text.MessageFormat.format("myvert/{0}/{1}", 
                                new Object[]{skierId, dayNum}))
                        .request()
                        .accept(MediaType.APPLICATION_JSON)
                        .get(SkierDailyStat.class);
    }

    public Response postRFIDLiftData(Object data) throws ClientErrorException {
        return webTarget.path("load")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.entity(data, MediaType.APPLICATION_JSON_TYPE));
    }

    public Response postEndOfData(int dayNum) {
        return postRFIDLiftData(new RFIDLiftData("EOF", 0, dayNum, 0, 0, 0));
    }
    
    public void close() {
        client.close();
    }
    
}
