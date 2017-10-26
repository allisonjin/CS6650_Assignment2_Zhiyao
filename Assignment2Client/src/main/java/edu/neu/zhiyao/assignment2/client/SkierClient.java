package edu.neu.zhiyao.assignment2.client;

import edu.neu.zhiyao.assignment2.client.entity.RFIDLiftData;
import edu.neu.zhiyao.assignment2.client.entity.SkierDailyStat;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientProperties;

/**
 * @author allisonjin
 */
public class SkierClient {
 
    private final WebTarget webTarget;
    private final Client client;
    private static final String BASE_URI = 
            "http://54.67.95.209:8081/Assignment2Server/webresources";
//    private static final String BASE_URI = "http://localhost:8081/Assignment2Server/webresources";

    public SkierClient() {
        client = ClientBuilder.newClient();
//        client.property(ClientProperties.CONNECT_TIMEOUT, 30000)
//              .property(ClientProperties.READ_TIMEOUT, 30000);
        webTarget = client.target(BASE_URI);
    }

    public SkierDailyStat getSkierDailyStat(int skierId, int dayNum) throws ClientErrorException {
        Response resp = webTarget.path(java.text.MessageFormat.format("myvert/{0}/{1}", 
                                new Object[]{skierId, dayNum}))
                        .request()
                        .accept(MediaType.APPLICATION_JSON)
                        .get();
        SkierDailyStat stat = null;
        if (resp.getStatus() == 200) {
            Integer[] res = resp.readEntity(Integer[].class);
            stat = new SkierDailyStat(skierId, dayNum, res[0], res[1]);
        }
        return stat;
    }

    public Response postRFIDLiftData(RFIDLiftData data) throws Exception {
        return webTarget.path("load")
                        .request(MediaType.TEXT_PLAIN)
                        .post(Entity.entity(data, MediaType.APPLICATION_JSON));
    }
    
    public Response postEndOfData(int dayNum) throws Exception{
        return postRFIDLiftData(new RFIDLiftData("EOF", 0, dayNum, 40000, 0, 0));
    }
    
    public void close() {
        client.close();
    }
    
}
