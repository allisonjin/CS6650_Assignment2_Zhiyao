package edu.neu.zhiyao.assignment2.server.controller;

import edu.neu.zhiyao.assignment2.server.entity.RFIDLiftData;
import edu.neu.zhiyao.assignment2.server.service.RFIDLiftService;
import edu.neu.zhiyao.assignment2.server.service.SkierDailyStatService;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("load")
public class RFIDLiftController {
    
    @Context
    UriInfo uriInfo;
    
    @Inject
    private RFIDLiftService rfidLiftService;
    
    @Inject
    private SkierDailyStatService statService;
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public Response postRFIDLiftData(RFIDLiftData data) {
        if (data.getId() == null) {
            rfidLiftService.saveOrUpdate(data);
        } else {
            statService.updateSkierDailyStats(data.getDayNum(), data.getSkierId());
        }
        return Response.status(200).build();
    }
    
}
