package com.dawandeapp.mydictcollectandlearn.Learning.Modes;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.DictsClasses.Pair;
import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.LearningAdapter;

public class TaskTypeTranslation extends LearningAdapter.Task<TaskTypeTranslation.ViewHolder> {
    //Все ID устанавливаются в ChooseModesActivity
    private static int ID = -1;
    public static int getID() {
        return ID;
    }
    public static void setID(int ID) {
        if (TaskTypeTranslation.ID == -1) {
            TaskTypeTranslation.ID = ID;
        }
    }

    public TaskTypeTranslation(Pair pair, int dictType, LearningAdapter LAdapter) {
        super(pair, dictType, LAdapter);
    }

    class ViewHolder extends LearningAdapter.VHolder {
        TextView tx_question;
        Button btn_send;
        EditText edt_answer;

        ViewHolder(View viewHolder) {
            super(viewHolder);
            tx_question = viewHolder.findViewById(R.id.tx_task_question);
            btn_send = viewHolder.findViewById(R.id.btn_task_send);
            edt_answer = viewHolder.findViewById(R.id.edt_task_answer);
        }
    }

    @Override
    public ViewHolder[] getViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_type_translation, parent, false);
        ViewHolder[] viewHolders = new ViewHolder[1];
        viewHolders[0] = new TaskTypeTranslation.ViewHolder(itemView);
        return viewHolders;
    }

    @Override
    public void fillViewHolder(final TaskTypeTranslation.ViewHolder[] holder) {
        holder[0].btn_send.setOnClickListener(v -> {
            boolean isSuccessful = checkAnswer(holder[0].edt_answer.getText().toString());
            if (isSuccessful) {
                //TODO: дописать переход, названияи др
                holder[0].btn_send.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.correct));
            }
            else {
                holder[0].btn_send.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.wrong));
            }
            holder[0].btn_send.setEnabled(false);
            LAdapter.notifyTaskCompleted(isSuccessful);
        });

        holder[0].tx_question.setText(this.getQuestion());
    }
}
