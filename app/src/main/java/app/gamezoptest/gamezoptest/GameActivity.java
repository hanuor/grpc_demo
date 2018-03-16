package app.gamezoptest.gamezoptest;
/*
 * Created by Han
 *Vamos!
 *
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.etsy.android.grid.StaggeredGridView;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import app.gamezoptest.gamezoptest.Models.AdapterModel;
import app.gamezoptest.gamezoptest.Utils.DownloadZipTask;
import app.gamezoptest.gamezoptest.Utils.UnzipTask;
import app.gamezoptest.gamezoptest.adapters.ImageAdapter;
import app.gamezoptest.gamezoptest.interfaces.GetResponse;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class GameActivity extends AppCompatActivity implements AbsListView.OnScrollListener, AbsListView.OnItemClickListener, GetResponse {
    ArrayList<AdapterModel> getImagesURL = new ArrayList<AdapterModel>();
    private ImageAdapter mAdapter;
    private StaggeredGridView mGridView;
    GrpcTask grpcTask = null;
    public static ProgressDialog progress = null;
    HashMap<Integer, String> hashMap = new HashMap<Integer, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        mGridView = (StaggeredGridView) findViewById(R.id.grid_view);

        grpcTask = new GrpcTask(this);
        grpcTask.execute();
        grpcTask.response = this;

        mAdapter = new ImageAdapter(this, android.R.layout.simple_list_item_1, getImagesURL);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);

    }

    public void sendMessage(View view) {

    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int i) {

    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
        progress = new ProgressDialog(this);
        progress.setMessage("Loading game. Sit tight ;)");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();
        String zipUrl = hashMap.get(i);
        DownloadZipTask download = new DownloadZipTask("/gamezop/" + i + ".zip", this, new DownloadZipTask.PostDownload(){
            @Override
            public void downloadDone(File file) {

                // check unzip file now
                UnzipTask unzip = new UnzipTask(GameActivity.this, file);
                String ss = unzip.unzip(i);
                String path_to_file = "gamezoptest/unzips/" + ss;
                Intent startAct = new Intent(GameActivity.this, WebActivity.class);
                startAct.putExtra("path", path_to_file);
                startActivity(startAct);
            }
        });
        download.execute(zipUrl);
    }

    @Override
    public void afterOperation(ArrayList<AdapterModel> data) {
        getImagesURL.addAll(data);
        mAdapter = new ImageAdapter(this, android.R.layout.simple_list_item_1, getImagesURL);
        mGridView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mGridView.invalidate();
        mGridView.setOnScrollListener(this);
        mGridView.setOnItemClickListener(this);
    }

    @Override
    public void getHashMapafterOperation(HashMap<Integer, String> hashMap) {
        this.hashMap.putAll(hashMap);
    }

    private class GrpcTask extends AsyncTask<String, Void, GamesResponse> {
        private final WeakReference<Activity> activityReference;
        private ManagedChannel channel;
        private GetResponse response = null;

        private GrpcTask(Activity activity) {
            this.activityReference = new WeakReference<Activity>(activity);
        }

        @Override
        protected GamesResponse doInBackground(String... params) {
            String host = "ben.gamezop.io";
            String portStr = "50051";
            int port = TextUtils.isEmpty(portStr) ? 0 : Integer.valueOf(portStr);
            try {
                channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext(true).build();
                InfoServiceGrpc.InfoServiceBlockingStub stub = InfoServiceGrpc.newBlockingStub(channel);
                GamesRequest request = GamesRequest.newBuilder().build();
                GamesResponse reply = stub.getGames(request);
                return reply;
            } catch (Exception e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
                pw.flush();
                return null;
            }
        }

        @Override
        protected void onPostExecute(GamesResponse result) {
            try {
                channel.shutdown().awaitTermination(1, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            ArrayList<AdapterModel> getImagesURLfromResult = new ArrayList<AdapterModel>();
            HashMap<Integer, String> makeMap = new HashMap<>();
            for (int i = 0; i < result.getGamesCount(); i++) {
                getImagesURLfromResult.add(new AdapterModel(result.getGames(i).getName(),result.getGames(i).getCover()));
                makeMap.put(i, result.getGames(i).getZipUrl());

            }
            response.getHashMapafterOperation(makeMap);
            response.afterOperation(getImagesURLfromResult);
            StaggeredGridView mGridView = (StaggeredGridView) findViewById(R.id.grid_view);
            ImageAdapter myadapter = (ImageAdapter) mGridView.getAdapter();
            myadapter.notifyDataSetChanged();
            Activity activity = activityReference.get();
            if (activity == null) {
                return;
            }

        }
    }

}
