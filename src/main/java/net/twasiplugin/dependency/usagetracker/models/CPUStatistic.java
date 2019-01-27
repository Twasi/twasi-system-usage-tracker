package net.twasiplugin.dependency.usagetracker.models;

import java.text.NumberFormat;

import static oshi.util.FormatUtil.formatHertz;

public class CPUStatistic {

    private int totalCores;
    private int totalThreads;

    private double totalCPULoad;
    private double avgCoreLoad;

    private long maxCPUFreq;

    public CPUStatistic(int totalCores, int totalThreads, double totalCPULoad, double avgCoreLoad, long maxCPUFreq) {
        this.totalCores = totalCores;
        this.totalThreads = totalThreads;
        this.totalCPULoad = totalCPULoad;
        this.avgCoreLoad = avgCoreLoad;
        this.maxCPUFreq = maxCPUFreq;
    }

    public int getTotalCores() {
        return totalCores;
    }

    public int getTotalThreads() {
        return totalThreads;
    }

    public double getTotalCPULoad() {
        return totalCPULoad;
    }

    public String getFormattedTotalCPULoad() {
        return toPercent(totalCPULoad);
    }

    public double getAvgCoreLoad() {
        return avgCoreLoad;
    }

    public String getFormattedAvgCoreLoad() {
        return toPercent(avgCoreLoad);
    }

    public long getMaxCPUFreq() {
        return maxCPUFreq;
    }

    public String getFormattedMaxCPUFreq() {
        return formatHertz(maxCPUFreq);
    }

    private static String toPercent(double d) {
        NumberFormat formatter = NumberFormat.getInstance();
        formatter.setMaximumFractionDigits(2);
        return formatter.format(d) + "%";
    }
}
