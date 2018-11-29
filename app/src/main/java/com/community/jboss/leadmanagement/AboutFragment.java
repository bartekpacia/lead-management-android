package com.community.jboss.leadmanagement;


import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.community.jboss.leadmanagement.data.models.ContributorData;
import com.community.jboss.leadmanagement.main.MainFragment;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class AboutFragment extends MainFragment {

    private Unbinder unbinder;

    private ContributorRecyclerViewAdapter adapter;
    private List<ContributorData> contributorDataList;

    @BindView(R.id.license) Button license;
    @BindView(R.id.aboutJboss) Button aboutJboss;
    @BindView(R.id.facebook) ImageButton facebook;
    @BindView(R.id.twitter) ImageButton twitter;
    @BindView(R.id.github) ImageButton github;
    @BindView(R.id.playstore) ImageButton playstore;
    @BindView(R.id.recyclerView) RecyclerView recyclerView;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        unbinder = ButterKnife.bind(this, view);

        contributorDataList = new ArrayList<>();
        adapter = new ContributorRecyclerViewAdapter(getContext(), contributorDataList);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);

        downloadData();

        setListeners();

        return view;
    }

    private void downloadData() {
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        StringRequest request = new StringRequest(Request.Method.GET, getString(R.string.contributors_url), this::processResponse, error -> {
            error.printStackTrace();
            Toast.makeText(getContext(), "An error occurred", Toast.LENGTH_SHORT).show();
        });
        requestQueue.add(request);
    }

    private void processResponse(String response) {
        try {
            JSONArray jsonArray = new JSONArray(response);

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject contributorJSON = jsonArray.getJSONObject(i);
                String login = contributorJSON.getString("login");
                String avatarUrl = contributorJSON.getString("avatar_url");
                String githubProfileUrl = contributorJSON.getString("html_url");
                int contributions = contributorJSON.getInt("contributions");

                ContributorData contributorData = new ContributorData(login, avatarUrl, githubProfileUrl, contributions);
                contributorDataList.add(contributorData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        adapter.notifyDataSetChanged();
    }

    private void showLicense() {
        Intent redirect = new Intent(Intent.ACTION_VIEW, Uri.parse(getContext().getString(R.string.license_url)));
        startActivity(redirect);
    }

    @Override
    public int getTitle() {
        return R.string.about;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setListeners() {
        license.setOnClickListener(v -> showLicense());
        aboutJboss.setOnClickListener(v -> showJBossInfo());
        facebook.setOnClickListener(v -> Toast.makeText(getContext(), "Opening facebook...", Toast.LENGTH_SHORT).show());
        twitter.setOnClickListener(v -> Toast.makeText(getContext(), "Opening twitter...", Toast.LENGTH_SHORT).show());
        github.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(getContext().getString(R.string.github_url)));
            startActivity(i);
        });
        playstore.setOnClickListener(v -> Toast.makeText(getContext(), "Opening github", Toast.LENGTH_SHORT).show());
    }

    private void showJBossInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.about_jboss).setMessage(R.string.jboss_description);
        builder.show();
    }
}
