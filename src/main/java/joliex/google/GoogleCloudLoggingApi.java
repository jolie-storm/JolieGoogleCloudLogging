package joliex.google;

import com.google.api.gax.paging.Page;
import com.google.cloud.MonitoredResource;
import com.google.cloud.logging.LogEntry;
import com.google.cloud.logging.Logging;
import com.google.cloud.logging.Payload;
import com.google.cloud.logging.Severity;

import java.util.Collections;
import java.util.LinkedHashMap;

public class GoogleCloudLoggingApi {


    public static void writeStructuredLog(Logging logging, String logName, String severity, LinkedHashMap<String, Object> logPayload) {


        LogEntry entry =
                LogEntry.newBuilder(Payload.JsonPayload.of(logPayload))
                        .setSeverity(Severity.valueOf(severity))
                        .setLogName(logName)
                        .setResource(MonitoredResource.newBuilder("global").build())
                        .build();
        logging.write(Collections.singleton(entry),
                Logging.WriteOption.logName(logName),
                Logging.WriteOption.resource(MonitoredResource.newBuilder("global").build()));
    }

    public static Page<LogEntry> readStructuredLog(Logging logging, String logName , String severity ) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("logName=projects/").append(logging.getOptions().getProjectId()).append("/logs/").append(logName).append(" AND severity=").append(severity);


        Page<LogEntry> logs = logging.listLogEntries(Logging.EntryListOption.filter(stringBuilder.toString()));
        return logs;
    }
}
