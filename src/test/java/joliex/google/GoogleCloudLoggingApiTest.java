package joliex.google;

import com.google.cloud.logging.Logging;
import com.google.cloud.logging.LoggingOptions;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GoogleCloudLoggingApiTest extends TestCase {

    public void testWriteStructuredLog() throws Exception {




        LinkedHashMap<String, Object> logPayload = new LinkedHashMap<> ();
        logPayload.put("Somedata",Long.valueOf(10000l));
        logPayload.put("TestContent","This.is a test");

        Logging logging = LoggingOptions.getDefaultInstance().getService();
        GoogleCloudLoggingApi.writeStructuredLog(logging,"test","ERROR",logPayload);

        logging.flush();
        logging.close();

    }

    public void testReadStructuredLog() {
        Logging logging = LoggingOptions.getDefaultInstance().getService();
        GoogleCloudLoggingApi.readStructuredLog(logging,"test", "ERROR");
    }
}