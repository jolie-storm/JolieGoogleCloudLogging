package joliex.google;

import jolie.runtime.Value;
import junit.framework.TestCase;

public class GoogleCloudLoggingServiceTest extends TestCase {

    public void testSyncWriteLog() {
        GoogleCloudLoggingService googleCloudLoggingService = new GoogleCloudLoggingService();
        Value request = Value.create();
        request.getFirstChild("logName").setValue("testJolieSync");
        request.getFirstChild("severity").setValue("ERROR");
        Value payload = Value.create();
        request.getFirstChild("payload").getNewChild("name").setValue("TestName");
        request.getFirstChild("payload").getNewChild("number").setValue(10);
        request.getFirstChild("payload").getNewChild("number").setValue(11);
        request.getFirstChild("payload").getFirstChild("subNode").getFirstChild("node1").setValue("aaaa");
        request.getFirstChild("payload").getFirstChild("subNode").getFirstChild("node2").setValue("bbb");
        googleCloudLoggingService.syncWriteLog(request);
    }

    public void testReadLogs() {
        GoogleCloudLoggingService googleCloudLoggingService = new GoogleCloudLoggingService();
        Value request = Value.create();
        request.getFirstChild("logName").setValue("testJolieSync");
        request.getFirstChild("severity").setValue("ERROR");
        Value logs =  googleCloudLoggingService.readLogs(request);
    }
}