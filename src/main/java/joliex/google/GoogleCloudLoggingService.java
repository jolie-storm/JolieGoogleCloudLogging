package joliex.google;


import com.google.api.gax.paging.Page;
import com.google.cloud.logging.*;
import com.google.common.collect.Lists;
import com.google.protobuf.Struct;
import com.google.protobuf.util.Structs;
import jolie.js.JsUtils;
import jolie.runtime.AndJarDeps;
import jolie.runtime.JavaService;
import jolie.runtime.Value;
import jolie.runtime.embedding.RequestResponse;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@AndJarDeps( { "jolie-js.jar", "json_simple.jar" } )

public class GoogleCloudLoggingService extends JavaService {
    private static Logging logging;

    public GoogleCloudLoggingService() {
        logging = LoggingOptions.getDefaultInstance().getService();
    }

    @RequestResponse
    public void syncWriteLog(Value request) {
        logging.setWriteSynchronicity(Synchronicity.SYNC);
        Value payloadValue = request.getFirstChild("payload");
        LinkedHashMap<String, Object> payload = parseValue(payloadValue);
        GoogleCloudLoggingApi.writeStructuredLog(logging ,
                request.getFirstChild("logName").strValue(),
                request.getFirstChild("severity").strValue(),
                payload);

    }

    @RequestResponse
    public void asyncWriteLog(Value request){

        logging.setWriteSynchronicity(Synchronicity.ASYNC);
        Value payloadValue = request.getFirstChild("payload");
        LinkedHashMap<String, Object> payload = parseValue(payloadValue);
        GoogleCloudLoggingApi.writeStructuredLog(logging ,
                request.getFirstChild("logName").strValue(),
                request.getFirstChild("severity").strValue(),
                payload);
        logging.flush();

    }
    
    @RequestResponse
    public Value readLogs(Value request){
        Value response = Value.create();
        Page<LogEntry> logs = GoogleCloudLoggingApi.readStructuredLog(logging, request.getFirstChild("logName").strValue(), request.getFirstChild("severity").strValue());

        do {
            for (LogEntry logEntry : logs.iterateAll()) {
                Value log = Value.create();
                Payload.JsonPayload jsonPayload = (Payload.JsonPayload) logEntry.getPayload();
                Map<String, Object> payloadMap = jsonPayload.getDataAsMap();
                Value payloadValue = parsePayload(payloadMap);
                log.getFirstChild("payload").deepCopy(payloadValue);
                log.getFirstChild("logName").setValue(logEntry.getLogName());
                log.getFirstChild("insertId").setValue(logEntry.getInsertId());
                log.getFirstChild("timestamp").setValue(logEntry.getReceiveTimestamp());
                log.getFirstChild("severity").setValue(logEntry.getSeverity().toString());
                response.getChildren("logs").add(log);


            }
            logs = logs.getNextPage();
        } while (logs != null);

        return response;

    }


    private Value parsePayload(Map<String, Object> payloadMap){
        Value response = Value.create();
        payloadMap.forEach((s, o) -> {

                 if ((o instanceof String) || (o instanceof Integer) || (o instanceof Long) || (o instanceof Double) || (o instanceof Boolean)){
                         response.getFirstChild(s).setValue(o);
                 }else if (o instanceof List){
                        List<Object> listO  = (List) o;
                        listO.forEach(o1 -> {
                            response.getNewChild(s).setValue(o1);
                        });
                 }else if (o instanceof Map){
                          Map mapO = (Map) o;
                          response.getNewChild(s).deepCopy(parsePayload(mapO));
                }
            });
       return response;
    }

    private LinkedHashMap<String, Object> parseValue(Value payload) {
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();

        payload.children().forEach((s, values) -> {
            if (values.size() > 1) {
                ArrayList<Object> list = new ArrayList<>();
                values.forEach(
                        value -> {
                            if (value.isBool()) {
                                list.add(Boolean.valueOf(value.boolValue()));
                            } else if (value.isString()) {
                                list.add(value.strValue());
                            } else if (value.isLong()) {
                                list.add(Long.valueOf(value.longValue()));
                            } else if (value.isDouble()) {
                                list.add(Double.valueOf(value.doubleValue()));
                            } else if (value.isInt()) {
                                list.add(Integer.valueOf(value.intValue()));
                            } else if (!value.isDefined()) {
                                if (value.hasChildren()) {
                                   list.add(parseValue(value));
                                }
                            }
                        });
                linkedHashMap.put(s,list);

            } else {
              Value value = values.get(0);
                if (value.isBool()) {
                    linkedHashMap.put(s ,Boolean.valueOf(value.boolValue()));
                } else if (value.isString()) {
                    linkedHashMap.put(s, value.strValue());
                } else if (value.isLong()) {
                    linkedHashMap.put(s ,Long.valueOf(value.longValue()));
                } else if (value.isDouble()) {
                    linkedHashMap.put(s ,Double.valueOf(value.doubleValue()));
                } else if (value.isInt()) {
                    linkedHashMap.put(s, Integer.valueOf(value.intValue()));
                } else if (!value.isDefined()) {
                    if (value.hasChildren()) {
                        linkedHashMap.put(s,parseValue(value));
                    }
                }
            }
        });
        return linkedHashMap;
    }




}

