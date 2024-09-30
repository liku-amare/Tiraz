package com.example.tiraz;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements SearchAdapterInterface {

    private List<AudioModel> mezmurList;
    private List<AudioModel> mezmurListFull;
    private Context context;

    public SearchAdapter(Context context, List<AudioModel> mezmurList) {
        this.context = context;
        this.mezmurList = mezmurList;
        this.mezmurListFull = new ArrayList<>(mezmurList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AudioModel audioModel = mezmurList.get(position);
        holder.textView.setText(audioModel.getTitle());
        Typeface typeface = ResourcesCompat.getFont(context, R.font.niyala);
        holder.textView.setTypeface(typeface);

        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<AudioModel> currentAudioList = new ArrayList<>();
                currentAudioList.add(audioModel);
                MyMediaPlayer.getInstance().reset();
                MyMediaPlayer.currentIndex = 0;
                Intent intent = new Intent(context, MezmurPlayer.class);
                intent.putExtra("LIST", currentAudioList);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mezmurList.size();
    }

    @Override
    public Filter getFilter() {
        return audioFilter;
    }

    private Filter audioFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AudioModel> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(mezmurListFull); // No filter applied
            } else {
                String filterPattern = constraint.toString().trim();

                for (AudioModel audio : mezmurListFull) {
                    if (audio.getTitle().contains(filterPattern)) {
                        filteredList.add(audio);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        @SuppressWarnings("unchecked")
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mezmurList.clear();
            mezmurList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(android.R.id.text1);
        }
    }
}