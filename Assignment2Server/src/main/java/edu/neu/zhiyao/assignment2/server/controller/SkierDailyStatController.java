/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.neu.zhiyao.assignment2.server.controller;

import edu.neu.zhiyao.assignment2.server.entity.SkierDailyStat;
import edu.neu.zhiyao.assignment2.server.service.SkierDailyStatService;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

@Path("myvert")
public class SkierDailyStatController {
    
    @Context
    UriInfo uriInfo;
    
    private SkierDailyStatService service = new SkierDailyStatService();
        
    @GET
    @Path("/{skierId}/{dayNum}")
    @Produces(MediaType.APPLICATION_JSON)
    public SkierDailyStat getSkierStat(@PathParam("skierId") int skierId, 
            @PathParam("dayNum") int dayNum) {
        return service.getSkierDailyStat(skierId, dayNum);
    }
}
