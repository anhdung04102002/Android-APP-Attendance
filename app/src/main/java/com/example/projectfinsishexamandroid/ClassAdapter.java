package com.example.projectfinsishexamandroid;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.StudentViewHolder> {
    ArrayList<ClassItem> classItems;
    Context context;
    public OnItemClickListener onItemClickListener;
    public interface OnItemClickListener {
        void Onclick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public ClassAdapter(ArrayList<ClassItem> classItems, Context context) {
        this.classItems = classItems;
        this.context = context;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder{
        TextView className ;
        TextView subjectName;
        public StudentViewHolder(@NonNull View itemView,OnItemClickListener onItemClickListener) {
            super(itemView);
            className = itemView.findViewById(R.id.class_tv);
            subjectName = itemView.findViewById(R.id.subject_tv);
            itemView.setOnClickListener(v -> onItemClickListener.Onclick(getAdapterPosition()));
        }
    }
    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_item,parent,false);
        return new StudentViewHolder(itemView,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) { // hiển thị dữ liệu tại vị trí cụ thể
            holder.className.setText(classItems.get(position).getClassName());
            holder.subjectName.setText(classItems.get(position).getSubjectName());
    }

    @Override
    public int getItemCount() {
        return classItems.size();
    }



}
