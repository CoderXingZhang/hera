package com.dfire.core.netty.worker;

import com.dfire.common.service.HeraDebugHistoryService;
import com.dfire.common.service.HeraGroupService;
import com.dfire.common.service.HeraJobActionService;
import com.dfire.common.service.HeraJobHistoryService;
import com.dfire.core.job.Job;
import io.netty.channel.Channel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.concurrent.BasicThreadFactory;
import org.springframework.context.ApplicationContext;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 * @author: <a href="mailto:lingxiao@2dfire.com">凌霄</a>
 * @time: Created in 11:30 2018/1/10
 * @desc
 */
@Data
@NoArgsConstructor
public class WorkContext {

    public static String host;
    public static Integer cpuCoreNum;
    public String serverHost;
    private Channel serverChannel;
    private Map<String, Job> running = new ConcurrentHashMap<String, Job>();
    private Map<String, Job> manualRunning = new ConcurrentHashMap<String, Job>();
    private Map<String, Job> debugRunning = new ConcurrentHashMap<String, Job>();
    private WorkHandler handler;
    private WorkClient workClient;
    private ScheduledExecutorService workThreadPool = new ScheduledThreadPoolExecutor(8,
            new BasicThreadFactory.Builder().namingPattern("WorkContext-schedule-pool-%d").daemon(true).build());

    private ApplicationContext applicationContext;

    static {
        try {
            host = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        cpuCoreNum = Runtime.getRuntime().availableProcessors();
    }

    public HeraDebugHistoryService getDebugHistoryService() {
        return (HeraDebugHistoryService) applicationContext.getBean("heraDebugHistoryService");
    }

    public HeraJobHistoryService getJobHistoryService() {
        return (HeraJobHistoryService) applicationContext.getBean("heraJobHistoryService");
    }

    public HeraGroupService getHeraGroupService() {
        return (HeraGroupService) applicationContext.getBean("heraGroupService");
    }

    public HeraJobActionService getHeraJobActionService() {
        return (HeraJobActionService) applicationContext.getBean("heraJobActionService");
    }


}
