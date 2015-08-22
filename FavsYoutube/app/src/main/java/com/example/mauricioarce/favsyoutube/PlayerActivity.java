package com.example.mauricioarce.favsyoutube;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayer.OnInitializedListener;
import com.google.android.youtube.player.YouTubePlayerFragment;
import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mauricio Arce on 15/08/2015.
 */
public class PlayerActivity extends AppCompatActivity {

    private final List<Video> videos = new ArrayList<>();
    private ListView videosListView;
    private Video current;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.player_list);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);
        fillVideosList();
        videosListView = (ListView) findViewById(R.id.videos_list);
        videosListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoFragment videoFragment = (VideoFragment) getFragmentManager().findFragmentById(R.id.player_fragment);
                TextView videoName = (TextView) view.findViewById(R.id.text_item);
                String name = videoName.getText().toString();
                for (Video video : videos) {
                    if (video.getName().equals(name)) {
                        current = video;
                    }
                }
                videoFragment.setVideoId(current.getCode());
            }
        });
    }

    private void fillVideosList() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Video");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                fillVideosList(list);
            }
        });
    }

    private void fillVideosList(List<ParseObject> objects) {
        for (ParseObject object : objects) {
            Video video = new Video();
            video.setCode(object.getString("videoId"));
            video.setName(object.getString("name"));
            videos.add(video);
        }
        fillVideosListView();
    }

    private void fillVideosListView() {
        String[] videosArray = new String[videos.size()];
        for (int i = 0; i < videos.size(); i++) {
            videosArray[i] = videos.get(i).getName();
        }
        videosListView.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.list_item, videosArray));
    }

    public static final class VideoFragment extends YouTubePlayerFragment implements OnInitializedListener {

        private YouTubePlayer player;
        private String videoId;
        private String apiKey;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            apiKey = getResources().getString(R.string.api_key);

            initialize(apiKey, this);
        }

        @Override
        public void onDestroy() {
            if (player != null) {
                player.release();
            }
            super.onDestroy();
        }

        public void setVideoId(String videoId) {
            if (videoId != null && !videoId.equals(this.videoId)) {
                this.videoId = videoId;
                if (player != null) {
                    player.loadVideo(videoId);
                }
            }
        }

        @Override
        public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean restored) {
            this.player = player;
            if (!restored && videoId != null) {
                player.cueVideo(videoId);
            }
        }

        @Override
        public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult result) {
            this.player = null;
        }

    }
}
