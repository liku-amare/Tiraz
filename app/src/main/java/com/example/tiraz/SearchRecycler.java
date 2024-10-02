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
import android.widget.ImageView;
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

public class SearchRecycler extends RecyclerView.Adapter<SearchRecycler.ViewHolder>{

    Context context;
    int item_count = 1;
    String query;

    ArrayList<AudioModel> mezmurList = new ArrayList<>();
    ArrayList<AudioModel> resultList = new ArrayList<>();

    public SearchRecycler (Context context, String query){
        this.context = context;
        this.query = query;
        populateMezmurList(query);
    }
    @NonNull
    @Override
    public SearchRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mezmur_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchRecycler.ViewHolder holder, int position) {
        AudioModel mezmurData = resultList.get(position);
        holder.mezmurOrderNo.setText(position + 1 + ". ");
        holder.mezmurItemTitle.setText(mezmurData.getTitle());
        String tags = mezmurData.getTags();
        holder.mezmurItemTags.setText(tags);
        if (mezmurData.getAudio_id() == -1)
            holder.mezmurItemAudio.setVisibility(View.INVISIBLE);
        else
            holder.mezmurItemAudio.setVisibility(View.VISIBLE);
        holder.mezmurItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = holder.getAdapterPosition();
                Intent intent = new Intent(context, MezmurPlayer.class);
                intent.putExtra("LIST", resultList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { return item_count; }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mezmurItem;
        TextView mezmurItemTitle, mezmurItemTags, mezmurOrderNo;
        ImageView mezmurItemAudio;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mezmurItem = itemView.findViewById(R.id.mezmur_item_ll);
            mezmurItemTitle = itemView.findViewById(R.id.mezmur_item_title);
            mezmurItemTags = itemView.findViewById(R.id.mezmur_item_tags);
            mezmurItemAudio = itemView.findViewById(R.id.mezmur_audio_icon);
            mezmurOrderNo = itemView.findViewById(R.id.mezmur_order_no);
        }
    }

    public void populateMezmurList(String query){
        String tirazString = getJson("tiraz_lyrics.json");
        JSONArray mezmurJsonArray;
        try {
            JSONArray tirazJson;
            tirazJson = new JSONArray(tirazString);
            JSONObject tirazJsonObject = tirazJson.getJSONObject(0);
            mezmurJsonArray = tirazJsonObject.getJSONArray("mezmurs");
            for (int i = 1; i < 4; i++){
                mezmurJsonArray = concatenate(mezmurJsonArray, tirazJson.getJSONObject(i).getJSONArray("mezmurs"));
            }
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
                        2,
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

        // Perform search using query
        String searchPattern = query.trim();
        for (AudioModel audio : mezmurList){
            if (audio.getTitle().contains(searchPattern) || audio.getLyrics().contains(searchPattern)){
                resultList.add(audio);
            }
        }
        item_count = resultList.size();
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

    public static JSONArray concatenate(JSONArray array1, JSONArray array2) throws JSONException {
        JSONArray result = new JSONArray();

        for (int i = 0; i < array1.length(); i++) {
            result.put(array1.get(i));
        }

        for (int i = 0; i < array2.length(); i++) {
            result.put(array2.get(i));
        }

        return result;
    }
}
