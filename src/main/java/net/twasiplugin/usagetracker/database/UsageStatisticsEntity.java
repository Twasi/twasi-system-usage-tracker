package net.twasiplugin.usagetracker.database;

import net.twasi.core.database.models.BaseEntity;
import net.twasiplugin.usagetracker.models.CPUStatistic;
import net.twasiplugin.usagetracker.models.RamStatistic;
import org.mongodb.morphia.annotations.Entity;

import java.util.Date;

@Entity(value = "usagestatistics", noClassnameStored = true)
public class UsageStatisticsEntity extends BaseEntity {

    private Date timeStamp;

    private RamStatistic ram;
    private CPUStatistic cpu;

    public UsageStatisticsEntity(Date timeStamp, RamStatistic ram, CPUStatistic cpu) {
        this.timeStamp = timeStamp;
        this.ram = ram;
        this.cpu = cpu;
    }

    public UsageStatisticsEntity() {
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }

    public RamStatistic getRam() {
        return ram;
    }

    public void setRam(RamStatistic ram) {
        this.ram = ram;
    }

    public CPUStatistic getCpu() {
        return cpu;
    }

    public void setCpu(CPUStatistic cpu) {
        this.cpu = cpu;
    }
}
