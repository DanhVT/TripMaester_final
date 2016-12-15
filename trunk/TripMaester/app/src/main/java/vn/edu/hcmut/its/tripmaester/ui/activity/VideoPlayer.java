package vn.edu.hcmut.its.tripmaester.ui.activity;

import android.media.MediaCodec;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.exoplayer.ExoPlayer;
import com.google.android.exoplayer.MediaCodecAudioTrackRenderer;
import com.google.android.exoplayer.MediaCodecSelector;
import com.google.android.exoplayer.MediaCodecVideoTrackRenderer;
import com.google.android.exoplayer.MediaFormat;
import com.google.android.exoplayer.extractor.ExtractorSampleSource;
import com.google.android.exoplayer.upstream.Allocator;
import com.google.android.exoplayer.upstream.DataSource;
import com.google.android.exoplayer.upstream.DefaultAllocator;
import com.google.android.exoplayer.upstream.DefaultUriDataSource;
import com.google.android.exoplayer.util.MimeTypes;

import java.util.Formatter;
import java.util.Locale;

import vn.edu.hcmut.its.tripmaester.R;

public class VideoPlayer extends AppCompatActivity {
    private SurfaceView surfaceView;
    private ExoPlayer exoPlayer;
    private boolean bAutoplay=true;
    private boolean bIsPlaying=false;
    private boolean bControlsActive=true;
    private ImageButton btnPlay;
    private ImageButton btnPause;
    private ImageButton btnFwd;
    private ImageButton btnPrev;
    private ImageButton btnRew;
    private ImageButton btnNext;
    private ImageButton btnSettings;
    private RelativeLayout loadingPanel;
    private int RENDERER_COUNT = 300000;
    private int minBufferMs =    250000;

    private final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private final int BUFFER_SEGMENT_COUNT = 256;
    private LinearLayout mediaController;
    private SeekBar seekPlayerProgress;
    private Handler handler;
    private TextView txtCurrentTime;
    private TextView txtEndTime;
    private StringBuilder mFormatBuilder;
    private Formatter mFormatter;
    private String HLSurl = "http://walterebert.com/playground/video/hls/sintel-trailer.m3u8";
    private String mp4URL = "http://player.hungama.com/mp3/91508493.mp4";
    private String userAgent = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10.11; rv:40.0) Gecko/20100101 Firefox/40.0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        surfaceView = (SurfaceView) findViewById(R.id.sv_player);
        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPause = (ImageButton) findViewById(R.id.btn_pause);
        loadingPanel = (RelativeLayout) findViewById(R.id.loadingVPanel);

        mediaController = (LinearLayout) findViewById(R.id.lin_media_controller);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Bundle extras = getIntent().getExtras();
        String url = extras.getString("URL");
        if(url != null){
            this.mp4URL = url;
        }

        initPlayer(0);
