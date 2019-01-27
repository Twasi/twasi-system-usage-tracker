package net.twasiplugin.dependency.usagetracker;

import net.twasiplugin.dependency.usagetracker.models.CPUStatistic;
import net.twasiplugin.dependency.usagetracker.models.RamStatistic;
import net.twasiplugin.dependency.usagetracker.database.UsageStatisticsEntity;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.util.Util;

import java.util.Calendar;
import java.util.Date;

import static java.lang.Runtime.getRuntime;

public class UsageStatisticsReader {

    public static UsageStatisticsEntity buildEntity() {
        Date timestamp = Calendar.getInstance().getTime();
        SystemInfo info = new SystemInfo();

        RamStatistic ram = getRam(info);
        CPUStatistic cpu = getCPU(info);

        return new UsageStatisticsEntity(timestamp, ram, cpu);
    }

    private static RamStatistic getRam(SystemInfo info) {
        Runtime rt = getRuntime();
        long sysMax = getMaxSysRam(info);
        long sysFree = getFreeSysRam(info);
        long max = rt.maxMemory();
        long assigned = rt.totalMemory();
        long free = rt.freeMemory();
        return new RamStatistic(sysMax, sysFree, max, assigned, free);
    }

    private static CPUStatistic getCPU(SystemInfo info) {
        int totalCores = getCoreAmount(info);
        int totalThreads = getThreadAmount(info);
        double totalCPULoad = getSystemLoadInPercent(info);
        double avgCoreLoad = getAverageCoreLoadInPercent(info);
        long maxCPUFreq = getCPUMaxFrequency(info);
        return new CPUStatistic(totalCores, totalThreads, totalCPULoad, avgCoreLoad, maxCPUFreq);
    }

    private static long getMaxSysRam(SystemInfo info) {
        return info.getHardware().getMemory().getTotal();
    }

    private static long getFreeSysRam(SystemInfo info) {
        return info.getHardware().getMemory().getAvailable();
    }

    private static int getCoreAmount(SystemInfo info) {
        return info.getHardware().getProcessor().getPhysicalProcessorCount();
    }

    private static int getThreadAmount(SystemInfo info) {
        return info.getHardware().getProcessor().getLogicalProcessorCount();
    }

    private static double getSystemLoadInPercent(SystemInfo info) {
        CentralProcessor processor = info.getHardware().getProcessor();
        int i = 0;
        while (processor.getSystemCpuLoad() == 0 && i++ < 20) Util.sleep(100);
        return processor.getSystemCpuLoad() * 100;
    }

    private static double getAverageCoreLoadInPercent(SystemInfo info) {
        double[] coreLoad = info.getHardware().getProcessor().getProcessorCpuLoadBetweenTicks();
        double avg = 0;
        for (double d : coreLoad) avg += d;
        avg = avg / coreLoad.length;
        return avg * 100;
    }

    private static long getCPUMaxFrequency(SystemInfo info) {
        return info.getHardware().getProcessor().getVendorFreq();
    }
}
