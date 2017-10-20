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

/**
 *
 * @author allisonjin
 */
public class SkierClientMain {

    private final int nThreads;
    private final SkierClient client;

    public SkierClientMain(int nThreads) {
        this.nThreads = nThreads;
        client = new SkierClient();
    }

    private List<RFIDLiftData> readCSV(String fileName) {
        List<RFIDLiftData> dataList = new ArrayList<>();
        try {
            ClassLoader classLoader = getClass().getClassLoader();
            File file = new File(classLoader.getResource(fileName).getFile());
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            bufferedReader.readLine();
            while ((line = bufferedReader.readLine()) != null) {
                String[] rawData = line.trim().split(",");
                int resortId = Integer.parseInt(rawData[0]);
                int dayNum = Integer.parseInt(rawData[1]);
                int skierId = Integer.parseInt(rawData[2]);
                int liftId = Integer.parseInt(rawData[3]);
                int time = Integer.parseInt(rawData[4]);
                RFIDLiftData data = new RFIDLiftData(resortId, dayNum,
                        skierId, liftId, time);
                dataList.add(data);
            }
            fileReader.close();
            bufferedReader.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(SkierClientMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(SkierClientMain.class.getName()).log(Level.SEVERE, null, ex);
        }
        return dataList;
    }

    private void runTask(Runnable task) {
        ExecutorService executor = Executors.newFixedThreadPool(nThreads);
        for (int i = 0; i < nThreads; i++) {
            executor.execute(task);
        }
        executor.shutdown();
        while (!executor.isTerminated());
    }

    private Map<Long, List<Double>> runPostTask(final List<RFIDLiftData> rfidLiftDataList) {
        final SynchronizedCounter counter = new SynchronizedCounter();
        final long startTime = System.currentTimeMillis();
        runTask(new Runnable() {
            @Override
            public void run() {
                int index;
                while ((index = counter.reqIncrement()) < rfidLiftDataList.size()) {
                    System.out.println(index);
                    RFIDLiftData data = rfidLiftDataList.get(index);
                    long reqStartTime = System.currentTimeMillis();
                    client.postRFIDLiftData(data);
                    long reqEndTime = System.currentTimeMillis();
                    double latency = elapsedTime(reqStartTime, reqEndTime);
                    counter.addLatencyAndTimestamp(reqStartTime - startTime, latency);
                }
            }
        });
        return counter.getLatencyTimestamp();
    }

    private double elapsedTime(long startTime, long endTime) {
        return (endTime - startTime) / 1000.0;
    }

    private void outputCSV(Map<Long, List<Double>> latencyTimestamp,
            String outputFilerName) {
        FileWriter writer;
        try {
            long time = System.currentTimeMillis();
            writer = new FileWriter(time + outputFilerName);
            writer.write("Time,Latency\n");
            for (Map.Entry<Long, List<Double>> e : latencyTimestamp.entrySet()) {
                Long timestamp = e.getKey();
                List<Double> latencies = e.getValue();
                for (Double latency : latencies) {
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
        if (args.length != 0) {
            try {
                nThreads = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input!");
            }
        }
        SkierClientMain instance = new SkierClientMain(nThreads);
        String csvFileName = "BSDSAssignment2Day1.csv";
        List<RFIDLiftData> dataList = instance.readCSV(csvFileName);
        Map<Long, List<Double>> latencyTimestamp = instance.runPostTask(dataList);
        instance.outputCSV(latencyTimestamp, "latencies.csv");
    }

}
