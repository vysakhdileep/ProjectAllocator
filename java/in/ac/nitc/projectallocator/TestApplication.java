package in.ac.nitc.projectallocator;

import android.app.Application;

/**
 * Created by Vysakh Dileep on 16-03-2018.
 */

public class TestApplication extends Application {

    private static TestApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized TestApplication getInstance() {
        return mInstance;
    }

    public void setConnectionListener(ConnectionReceiver.ConnectionReceiverListener listener) {
        ConnectionReceiver.connectionReceiverListener = listener;
    }
}
