package in.ecstasy.app.Login;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import in.ecstasy.app.MainActivity;
import in.ecstasy.app.R;

public class ViewThemeVideoActivity extends AppCompatActivity {
    VideoView videoView;
    Button skip_btn;
    String videoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_preview);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN);
        videoPath = "android.resource://" + getPackageName() + "/" + R.raw.preview;
        videoView = findViewById(R.id.login_videoView);
        videoView.setVideoPath(videoPath);
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                    startActivity(new Intent(ViewThemeVideoActivity.this, MainActivity.class));
                finish();
            }
        });
        skip_btn = findViewById(R.id.login_skip_btn);
        skip_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (FirebaseAuth.getInstance().getCurrentUser() != null)
                    startActivity(new Intent(ViewThemeVideoActivity.this, MainActivity.class));
                finish();
            }
        });


    }
}