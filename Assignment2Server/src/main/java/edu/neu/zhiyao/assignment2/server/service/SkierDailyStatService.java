package edu.neu.zhiyao.assignment2.server.service;

import edu.neu.zhiyao.assignment2.server.dao.SkierDailyStatDao;
import edu.neu.zhiyao.assignment2.server.entity.RFIDLiftData;
import edu.neu.zhiyao.assignment2.server.entity.SkierDailyStat;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Path;

@Path("SkierDaliyStatService")
@Singleton
public class SkierDailyStatService {
    
    @Inject
    private SkierDailyStatDao dao;
    
    @Inject
    private RFIDLiftService liftService;
    
    public SkierDailyStat getSkierDailyStat(int skierId, int dayNum) {
        return dao.find(skierId, dayNum);
    }
    
    public void createNewStats(int dayNum) {
        for (int i = 1; i <= 40000; i++) {
            SkierDailyStat stat = new SkierDailyStat(i, dayNum, 0, 0);
            dao.saveOrUpdate(stat);
        }
    }
    
    private SkierDailyStat getDailyStatFromRFID(int skierId, int dayNum) {
        List<RFIDLiftData> dataList = liftService.findRFIDLiftData(skierId, dayNum);
        int totalVert = 0, rideNum = 0;
        for (RFIDLiftData data : dataList) {
            totalVert += getVertByLiftId(data.getLiftId());
            rideNum++;
        }
        SkierDailyStat stat = new SkierDailyStat(skierId, dayNum, totalVert, rideNum);
        return stat;
    }
    
    private int getVertByLiftId(int liftId) {
        if (liftId <= 10) {
            return 200;
        } else if (liftId <= 20) {
            return 300;
        } else if (liftId <= 30) {
            return 400;
        } else {
            return 500;
        }
    }
    
    public void updateSkierDailyStats(int dayNum, int n) {
        for (int i = 1; i <= n; i++) {
            SkierDailyStat stat = getDailyStatFromRFID(i, dayNum);
            dao.saveOrUpdate(stat);
        }
    }
    
}

