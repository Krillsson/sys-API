package se.christianjensen.maintenance.push;

import com.phonedeck.gcm4j.Gcm;
import org.junit.Before;
import org.mockito.Mockito;
import se.christianjensen.maintenance.representation.internal.Device;

import java.util.HashMap;
import java.util.Map;

public class GcmSenderTest {
    GcmSender gcmSender;
    Gcm gcmMock;


    @Before
    public void setUp() throws Exception {
        gcmMock = Mockito.mock(Gcm.class);
        gcmSender = new GcmSender(gcmMock);
    }


    public void sendingPushMessagesShouldWork() throws Exception {
        Device device = new Device(123, "123");
        Map<String, String> payload = new HashMap<>();
        gcmSender.send(device, payload);
    }

}