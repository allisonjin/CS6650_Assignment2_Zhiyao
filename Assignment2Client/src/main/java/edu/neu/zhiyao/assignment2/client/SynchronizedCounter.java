package edu.neu.zhiyao.assignment2.client;

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
    List<Long> latencies = new ArrayList<>();
    Map<Long, List<Long>> latencyTimestamp = new HashMap<>();
    
    public synchronized void reqIncrement() {
        reqCnt ++;
    }
    
    public synchronized void respIncrement() {
        respCnt++;
    }

    public synchronized void addLatency(long latency) {
        latencies.add(latency);
    }
    
    public synchronized void addLatencyAndTimestamp(long timestamp, 
            long latency) {
        List<Long> list = latencyTimestamp.getOrDefault(timestamp,
                new ArrayList<Long>());
        list.add(latency);
        latencyTimestamp.put(timestamp, list);
    }
    
    public int getReqCnt() {
        return reqCnt;
    }

    public int getRespCnt() {
        return respCnt;
    }
    
    public List<Long> getLatencies() {
        return latencies;
    }
    
    public Map<Long, List<Long>> getLatencyTimestamp() {
        return latencyTimestamp;
    }
    
}
