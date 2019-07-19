package com.dawandeapp.mydictcollectandlearn.Learning.Modes;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.DictsClasses.Pair;
import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.LearningAdapter;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;

public class TaskChooseTranslation extends LearningAdapter.Task<TaskChooseTranslation.ViewHolder> {
    //Все ID устанавливаются в ChooseModesActivity
    private static int ID = -1;
    public static int getID() {
        return ID;
    }
    public static void setID(int ID) {
        if (TaskChooseTranslation.ID == -1) {
            TaskChooseTranslation.ID = ID;
        }
    }
    private static int NUM = 4;
    public static int getNUM() {
        return NUM;
    }
    public static void setNUM(int NUM) {
        TaskChooseTranslation.NUM = NUM;
    }

    private String[] allAnswers;

    private int dictType;
    public TaskChooseTranslation(Pair pairAns, Pair[] pairAll, int dictType, LearningAdapter LAdapter) {
        super(pairAns, dictType, LAdapter);
        allAnswers = new String[TaskChooseTranslation.NUM];
        for(int i=0; i<pairAll.length; i++) {
            if (dictType == 1) {
                allAnswers[i] = pairAll[i].getTranslation();
            }
            else {
                allAnswers[i] = pairAll[i].getForeign();
            }
        }
    }

    class ViewHolder extends LearningAdapter.VHolder {
        TextView tx_question;
        TableLayout TL_task_buttons;
        Button[] btn_answers;

        public ViewHolder(View viewHolder, int varNum, String[] answers) {
            super(viewHolder);
            btn_answers = new Button[varNum];
            TL_task_buttons = viewHolder.findViewById(R.id.TL_task_buttons);
            tx_question = viewHolder.findViewById(R.id.tx_task_question);
        }
    }

    @Override
    public ViewHolder[] getViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_choose_translation, parent, false);
        ViewHolder[] viewHolders = new ViewHolder[1];
        viewHolders[0] = new TaskChooseTranslation.ViewHolder(itemView, TaskChooseTranslation.NUM, allAnswers);
        return viewHolders;
    }

    @Override
    public void fillViewHolder(TaskChooseTranslation.ViewHolder[] holder) {
        M.d(String.valueOf(getNUM()));
        for (int i=0; i<getNUM(); i++) {
            holder[0].btn_answers[i] = new Button(holder[0].getContext());
            holder[0].btn_answers[i].setText(allAnswers[i]);
            holder[0].btn_answers[i].setTextSize(20f);
            holder[0].btn_answers[i].setLayoutParams(new LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f));
            holder[0].btn_answers[i].setOnClickListener(v -> {
                Button btn = (Button) v;
                boolean isSuccessful = checkAnswer(btn.getText().toString());
                if (isSuccessful) {
                    btn.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.correct));
                }
                else {
                    btn.setBackgroundColor(ContextCompat.getColor(v.getContext(), R.color.wrong));
                }
                for (Button b: holder[0].btn_answers) {
                    b.setEnabled(false);
                }
                LAdapter.notifyTaskCompleted(isSuccessful);
            });
        }
        if (getNUM()>=2 && getNUM() <=4) {
            TableRow TRow_top = new TableRow(holder[0].getContext());
            for (int i=0; i<2; i++) {
                TRow_top.addView(holder[0].btn_answers[i]);
            }
            TRow_top.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                    LayoutParams.WRAP_CONTENT));
            holder[0].TL_task_buttons.addView(TRow_top);
            if (getNUM()>=3) {
                TableRow TRow_bottom = new TableRow(holder[0].getContext());
                TRow_bottom.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.WRAP_CONTENT));
                TRow_bottom.addView(holder[0].btn_answers[2]);
                if (getNUM()==4) {
                    TRow_bottom.addView(holder[0].btn_answers[3]);
                }
                holder[0].TL_task_buttons.addView(TRow_bottom);
            }
        }
        else {
            M.d("varNum is not 2, 3 or 4. It's ".concat(String.valueOf(getNUM())));
        }
        holder[0].tx_question.setText(this.getQuestion());
    }
}