//        initHLSPlayer(0);


        if(bAutoplay){
            if(exoPlayer!=null){
                exoPlayer.setPlayWhenReady(true);
                bIsPlaying=true;
//                loadingPanel.setVisibility(View.VISIBLE);
                setProgress();

                btnPlay.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
            }

        }

    }

    private void initMediaControls() {
        initSurfaceView();
        initPlayButton();
        initSeekBar();
        initTxtTime();
        initFwd();
        initPrev();
        initRew();
        initNext();
        initSetting();

    }

    private void initSetting() {
        btnSettings = (ImageButton) findViewById(R.id.setting);
        btnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(VideoPlayer.this, view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        exoPlayer.setSelectedTrack(0, (item.getItemId() - 1));
                        return false;
                    }
                });
                Menu menu = popup.getMenu();
                menu.add(Menu.NONE, 0, 0, "Video Quality");
                for (int i = 0; i < exoPlayer.getTrackCount(0); i++) {
                    MediaFormat format = exoPlayer.getTrackFormat(0, i);
                    if (MimeTypes.isVideo(format.mimeType)) {
                        if (format.adaptive) {
                            menu.add(1, (i + 1), (i + 1), "Auto");
                        } else {
                            menu.add(1, (i + 1), (i + 1), format.width + "p");
                        }
                    }
                }
                menu.setGroupCheckable(1, true, true);
                menu.findItem((exoPlayer.getSelectedTrack(0) + 1)).setChecked(true);
                popup.show();
            }
        });
    }

    private void initNext() {
        btnNext = (ImageButton) findViewById(R.id.next);
        btnNext.requestFocus();
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.seekTo(exoPlayer.getDuration());
            }
        });
    }

    private void initRew() {
        btnRew = (ImageButton) findViewById(R.id.rew);
        btnRew.requestFocus();
        btnRew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.seekTo(exoPlayer.getCurrentPosition()-10000);
            }
        });
    }

    private void initPrev() {
        btnPrev = (ImageButton) findViewById(R.id.prev);
        btnPrev.requestFocus();
        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.seekTo(0);
            }
        });


    }


    private void initFwd() {
        btnFwd = (ImageButton) findViewById(R.id.ffwd);
        btnFwd.requestFocus();
        btnFwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.seekTo(exoPlayer.getCurrentPosition()+10000);
            }
        });


    }

    private void initTxtTime() {
        txtCurrentTime = (TextView) findViewById(R.id.time_current);
        txtEndTime = (TextView) findViewById(R.id.player_end_time);
    }

    private void initSurfaceView() {
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleMediaControls();
            }
        });
    }

    private String stringForTime(int timeMs) {
        mFormatBuilder = new StringBuilder();
        mFormatter = new Formatter(mFormatBuilder, Locale.getDefault());
        int totalSeconds =  timeMs / 1000;

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours   = totalSeconds / 3600;

        mFormatBuilder.setLength(0);
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    private void setProgress() {
        seekPlayerProgress.setProgress(0);
        seekPlayerProgress.setMax(0);
        seekPlayerProgress.setMax((int) exoPlayer.getDuration()/1000);


        handler = new Handler();
        //Make sure you update Seekbar on UI thread
        handler.post(new Runnable() {

            @Override
            public void run() {
                if (exoPlayer != null && bIsPlaying ) {
                    seekPlayerProgress.setMax(0);
                    seekPlayerProgress.setMax((int) exoPlayer.getDuration()/1000);
                    int mCurrentPosition = (int) exoPlayer.getCurrentPosition() / 1000;
                    seekPlayerProgress.setProgress(mCurrentPosition);
                    txtCurrentTime.setText(stringForTime((int)exoPlayer.getCurrentPosition()));
                    txtEndTime.setText(stringForTime((int)exoPlayer.getDuration()));

                    switch (exoPlayer.getPlaybackState()) {
                        case ExoPlayer.STATE_BUFFERING:
                            loadingPanel.setVisibility(View.VISIBLE);
                            break;
                        case ExoPlayer.STATE_ENDED:
                            finish();
                            break;
                        case ExoPlayer.STATE_IDLE:
                            loadingPanel.setVisibility(View.GONE);
                            break;
                        case ExoPlayer.STATE_PREPARING:
                            loadingPanel.setVisibility(View.VISIBLE);
                            break;
                        case ExoPlayer.STATE_READY:
                            loadingPanel.setVisibility(View.GONE);
                            break;
                        default:
                            break;
                    }

                    handler.postDelayed(this, 1000);
                }

            }
        });


    }

    private void initSeekBar() {
        seekPlayerProgress = (SeekBar) findViewById(R.id.mediacontroller_progress);
        seekPlayerProgress.requestFocus();

        seekPlayerProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (!fromUser) {
                    // We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    return;
                }

                exoPlayer.seekTo(progress*1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekPlayerProgress.setMax(0);
        seekPlayerProgress.setMax((int) exoPlayer.getDuration()/1000);

    }


    private void toggleMediaControls() {

        if(bControlsActive){
            hideMediaController();
            bControlsActive=false;

        }else{
            showController();
            bControlsActive=true;
            setProgress();
        }
    }

    private void showController() {
        mediaController.setVisibility(View.VISIBLE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void hideMediaController() {
        mediaController.setVisibility(View.GONE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initPlayButton() {

        btnPlay.requestFocus();
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exoPlayer.setPlayWhenReady(true);
                bIsPlaying=true;
                setProgress();
                btnPlay.setVisibility(View.GONE);
                btnPause.setVisibility(View.VISIBLE);
            }
        });

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exoPlayer.setPlayWhenReady(false);
                bIsPlaying=false;

                btnPause.setVisibility(View.GONE);
                btnPlay.setVisibility(View.VISIBLE);
            }
        });
    }

    private void initPlayer(int position) {


        Allocator allocator = new DefaultAllocator(minBufferMs);
        DataSource dataSource = new DefaultUriDataSource(this, null, userAgent);

        ExtractorSampleSource sampleSource = new ExtractorSampleSource( Uri.parse(mp4URL), dataSource, allocator,
                BUFFER_SEGMENT_COUNT * BUFFER_SEGMENT_SIZE);

        MediaCodecVideoTrackRenderer videoRenderer = new
                MediaCodecVideoTrackRenderer(this, sampleSource, MediaCodecSelector.DEFAULT,
                MediaCodec.VIDEO_SCALING_MODE_SCALE_TO_FIT);

        MediaCodecAudioTrackRenderer audioRenderer = new MediaCodecAudioTrackRenderer(sampleSource, MediaCodecSelector.DEFAULT);

        exoPlayer = ExoPlayer.Factory.newInstance(RENDERER_COUNT);
        exoPlayer.prepare(videoRenderer, audioRenderer);
        exoPlayer.sendMessage(videoRenderer,
                MediaCodecVideoTrackRenderer.MSG_SET_SURFACE,
                surfaceView.getHolder().getSurface());
        exoPlayer.seekTo(position);
        initMediaControls();

    }

    private void killPlayer(){
        if (exoPlayer != null) {
            exoPlayer.release();
        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        killPlayer();
    }
}
