package com.example.todoapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.todoapp.Interfaces.RecyclerViewClickListener;
import com.example.todoapp.R;
import com.example.todoapp.model.DisplayTasks.TodosItem;

import java.util.List;

public class FinishedTodoListAdapter extends RecyclerView.Adapter<FinishedTodoListAdapter.ViewHolder> {
    List<TodosItem> todosItemList;
    Context context;
    final private RecyclerViewClickListener clickListener;

    public FinishedTodoListAdapter(List<TodosItem> todosItemList, Context context, RecyclerViewClickListener clickListener) {
        this.todosItemList = todosItemList;
        this.context = context;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public FinishedTodoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.finished_todo_list_item, parent, false);

        final FinishedTodoListAdapter.ViewHolder myViewHolder = new FinishedTodoListAdapter.ViewHolder(view);
        myViewHolder.arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myViewHolder.accordian_body.getVisibility() == View.VISIBLE) {
                    myViewHolder.accordian_body.setVisibility(View.GONE);
                } else {
                    myViewHolder.accordian_body.setVisibility(View.VISIBLE);
                }
            }
        });
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FinishedTodoListAdapter.ViewHolder holder, int position) {
        TodosItem todosItem= todosItemList.get(position);

        holder.titleTv.setText(todosItem.getTitle());
        if(!todosItem.getDescription().equals("")) {
            holder.descriptionTv.setText(todosItem.getDescription());
        }

    }

    @Override
    public int getItemCount() {
        return todosItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CardView accordian_title;
        TextView titleTv, descriptionTv;
        RelativeLayout accordian_body;
        ImageView arrow;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTv = (TextView) itemView.findViewById(R.id.finished_task_title);
            descriptionTv = (TextView) itemView.findViewById(R.id.finished_task_description);
            accordian_title = (CardView) itemView.findViewById(R.id.finished_accordian_title);
            accordian_body = (RelativeLayout) itemView.findViewById(R.id.finished_accordian_body);
            arrow = (ImageView) itemView.findViewById(R.id.finished_arrow);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
