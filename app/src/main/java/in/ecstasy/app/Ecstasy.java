package in.ecstasy.app;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;

/**
 * Created By Shivam Gupta on 17-06-2021 of package in.ecstasy.app
 */
public class Ecstasy extends Application {
    public static final String CHANNEL_ID = "NotificationChannel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel();
    }

    private void createNotificationChannel() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Ecstasy",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Ecstasy Notifications");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }
    }

}
