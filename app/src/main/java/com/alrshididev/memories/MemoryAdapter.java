package com.alrshididev.memories;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MemoryAdapter extends RecyclerView.Adapter<MemoryAdapter.MemoryviewHolder > {
    private Context context;
    private List<Memories> memoriesList;

    public MemoryAdapter(Context context, List<Memories> memoriesList) {
        this.context = context;
        this.memoriesList = memoriesList;
    }

    @NonNull
    @Override
    public MemoryviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout,parent,false);

        return new MemoryviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemoryviewHolder holder, int position) {
        Memories memories = memoriesList.get(position);
        Uri uri = Uri.parse(memories.getUriImage());
        holder.title.setText(memories.getTitle());
        holder.desc.setText(memories.getDescription());
        Picasso.with(context).load(uri).placeholder(R.mipmap.ic_launcher).fit().centerCrop()
                .into(holder.photo);
    }

    @Override
    public int getItemCount() {
        return memoriesList.size();
    }

    public class MemoryviewHolder extends RecyclerView.ViewHolder {
        TextView title,desc;
        ImageView photo;

        public MemoryviewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title_);
            desc = itemView.findViewById(R.id.desc);
            photo = itemView.findViewById(R.id.image);
        }
    }
}
