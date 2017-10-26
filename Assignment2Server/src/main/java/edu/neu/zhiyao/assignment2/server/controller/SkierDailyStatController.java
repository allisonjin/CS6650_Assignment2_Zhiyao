package edu.neu.zhiyao.assignment2.server.controller;

import edu.neu.zhiyao.assignment2.server.entity.SkierDailyStat;
import edu.neu.zhiyao.assignment2.server.service.SkierDailyStatService;
import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("myvert")
public class SkierDailyStatController {
    
    @Context
    UriInfo uriInfo;
    
    @Inject
    private SkierDailyStatService service;
        
    @GET
    @Path("/{skierId}/{dayNum}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSkierStat(@PathParam("skierId") int skierId, 
            @PathParam("dayNum") int dayNum) {
        SkierDailyStat stat = service.getSkierDailyStat(skierId, dayNum);
        if (stat != null) {
            return Response.status(200)
                    .entity(new Integer[]{stat.getTotalVert(), stat.getRideNum()})
                    .build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}
