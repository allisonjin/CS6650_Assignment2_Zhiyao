package edu.neu.zhiyao.assignment2.server.service;

import edu.neu.zhiyao.assignment2.server.dao.RFIDLiftDao;
import edu.neu.zhiyao.assignment2.server.entity.RFIDLiftData;
import java.util.List;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Path;

@Path("RFIDLiftService")
@Singleton
public class RFIDLiftService {
    
    @Inject
    private RFIDLiftDao dao;
    
    public void saveOrUpdate(RFIDLiftData data) {
        dao.saveOrUpdate(data);
    }
    
    public List<RFIDLiftData> findRFIDLiftData(int skierId, int dayNum) {
        return dao.findBySkierIdAndDayNum(skierId, dayNum);
    }
    
}

