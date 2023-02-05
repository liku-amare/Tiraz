package com.example.tiraz;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MezmurRecycler extends RecyclerView.Adapter<MezmurRecycler.ViewHolder>{

    Context context;
    int item_count = 1;
    int tiraz_id;

    ArrayList<AudioModel> mezmurList = new ArrayList<>();

    public MezmurRecycler (Context context, int tiraz_id){
        this.context = context;
        this.tiraz_id = tiraz_id;
        populateMezmurList();
    }
    @NonNull
    @Override
    public MezmurRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mezmur_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MezmurRecycler.ViewHolder holder, int position) {
        AudioModel mezmurData = mezmurList.get(position);
        String title = position + 1 + ". " + mezmurData.getTitle();
        String tags = "ገላጭ ቃላት፡ " + mezmurData.getTags();
        holder.mezmurItemTitle.setText(title);
        holder.mezmurItemTags.setText(tags);
        if (mezmurData.getAudio_id() == -1)
            holder.mezmurItemAudio.setText("ድምፅ ቅጅ: የለም");
        else
            holder.mezmurItemAudio.setText("ድምፅ ቅጅ: አለ");
        holder.mezmurItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = holder.getAdapterPosition();
                Intent intent = new Intent(context, MezmurPlayer.class);
                intent.putExtra("LIST", mezmurList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return item_count; }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mezmurItem;
        TextView mezmurItemTitle, mezmurItemTags, mezmurItemAudio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mezmurItem = itemView.findViewById(R.id.mezmur_item_ll);
            mezmurItemTitle = itemView.findViewById(R.id.mezmur_item_title);
            mezmurItemTags = itemView.findViewById(R.id.mezmur_item_tags);
            mezmurItemAudio = itemView.findViewById(R.id.mezmur_item_audio);
        }
    }

    public void populateMezmurList(){
        String tirazString = getJson("tiraz_lyrics.json");
        JSONArray mezmurJsonArray;
        try {
            JSONArray tirazJson;
            tirazJson = new JSONArray(tirazString);
            JSONObject tirazJsonObject = tirazJson.getJSONObject(tiraz_id-1);
            Log.d("Item Count in View Holder", item_count + "");
            mezmurJsonArray = tirazJsonObject.getJSONArray("mezmurs");
            item_count = mezmurJsonArray.length();
            Log.d("Item Count", "" + item_count);
            for (int i = 0; i < item_count; i ++) {
                JSONObject mezmur = mezmurJsonArray.getJSONObject(i);
                StringBuilder mezmur_tags = new StringBuilder();
                JSONArray tags = mezmur.getJSONArray("mezmur_tags");
                int j = 0;
                while (j < tags.length()){
                    mezmur_tags.append(tags.getString(j)).append("፣ ");
                    j++;
                }
                mezmur_tags.delete(mezmur_tags.length()-2, mezmur_tags.length()-1);
                AudioModel mezmurData = new AudioModel(
                        mezmur.getInt("mezmur_id"),
                        mezmur.getString("mezmur_title"),
                        mezmur.getString("language"),
                        mezmur_tags.toString(),
                        mezmur.getInt("audio_id"),
                        mezmur.getString("lyrics")
                );
                mezmurList.add(mezmurData);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getJson(String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            AssetManager assetManager = context.getAssets();
            BufferedReader bf = new BufferedReader(
                    new InputStreamReader(assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
