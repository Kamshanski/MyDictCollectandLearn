package com.dawandeapp.mydictcollectandlearn.Learning.Modes;

import android.support.constraint.motion.MotionLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.DictsClasses.Pair;
import com.dawandeapp.mydictcollectandlearn.R;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.LearningAdapter;
import com.dawandeapp.mydictcollectandlearn.zHelpClasses.M;

public class TaskTrustCard extends LearningAdapter.Task<TaskTrustCard.ViewHolder> {
    //Все ID устанавливаются в ChooseModesActivity
    private static int ID = -1;
    public static int getID() {
        return ID;
    }
    public static void setID(int ID) {
        if (TaskTrustCard.ID == -1) {
            TaskTrustCard.ID = ID;
        }
    }

    private int dictType;

    public TaskTrustCard(Pair pair, int dictType, LearningAdapter LAdapter) {
        super(pair, dictType, LAdapter);
    }

    class ViewHolder extends LearningAdapter.VHolder {

        TextView tx_question, tx_answer;
        FrameLayout CL_top, CL_bottom;
        MotionLayout ML_trust_card;

        public ViewHolder(View viewHolder) {
            super(viewHolder);

            ML_trust_card = viewHolder.findViewById(R.id.ML_trust_card);
            tx_question = viewHolder.findViewById(R.id.tx_task_question);
            tx_answer = viewHolder.findViewById(R.id.tx_task_answer);
            CL_top = viewHolder.findViewById(R.id.CL_top_card);
            CL_bottom = viewHolder.findViewById(R.id.CL_bottom_card);

            CL_top.setOnClickListener(CLv -> {
                M.d("top_clicked");
//                ML_trust_card.transitionToState(R.id.flip_keyFrameSet);
                ML_trust_card.transitionToEnd();
            });

            CL_bottom.setOnClickListener(CLv -> {
                M.d("bottom_clicked");
//                ML_trust_card.transitionToState(R.id.flip_keyFrameSet);
                ML_trust_card.transitionToStart();
           });
//
//            tx_question.setOnClickListener(v -> {
//                M.d("tx_question");
//            });
//            tx_answer.setOnClickListener(v -> {
//                M.d("tx_answer");
//            });
//            FL_question.setOnClickListener(v -> {
//                M.d("FL_question");
//            });
//            FL_answer.setOnClickListener(v -> {
//                M.d("FL_answer");
//            });
        }
    }

    @Override
    public ViewHolder[] getViewHolder(ViewGroup parent) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_trust_card, parent, false);
        ViewHolder[] viewHolders = new ViewHolder[1];
        viewHolders[0] = new TaskTrustCard.ViewHolder(itemView);
        return viewHolders;
    }

    @Override
    public void fillViewHolder(TaskTrustCard.ViewHolder[] holder) {
        M.d(String.valueOf(getID()));

    }
}
