package com.example.mymusic;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.songholder> {
    ArrayList<com.example.mymusic.songs>songs;
    Context context;
    onItemClickListner onitemclicklistner;
    SongAdapter(Context context,ArrayList<com.example.mymusic.songs>songs){
        this.context=context;
        this.songs=songs;
    }
    public interface onItemClickListner{
        void onItemClick(Button b, View v, com.example.mymusic.songs obj, int position);
    }
    public  void setOnitemclicklistner(onItemClickListner onitemclicklistner){
        this.onitemclicklistner=onitemclicklistner;

    }
    @Override
    public songholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View myview=  (View) LayoutInflater.from(context).inflate(R.layout.row_song,viewGroup,false);
        return new songholder(myview);
    }

    @Override
    public void onBindViewHolder(@NonNull final songholder songholder, final int i) {
        final com.example.mymusic.songs c=songs.get(i);
        songholder.artistname.setText(c.Artist);
        songholder.songname.setText(c.Song);
        songholder.playbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onitemclicklistner!= null){
                    onitemclicklistner.onItemClick(songholder.playbt,v,c,i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public class songholder extends RecyclerView.ViewHolder {
        TextView songname,artistname;
        Button playbt;
        public songholder(@NonNull View itemView) {
            super(itemView);
            songname=(TextView)itemView.findViewById(R.id.textView);
            artistname=(TextView)itemView.findViewById(R.id.textView2);
            playbt= (Button) itemView.findViewById(R.id.button);
        }
    }
}
