package se.christianjensen.maintenance.push;


import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.phonedeck.gcm4j.*;
import se.christianjensen.maintenance.representation.internal.Device;

import java.io.IOException;
import java.util.Map;

public class GcmSender {

    Gcm gcmClient;

    public GcmSender(Gcm gcmClient) {
        this.gcmClient = gcmClient;
    }


    public void send(Device device, Map<String, String> data) {
        GcmRequest request = new GcmRequest()
                .withRegistrationId(device.getGcmId())
                .withCollapseKey("sync")
                .withDelayWhileIdle(true)
                .withData(data)
                .withDryRun(true);

        // send the request asynchronously
        ListenableFuture<GcmResponse> responseFuture = gcmClient.send(request);

        // wait for and process the response
        Futures.addCallback(responseFuture, new FutureCallback<GcmResponse>() {
            public void onSuccess(GcmResponse response) {
                for (Result result : response.getResults()) {
                    if (result.getError() == null) {
                        System.out.println("Aaaaalright");
                    } else {
                        switch (result.getError()) {
                            case MISSING_REGISTRATION:
                                break;
                            case INVALID_REGISTRATION:
                                break;
                            case MISMATCH_SENDER_ID:
                                break;
                            case NOT_REGISTERED:
                                break;
                            case MESSAGE_TOO_BIG:
                                break;
                            case INVALID_DATA_KEY:
                                break;
                            case INVALID_TTL:
                                break;
                            case INTERNAL_SERVER_ERROR:
                                break;
                            case UNAVAILABLE:
                                break;
                            case INVALID_PACKAGE_NAME:
                                break;
                            case UNSUPPORTED_ERROR_CODE:
                                break;
                        }
                        System.err.println("Error " + result.getError() + " occured for registration id " + result.getRequestedRegistrationId());
                    }
                }
            }

            public void onFailure(Throwable t) {
                if (t instanceof GcmNetworkException) {
                    // GCM server gave a non-200 HTTP response
                    GcmNetworkException gne = (GcmNetworkException) t;
                    System.err.println("GCM Server responded with and error: " + gne.getCode() + " " + gne.getResponse());
                } else if (t instanceof IOException) {
                    // host not found or net is down
                    System.err.println("Network error occured: " + t);
                } else {
                    // should not happen during normal operation
                    System.err.println("Unknown error occured: " + t);
                }
            }
        });

    }
}
