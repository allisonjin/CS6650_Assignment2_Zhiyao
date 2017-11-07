/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.neu.zhiyao.assignment2.client;

import edu.neu.zhiyao.assignment2.client.entity.RFIDLiftData;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;

/**
 *
 * @author allisonjin
 */
public class SkierClientMain {

    private static final int BAR_NUM = 100;
    private static final int SKIER_NUM_EACH_BAR = 400;
    private static final int GET_CLIENT = 1;
    private static final int POST_CLIENT = 2;
    
    private final int nThreads;
    private final int dayNum;
    private final SkierClient client;
    private final SynchronizedCounter counter;

    public SkierClientMain(int nThreads, int dayNum) {
        this.nThreads = nThreads;
        this.dayNum = dayNum;
        client = new SkierClient();
        counter = new SynchronizedCounter();
    }

    private List<RFIDLiftData> readCSV(String fileName) {
        List<RFIDLiftData> dataList = new ArrayList<>();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            System.out.println("Loading CSV file...");
            String line;
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] rawData = line.trim().split(",");
                int resortId = Integer.parseInt(rawData[0]);
                int day = Integer.parseInt(rawData[1]);
                int skierId = Integer.parseInt(rawData[2]);
                int liftId = Integer.parseInt(rawData[3]);
                int time = Integer.parseInt(rawData[4]);
                RFIDLiftData data = new RFIDLiftData(resortId, day,
                        skierId, liftId, time);
                dataList.add(data);
            }
            System.out.println("Load succeeded!");
            fileReader.close();
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SkierClientMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SkierClientMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataList;
    }
    
    private Map<Long, List<Long>> testPostRequests(final List<RFIDLiftData> rfidLiftDataList) {
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        final long startTime = System.currentTimeMillis();
        System.out.println("Post Client starting... Time: " + startTime);
        for (int i = 0; i < 0; i++) {
            final int index = i;
            final RFIDLiftData data = rfidLiftDataList.get(i);
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    Response resp = null;
                    long reqStartTime = System.currentTimeMillis();
                    try {
                        counter.reqIncrement();
                        resp = client.postRFIDLiftData(data);
                    } catch (Exception ex) {
                        System.out.println(ex.getMessage());
                    }
                    long reqEndTime = System.currentTimeMillis();
                    long latency = elapsedTime(reqStartTime, reqEndTime);
//                    System.out.println(index + ": " + latency);
                    if (resp != null && resp.getStatus() == 200) {
                        counter.respIncrement();
                    }
                    counter.addLatency(latency);
                    counter.addLatencyAndTimestamp(reqStartTime - startTime, latency);
                }
            });
        }
        
        executor.shutdown();
        while (!executor.isTerminated());
        try {
            client.postEndOfData(dayNum);
            System.out.println("EOF");
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        
        long endTime = System.currentTimeMillis();
        long wallTime = elapsedTime(startTime, endTime);
        System.out.println("All threads complete... Time: " + endTime);
        
        printStats(wallTime);
        
        return counter.getLatencyTimestamp();
    }
    
    private Map<Long, List<Long>> testGetRequests() {
        ExecutorService executor = Executors.newFixedThreadPool(BAR_NUM);
        final long startTime = System.currentTimeMillis();
        System.out.println("Get Client starting... Time: " + startTime);
        for (int i = 1; i <= BAR_NUM; i++) {
            final int index = i;
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    for (int j = 1; j <= SKIER_NUM_EACH_BAR; j++) {
                        int skierId = SKIER_NUM_EACH_BAR * (index - 1) + j;
                        long reqStartTime = System.currentTimeMillis();
                        Response resp = null;
                        try {
                            counter.reqIncrement();
                            resp = client.getSkierDailyStat(skierId, dayNum);
                        } catch (Exception ex) {
                            System.out.println(ex.getMessage());
                        }
                        long reqEndTime = System.currentTimeMillis();
                        long latency = elapsedTime(reqStartTime, reqEndTime);
//                        System.out.println(skierId + ": " + latency);
                        if (resp != null && resp.getStatus() == 200) {
                            counter.respIncrement();
//                            Integer[] res = resp.readEntity(Integer[].class);
//                            System.out.println(skierId + ": " + res[0] + " " + res[1]);
                        }
                        counter.addLatency(latency);
                        counter.addLatencyAndTimestamp(reqStartTime - startTime, latency);
                    }
                }
            });
        }
        System.out.println("All threads running...");
        
        executor.shutdown();
        while (!executor.isTerminated());
        
        long endTime = System.currentTimeMillis();
        long wallTime = elapsedTime(startTime, endTime);
        System.out.println("All threads complete... Time: " + endTime);
        
        printStats(wallTime);
        
        return counter.getLatencyTimestamp();
    }

    private static long elapsedTime(long startTime, long endTime) {
        return endTime - startTime;
    }
    
    private void printStats(long wallTime) {
        System.out.println("Total number of requests sent: "
                + counter.getRespCnt());
        System.out.println("Total number of successful responses: "
                + counter.getRespCnt());
        System.out.format("Test wall time: %d ms\n", wallTime);
        
        processLatencies(counter.getLatencies());
    }

    private void processLatencies(List<Long> latencyList) {
        double[] latencies = new double[latencyList.size()];
        for (int i = 0; i < latencyList.size(); i++) {
            latencies[i] = (double)latencyList.get(i);
        }
        System.out.format("Mean lantency: %.1f ms\n", mean(latencies));
        System.out.format("Median latency: %.1f ms\n", median(latencies));
        System.out.format("99th percentile latency: %.1f ms\n", 
                percentile(latencies, 99));
        System.out.format("95th percentile latency: %.1f ms\n", 
                percentile(latencies, 95));
    }
    
    private double mean(double[] values) {
        double ans = 0;
        for (double v : values) {
            ans += v;
        }
        return ans / values.length;
    }
    
    private double median(double[] values) {
        int n = values.length;
        int mid = (n - 1) / 2;
        return n % 2 == 0 ? (values[mid] - values[mid + 1]) / 2 + values[mid + 1] :
                values[mid];
    }
    
    private double percentile(double[] values, double percentile) {
        Percentile p = new Percentile();
        return p.evaluate(values, percentile);
    }
    
    private void outputCSV(Map<Long, List<Long>> latencyTimestamp,
            String outputFilerName) {
        FileWriter writer;
        try {
            writer = new FileWriter(outputFilerName);
            System.out.println("Writing csv...");
            writer.write("Time,Latency\n");
            for (Map.Entry<Long, List<Long>> e : latencyTimestamp.entrySet()) {
                Long timestamp = e.getKey();
                List<Long> latencies = e.getValue();
                for (Long latency : latencies) {
                    writer.append(timestamp + "," + latency + "\n");
                }
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(SkierClientMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static void main(String[] args) {
        int nThreads = 100;
        int dayNum = 2;
        int clientType = POST_CLIENT;
        if (args.length != 0) {
            try {
                dayNum = Integer.parseInt(args[0]);
                clientType = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            }
        }
        SkierClientMain instance = new SkierClientMain(nThreads, dayNum);
        long allStartTime = System.currentTimeMillis();
        Map<Long, List<Long>> latencyTimestamp;
        if (clientType == GET_CLIENT) {
            latencyTimestamp = instance.testGetRequests();
        } else {
            String csvFileName = "BSDSAssignment2Day" + dayNum + ".csv";
            List<RFIDLiftData> dataList = instance.readCSV(csvFileName);
            latencyTimestamp = instance.testPostRequests(dataList);
        }
        long allEndTime = System.currentTimeMillis();
        long wallTime = elapsedTime(allStartTime, allEndTime);
        instance.outputCSV(latencyTimestamp, clientType + "-" + wallTime + ".csv");
    }

}
