package com.community.jboss.leadmanagement;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.community.jboss.leadmanagement.data.models.ContributorData;

import java.util.List;

public class ContributorRecyclerViewAdapter extends RecyclerView.Adapter<ContributorRecyclerViewAdapter.ViewHolder> {
    private List<ContributorData> contributorDataList;
    private Context context;

    public ContributorRecyclerViewAdapter(Context context, List<ContributorData> contributorDataList) {
        this.context = context;
        this.contributorDataList = contributorDataList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView imageView_avatar;
        TextView textView_login;
        TextView textView_contributionCount;
        Button button_seeOnGitHub;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            imageView_avatar = itemView.findViewById(R.id.imageView_avatar);
            textView_login = itemView.findViewById(R.id.textView_login);
            textView_contributionCount = itemView.findViewById(R.id.textView_contributionCount);
            button_seeOnGitHub = itemView.findViewById(R.id.button_githubProfile);
        }
    }

    @Override
    public ContributorRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_contributor, parent, false);
        final ContributorRecyclerViewAdapter.ViewHolder holder = new ContributorRecyclerViewAdapter.ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(ContributorRecyclerViewAdapter.ViewHolder holder, final int position) {
        //for easier access
        final ContributorData contributorData = contributorDataList.get(holder.getAdapterPosition());

        holder.textView_login.setText(contributorData.getLogin());
        Glide.with(context).load(contributorData.getAvatarUrl()).into(holder.imageView_avatar);

        holder.textView_contributionCount.setText(Integer.toString(contributorData.getContributions()));

        holder.button_seeOnGitHub.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(contributorData.getGithubProfileUrl()));
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return contributorDataList.size();
    }
}
