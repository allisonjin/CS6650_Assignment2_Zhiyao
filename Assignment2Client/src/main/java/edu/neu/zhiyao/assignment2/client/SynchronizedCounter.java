package edu.neu.zhiyao.assignment2.client;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author allisonjin
 */
public class SynchronizedCounter {
    private int reqCnt = 0;
    private int respCnt = 0;
//    List<Double> latencies = new ArrayList<>();
    Map<Long, List<Double>> latencyTimestamp = new HashMap<>();
    
    public synchronized int reqIncrement() {
        reqCnt++;
        return reqCnt - 1;
    }
    
//    public synchronized void respIncrement() {
//        respCnt++;
//    }
    
//    public synchronized void addLatency(double latency) {
//        latencies.add(latency);
//    }

    public synchronized void addLatencyAndTimestamp(long timestamp, 
            double latency) {
        List<Double> list = latencyTimestamp.getOrDefault(timestamp,
                new ArrayList<Double>());
        list.add(latency);
        latencyTimestamp.put(timestamp, list);
    }
    
//    public int getReqCnt() {
//        return reqCnt;
//    }
//
//    public int getRespCnt() {
//        return respCnt;
//    }

//    public List<Double> getLatencies() {
//        return latencies;
//    }    
    
    public Map<Long, List<Double>> getLatencyTimestamp() {
        return latencyTimestamp;
    }
    
}
