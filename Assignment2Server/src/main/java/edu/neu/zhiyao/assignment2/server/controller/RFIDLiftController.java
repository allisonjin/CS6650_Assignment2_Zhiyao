
package edu.neu.zhiyao.assignment2.server.controller;

import edu.neu.zhiyao.assignment2.server.entity.RFIDLiftData;
import edu.neu.zhiyao.assignment2.server.service.RFIDLiftService;
import edu.neu.zhiyao.assignment2.server.service.SkierDailyStatService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("load")
public class RFIDLiftController {
    
    @Context
    UriInfo uriInfo;
    
    private RFIDLiftService service = new RFIDLiftService();
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response postRFIDLiftData(RFIDLiftData data) {
        String result;
        if (data.getId() == null) {
            service.saveOrUpdate(data);
            result = "RFID lift data saved: " + data;
        } else {
            SkierDailyStatService statService = new SkierDailyStatService();
            statService.updateSkierDailyStats(data.getDayNum());
            result = "Daily data all saved!";
        }
        return Response.status(201).entity(result).build();
    }
    
}
