package com.example.question4;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener {
    ArrayList<Repo> persons = new ArrayList<Repo>();
    private final String TAG = "MyAdapter";
    private OnItemClickListener mOnItemClickListener = null;
    private int highlightIndex = -1;

    public MyAdapter(Context context) {
    }

    public void setDatelist(ArrayList<Repo> list) {
        persons = list;
    }

    public void addList(ArrayList<Repo> list) {
        persons.addAll(list);
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listlayout, viewGroup, false);
        view.setOnClickListener(this);
        return new MyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyAdapter.ViewHolder holder, final int position) {
        String id = persons.get(position).getID();
        String name = persons.get(position).getName();
        int attack = persons.get(position).getAttack();
        int defense = persons.get(position).getDefense();
        holder.id_holder.setText(id);
        holder.name_holder.setText(name);
        holder.attack_holder.setText("" + attack);
        holder.defense_holder.setText("" + defense);
        if (highlightIndex == position) {
            holder.id_holder.setTextColor(Color.RED);
            holder.name_holder.setTextColor(Color.RED);
            holder.attack_holder.setTextColor(Color.RED);
            holder.defense_holder.setTextColor(Color.RED);
        } else {
            holder.id_holder.setTextColor(Color.BLACK);
            holder.name_holder.setTextColor(Color.BLACK);
            holder.name_holder.setTextColor(Color.BLACK);
            holder.attack_holder.setTextColor(Color.BLACK);
            holder.defense_holder.setTextColor(Color.BLACK);
        }

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Log.i(TAG, "view position : " + position);
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() { //設反
            @Override
            public void onClick(View v) {
//                highlightIndex = position;
                hilightouch(position);
                notifyDataSetChanged();
            }
        });
    }



    @Override
    public int getItemCount() {
//        Log.i("123", "persons.size() " + persons.size());
        return persons.size();

    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name_holder, id_holder, attack_holder, defense_holder;
        private View view;

        ViewHolder(final View itemView) {
            super(itemView);
            view = itemView;
            this.id_holder = (TextView) itemView.findViewById(R.id.id);
            this.name_holder = (TextView) itemView.findViewById(R.id.name);
            this.attack_holder = (TextView) itemView.findViewById(R.id.attack);
            this.defense_holder = (TextView) itemView.findViewById(R.id.defense);
        }

        public void setOnItemClick(View.OnClickListener l) {
            this.view.setOnClickListener(l);
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void hilightouch(int touch) {
        if (highlightIndex == touch) {
            highlightIndex = -1;
        } else {
            highlightIndex = touch;
        }
    }
}