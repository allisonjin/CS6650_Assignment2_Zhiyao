package edu.neu.zhiyao.assignment2.server.service;

import edu.neu.zhiyao.assignment2.server.dao.SkierDailyStatDao;
import edu.neu.zhiyao.assignment2.server.entity.RFIDLiftData;
import edu.neu.zhiyao.assignment2.server.entity.SkierDailyStat;
import java.util.ArrayList;
import java.util.List;

public class SkierDailyStatService {
    
    private SkierDailyStatDao dao = new SkierDailyStatDao();
    
    public SkierDailyStat getSkierDailyStat(int skierId, int dayNum) {
        return dao.find(dayNum, dayNum);
    }
    
    public void createNewStats(int dayNum) {
        for (int i = 1; i <= 40000; i++) {
            SkierDailyStat stat = new SkierDailyStat(i, dayNum, 0, 0);
            dao.saveOrUpdate(stat);
        }
    }
    
    private List<SkierDailyStat> getDailyStats(int dayNum) {
        RFIDLiftService liftService = new RFIDLiftService();
        List<SkierDailyStat> stats = new ArrayList<>();
        for (int i = 1; i <= 40000; i++) {
            List<RFIDLiftData> dataList = liftService.findRFIDLiftData(i, dayNum);
            int totalVert = 0, rideNum = 0;
            for (RFIDLiftData data : dataList) {
                totalVert += getVertByLiftId(data.getLiftId());
                rideNum++;
            }
            SkierDailyStat stat = new SkierDailyStat(i, dayNum, totalVert, rideNum);
            stats.add(stat);
        }
        return stats;
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
    
    public void updateSkierDailyStats(int dayNum) {
        updateSkierDailyStats(getDailyStats(dayNum));
    }
    
    private void updateSkierDailyStats(List<SkierDailyStat> stats) {
        for (SkierDailyStat stat : stats) {
            dao.saveOrUpdate(stat);
        }
    }
    
}

