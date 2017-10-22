package edu.neu.zhiyao.assignment2.server.service;

import edu.neu.zhiyao.assignment2.server.dao.RFIDLiftDao;
import edu.neu.zhiyao.assignment2.server.entity.RFIDLiftData;
import java.util.List;

public class RFIDLiftService {
    
    private RFIDLiftDao dao = new RFIDLiftDao();
    
    public void saveOrUpdate(RFIDLiftData data) {
        dao.saveOrUpdate(data);
    }
    
    public List<RFIDLiftData> findRFIDLiftData(int skierId, int dayNum) {
        return dao.findBySkierIdAndDayNum(skierId, dayNum);
    }
    
}

