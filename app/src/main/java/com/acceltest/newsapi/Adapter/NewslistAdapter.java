package com.acceltest.newsapi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acceltest.newsapi.Apputil;
import com.acceltest.newsapi.Modal_articles;
import com.acceltest.newsapi.R;
import com.acceltest.newsapi.Webview;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Praba-Crayond on 8/30/2017.
 */


public class NewslistAdapter extends RecyclerView.Adapter<NewslistAdapter.ViewHolder> {

    ArrayList<Modal_articles> modal_articles = new ArrayList<Modal_articles>();

    Context context;
    int previous_position = 0;


    public NewslistAdapter(Context con, ArrayList<Modal_articles> modal_artic) {
        this.context = con;
        this.modal_articles = modal_artic;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView date, author, title, description,source_link;
        public ImageView banner_img;

        public ViewHolder(View itemvi) {
            super(itemvi);
            date = (TextView) itemvi.findViewById(R.id.date);
            author = (TextView) itemvi.findViewById(R.id.author);
            title = (TextView) itemvi.findViewById(R.id.title);
            description = (TextView) itemvi.findViewById(R.id.description);
            source_link = (TextView) itemvi.findViewById(R.id.source_link);
            banner_img = (ImageView) itemvi.findViewById(R.id.banner_img);
        }
    }


    @Override
    public int getItemCount() {
        return modal_articles.size();
    }


    @Override
    public void onBindViewHolder(NewslistAdapter.ViewHolder holder, final int position) {
        if (position > previous_position) {
            Apputil.animation(holder, true);
        } else {
            Apputil.animation(holder, false);
        }
        previous_position = position;

        if(!Apputil.isEmpty( modal_articles.get(position).getPublishedAt())) {
            String dat=Apputil.ConvertDateandTimeFromServer(modal_articles.get(position).getPublishedAt());
            holder.date.setVisibility(View.VISIBLE);
            holder.date.setText(dat);
        }else {
            holder.date.setVisibility(View.GONE);
        }

        if(!Apputil.isEmpty(modal_articles.get(position).getAuthor())) {
            holder.author.setVisibility(View.VISIBLE);
            holder.author.setText(modal_articles.get(position).getAuthor());
        }else {
            holder.author.setVisibility(View.GONE);
        }

        if(!Apputil.isEmpty(modal_articles.get(position).getTitle())) {
            holder.title.setVisibility(View.VISIBLE);
            holder.title.setText(modal_articles.get(position).getTitle());
        }else {
            holder.title.setVisibility(View.GONE);
        }

        if(!Apputil.isEmpty(modal_articles.get(position).getDescription())) {
            holder.description.setVisibility(View.VISIBLE);
            holder.description.setText(modal_articles.get(position).getDescription());
        }else {
            holder.description.setVisibility(View.GONE);
        }
        if(!Apputil.isEmpty(modal_articles.get(position).getUrl())) {
            holder.source_link.setVisibility(View.VISIBLE);
            holder.source_link.setText(modal_articles.get(position).getUrl());
            holder.source_link.setPaintFlags(holder.source_link.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);
        }else {
            holder.source_link.setVisibility(View.GONE);
        }

        if (!Apputil.isEmpty(modal_articles.get(position).getUrlToImage())) {
            Picasso.with(context).load(modal_articles.get(position).getUrlToImage().trim().replace(" ", "%20")).placeholder(R.drawable.img1).centerCrop().fit().error(R.drawable.img1).into(holder.banner_img);
        }


        holder.source_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(context, Webview.class);
                i.putExtra("url",modal_articles.get(position).getUrl());
                context.startActivity(i);
            }
        });

    }

    @Override
    public NewslistAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View create_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newsadapterlayout, parent, false);
        ViewHolder VH = new ViewHolder(create_view);
        return VH;
    }

}
