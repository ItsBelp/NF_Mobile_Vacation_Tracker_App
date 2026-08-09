package com.example.myapplication.UI;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.entities.Excursion;

import java.util.List;
import java.util.zip.Inflater;

public class ExcursionAdapter extends RecyclerView.Adapter<ExcursionAdapter.ExcursionViewHolder> {

    private List<Excursion> mExcursions;
    private final Context context;
    private final LayoutInflater mInflater;

    public class ExcursionViewHolder extends RecyclerView.ViewHolder {

        private final TextView excursionItemView;
        private final TextView excursionItemView2;

        private ExcursionViewHolder(View itemView){
            super(itemView);
            excursionItemView = itemView.findViewById(R.id.excursionName);
            excursionItemView2 = itemView.findViewById(R.id.excursionDate);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAbsoluteAdapterPosition();
                    final Excursion current = mExcursions.get(position);
                    Intent intent = new Intent(context, ExcursionDetails.class);
                    intent.putExtra("id", current.getExcID());
                    intent.putExtra("name", current.getExcTitle());
                    intent.putExtra("date", current.getExcDate());
                    context.startActivity(intent);
                }
            });
        }
    }


    public ExcursionAdapter(Context context){
        mInflater = LayoutInflater.from(context);
        this.context = context;
    }

    @NonNull
    @Override
    public ExcursionAdapter.ExcursionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.excursion_list_item, parent, false);
        return new ExcursionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExcursionAdapter.ExcursionViewHolder holder, int position) {
        if (mExcursions != null){
            Excursion current = mExcursions.get(position);
            String name = current.getExcTitle();
            String excDate = current.getExcDate();
            holder.excursionItemView.setText(name);
            holder.excursionItemView2.setText(excDate);
        }
        else {
            holder.excursionItemView.setText("No Excursion Name");
            holder.excursionItemView2.setText("No Excursion Date");
        }
    }
    public void setExcursions(List<Excursion> excursions) {
        mExcursions = excursions;
        notifyDataSetChanged();
    }

    public int getItemCount() {
        if (mExcursions != null) return mExcursions.size();
        else return 0;
    }

}
