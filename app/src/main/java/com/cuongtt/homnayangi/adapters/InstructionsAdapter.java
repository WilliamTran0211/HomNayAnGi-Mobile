package com.cuongtt.homnayangi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cuongtt.homnayangi.R;

import java.util.ArrayList;
import java.util.List;

public class InstructionsAdapter extends RecyclerView.Adapter<InstructionsAdapter.IntructionsItemViewHolder> {

    private ArrayList<String> instructionList;
    private Context context;

    public InstructionsAdapter(ArrayList<String> instructionList, Context context) {
        this.instructionList = instructionList;
        this.context = context;
    }

    @NonNull
    @Override
    public IntructionsItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_instructions, parent, false);
        return new IntructionsItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull IntructionsItemViewHolder holder, int position) {
        String instruction = instructionList.get(holder.getAdapterPosition());

        int step = holder.getAdapterPosition() + 1;
        holder.tv_step.setText("Bước "+step);
        holder.tv_instructions.setText(instruction);

    }

    @Override
    public int getItemCount() {
        return instructionList.size();
    }

    public static class IntructionsItemViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_step;
        public TextView tv_instructions;


        public IntructionsItemViewHolder(View itemView) {
            super(itemView);

            tv_step = itemView.findViewById(R.id.tv_instruction_step);
            tv_instructions = itemView.findViewById(R.id.tv_instruction_content);

        }
    }
}
