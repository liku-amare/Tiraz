package com.example.tiraz;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
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

public class MezmurRecycler extends RecyclerView.Adapter<MezmurRecycler.ViewHolder>{

    Context context;
    int item_count = 5;
    int tiraz_id;

    public MezmurRecycler (Context context, int tiraz_id){
        this.context = context;
        this.tiraz_id = tiraz_id;
    }
    @NonNull
    @Override
    public MezmurRecycler.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mezmur_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MezmurRecycler.ViewHolder holder, int position) {
        Log.d("Item Count in OnBind", item_count + "");
        JSONObject mezmur = null;
        String title = "";
        int audio_id = 0;
        StringBuilder mezmur_tags = new StringBuilder();

        try {
            mezmur = holder.mezmurJsonArray.getJSONObject(position);
            title = mezmur.getString("mezmur_title");
            audio_id = mezmur.getInt("audio_id");
            JSONArray tags = mezmur.getJSONArray("mezmur_tags");
            int i = 0;
            mezmur_tags.append("ገላጭ ቃላት፡ ");
            while (i < tags.length()){
                mezmur_tags.append(tags.getString(i)).append("፣ ");
                i++;
            }
            mezmur_tags.delete(mezmur_tags.length()-2, mezmur_tags.length()-1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        title = position + 1 + ". " + title;
        holder.mezmurItemTitle.setText(title);
        holder.mezmurItemTags.setText(mezmur_tags.toString());
        if (audio_id == -1)
            holder.mezmurItemAudio.setText("ድምፅ ቅጅ: የለም");
        else
            holder.mezmurItemAudio.setText("ድምፅ ቅጅ: አለ");
        JSONObject finalMezmur = mezmur;
        holder.mezmurItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MezmurPlayer.class);
                intent.putExtra("mezmur", finalMezmur.toString());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() { Log.d("Item Count in View Holder", item_count + ""); return item_count; }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mezmurItem;
        TextView mezmurItemTitle, mezmurItemTags, mezmurItemAudio;
        JSONArray mezmurJsonArray;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mezmurItem = itemView.findViewById(R.id.mezmur_item_ll);
            mezmurItemTitle = itemView.findViewById(R.id.mezmur_item_title);
            mezmurItemTags = itemView.findViewById(R.id.mezmur_item_tags);
            mezmurItemAudio = itemView.findViewById(R.id.mezmur_item_audio);

            String tirazString = getJson("tiraz_lyrics.json");
            try {
                JSONArray tirazJson;
                tirazJson = new JSONArray(tirazString);
                JSONObject tirazJsonObject = tirazJson.getJSONObject(tiraz_id-1);
                item_count = tirazJsonObject.getInt("hymns_count");
                Log.d("Item Count in View Holder", item_count + "");
                mezmurJsonArray = tirazJsonObject.getJSONArray("mezmurs");
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
