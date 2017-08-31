package com.acceltest.newsapi;

import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acceltest.newsapi.Adapter.NewslistAdapter;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    RecyclerView newslist;
    RecyclerView.Adapter newslistAdapter;
    ArrayList<Modal_articles> modal_articles = new ArrayList<Modal_articles>();
    private ProgressDialog pDialog;
    TextView no_news_found;
    ImageView searchbutton;
    EditText search_txt;
    String serch_news;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchbutton = (ImageView) findViewById(R.id.searchbutton);
        search_txt = (EditText) findViewById(R.id.search_txt);
        no_news_found = (TextView) findViewById(R.id.no_news_found);
        newslist = (RecyclerView) findViewById(R.id.newslist);
        RecyclerView.LayoutManager re_lay_manager = new LinearLayoutManager(this);
        newslist.setLayoutManager(re_lay_manager);

        newslist.setVisibility(View.GONE);
        no_news_found.setVisibility(View.VISIBLE);

        searchbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(search_txt.getText().toString().length() > 0) {
                    serch_news = search_txt.getText().toString().trim();
                    loaddata();
                }else {
                    newslist.setVisibility(View.GONE);
                    no_news_found.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    public void progress_show() {
        pDialog = new ProgressDialog(MainActivity.this);
        pDialog.setMessage("Loading");
        pDialog.show();
    }

    public void progress_hide() {
        if (pDialog.isShowing()) {
            pDialog.hide();
        }
    }

    public void loaddata() {
        if (Apputil.checkInternetConnection(MainActivity.this)) {
            String base_url = "https://newsapi.org/v1/articles?source="+serch_news+"&apiKey=cfb33b18fba149509aec7137ec9c57d0";
            HashMap<String, String> params = new HashMap<String, String>();
            Post(base_url, params);
        } else {
            Apputil.No_network_connection(MainActivity.this);
        }

    }

    public void Post(String url, final Map<String, String> map) {
        progress_show();
        RequestQueue queue = VolleySingleton.getInstance(MainActivity.this).getRequestQueue();
        DiskBasedCache cache = new DiskBasedCache(getCacheDir(), 16 * 1024 * 1024);
        queue = new RequestQueue(cache, new BasicNetwork(new HurlStack()));
        queue.start();
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("response_is", "response_is" + response);
                if (response != null && response.length() > 0) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("articles");
                        if (jsonArray.length() > 0) {
                            modal_articles.clear();
                            for (int j = 0; j < jsonArray.length(); j++) {
                                JSONObject liveobj = jsonArray.getJSONObject(j);
                                Gson gson = new Gson();
                                Modal_articles obj = null;
                                obj = new Modal_articles();
                                obj = gson.fromJson(liveobj.toString(), Modal_articles.class);
                                modal_articles.add(obj);
                            }
                            newslistAdapter = new NewslistAdapter(MainActivity.this, modal_articles);
                            newslist.setAdapter(newslistAdapter);
                            newslist.setVisibility(View.VISIBLE);
                            no_news_found.setVisibility(View.GONE);
                            progress_hide();
                            search_txt.clearFocus();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progress_hide();
                try {
                    if (error != null) {
                        String error1 = error.toString();
                        newslist.setVisibility(View.GONE);
                        no_news_found.setVisibility(View.VISIBLE);
                        search_txt.clearFocus();
                        Toast.makeText(MainActivity.this,"No news has been found",Toast.LENGTH_SHORT).show();
                    } else {

                    }
                } catch (Exception e) {

                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                return map;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/x-www-form-urlencoded");
                return params;
            }
        };
        request.setShouldCache(false);
        request.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 1, 1.0f));
        queue.add(request);
    }

    @Override
    public void onBackPressed() {
        newslist.setVisibility(View.GONE);
        search_txt.setText("");
        search_txt.clearFocus();
        no_news_found.setVisibility(View.VISIBLE);

    }
}
