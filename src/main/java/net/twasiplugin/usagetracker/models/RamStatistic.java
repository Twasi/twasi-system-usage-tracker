package net.twasiplugin.usagetracker.models;

import static oshi.util.FormatUtil.formatBytes;

public class RamStatistic {

    private long sysMax; // How much RAM is available on the current machine
    private long sysFree; // How much of that ram is available
    private long sysUsed; // and how much of it is in use

    private long jvmMax; // How much RAM the JVM gets from the OS
    private long jvmAssigned; // How much of that RAM currently is assigned for new Objects
    private long jvmFree; // How much of that RAM is currently free
    private long jvmUsed; // How much RAM Twasi is currently using

    public RamStatistic(long sysMax, long sysFree, long jvmMax, long jvmAssigned, long jvmFree) {
        this.sysMax = sysMax;
        this.sysFree = sysFree;
        sysUsed = sysMax - sysFree;
        this.jvmMax = jvmMax;
        this.jvmAssigned = jvmAssigned;
        this.jvmFree = jvmFree;
        jvmUsed = jvmAssigned - jvmFree;
    }

    public long getJvmMax() {
        return jvmMax;
    }

    public String getFormattedJvmMax() {
        return formatBytes(jvmMax);
    }

    public long getJvmAssigned() {
        return jvmAssigned;
    }

    public String getFormattedJvmAssigned() {
        return formatBytes(jvmAssigned);
    }

    public long getJvmFree() {
        return jvmFree;
    }

    public String getFormattedJvmFree() {
        return formatBytes(jvmFree);
    }

    public long getJvmUsed() {
        return jvmUsed;
    }

    public String getFormattedJvmUsed() {
        return formatBytes(jvmUsed);
    }

    public long getSysMax() {
        return sysMax;
    }

    public String getFormattedSysMax() {
        return formatBytes(sysMax);
    }

    public long getSysFree() {
        return sysFree;
    }

    public String getFormattedSysFree() {
        return formatBytes(sysFree);
    }

    public long getSysUsed() {
        return sysUsed;
    }

    public String getFormattedSysUsed() {
        return formatBytes(sysUsed);
    }
}
