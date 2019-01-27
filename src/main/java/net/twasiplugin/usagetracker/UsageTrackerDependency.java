package net.twasiplugin.usagetracker;

import net.twasi.core.logger.TwasiLogger;
import net.twasi.core.plugin.TwasiDependency;
import net.twasi.core.services.ServiceRegistry;
import net.twasi.core.services.providers.DataService;
import net.twasi.core.services.providers.TelegramService;
import net.twasiplugin.usagetracker.database.UsageStatisticsEntity;
import net.twasiplugin.usagetracker.database.UsageStatisticsRepository;
import net.twasiplugin.usagetracker.models.CPUStatistic;
import net.twasiplugin.usagetracker.models.RamStatistic;
import org.apache.commons.lang3.text.StrBuilder;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class UsageTrackerDependency extends TwasiDependency {

    @Override
    public void onActivate() {
        Thread main = new Thread(() -> {
            UsageStatisticsEntity entity = UsageStatisticsReader.buildEntity();
            TwasiLogger.log.info("Usage-Tracker is running. Current usage: " + formatUsageStatistics(entity));
            UsageStatisticsRepository repo = ServiceRegistry.get(DataService.class).get(UsageStatisticsRepository.class);
            repo.add(entity);
            repo.commitAll();
            while (true) {
                try {
                    TimeUnit.MINUTES.sleep(5);
                } catch (InterruptedException e) {
                    TwasiLogger.log.trace(e);
                }
                entity = UsageStatisticsReader.buildEntity();
                TwasiLogger.log.debug("Current usage: " + formatUsageStatistics(entity));
                repo.add(entity);
                repo.commitAll();
            }
        });
        main.setDaemon(true);
        main.start();
        TelegramService telegramService = ServiceRegistry.get(TelegramService.class);
        telegramService.registerCommandHandler(telegramService.new TelegramBotCommandHandler() {
            @Override
            public String getCommandName() {
                return "currentusage";
            }

            @Override
            public TelegramService.TelegramBotCommandHelpfile getHelpFile() {
                return telegramService.new TelegramBotCommandHelpfile("Print the system usage of the current running Twasi-instance");
            }

            @Override
            public void onCommand(String commandName, List<String> args, Message message, TelegramService.TelegramBotCommandHandlerAnswerInterface telegramBotCommandHandlerAnswerInterface) {
                try {
                    telegramBotCommandHandlerAnswerInterface.answer(formatUsageStatistics(UsageStatisticsReader.buildEntity()));
                } catch (TelegramApiException e) {
                    TwasiLogger.log.trace(e);
                }
            }
        });
    }

    public static String formatUsageStatistics(UsageStatisticsEntity entity) {
        RamStatistic ram = entity.getRam();
        CPUStatistic cpu = entity.getCpu();
        StrBuilder b = new StrBuilder();
        String spacer = "+======";
        for (int i = 0; i < 3; i++) spacer += spacer;
        spacer += "+";
        b.appendln("\n\n" + spacer);
        b.appendln("Usage-Statistics for Timestamp " + entity.getTimeStamp().toString());
        b.appendln(spacer);
        b.appendln("RAM:")
                .appendln("\t%s - Total RAM", ram.getFormattedSysMax())
                .appendln("\t%s - Total Free RAM", ram.getFormattedSysFree())
                .appendln("\t%s - JVM Max-RAM", ram.getFormattedJvmMax())
                .appendln("\t%s - JVM assigned", ram.getFormattedJvmAssigned())
                .appendln("\t%s - Twasi RAM-usage", ram.getFormattedJvmUsed());
        b.appendln(spacer);
        b.appendln("CPU:")
                .appendln("\t%d - Total Cores", cpu.getTotalCores())
                .appendln("\t%d - Total Threads", cpu.getTotalThreads())
                .appendln("\t%s - CPU-load", String.format("%.2f", cpu.getTotalCPULoad()))
                .appendln("\t%s - Average Core-load", String.format("%.2f", cpu.getAvgCoreLoad()))
                .appendln("\t%s - Max CPU-frequency", cpu.getFormattedMaxCPUFreq());
        b.appendln(spacer + "\n\n");
        return b.toString();
    }
}
