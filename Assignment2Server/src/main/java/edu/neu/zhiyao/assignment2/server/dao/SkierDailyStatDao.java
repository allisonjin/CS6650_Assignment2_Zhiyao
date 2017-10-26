package edu.neu.zhiyao.assignment2.server.dao;

import edu.neu.zhiyao.assignment2.server.entity.SkierDailyStat;
import javax.inject.Singleton;
import javax.ws.rs.Path;

@Path("SkierDailyStatDao")
@Singleton
public class SkierDailyStatDao extends BaseDao<SkierDailyStat> {
    
    public SkierDailyStatDao() {
        super(SkierDailyStat.class);
    }
    
}
