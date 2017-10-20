package edu.neu.zhiyao.assignment2.server;

import edu.neu.zhiyao.assignment2.server.entity.RFIDLiftData;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("skierserver")
public class SkierSever {

    @GET
    @Path("/myvert/{skierId}/{dayNum}")
    public String getVert(@PathParam("skierId") String skierId, 
            @PathParam("dayNum") int dayNum) {
        return skierId;
    }

    @POST
    @Path("/load")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postRFIDLiftData(RFIDLiftData data) {
        String result = "RFID lift data saved: " + data;
        return Response.status(201).entity(result).build();
    }
}

