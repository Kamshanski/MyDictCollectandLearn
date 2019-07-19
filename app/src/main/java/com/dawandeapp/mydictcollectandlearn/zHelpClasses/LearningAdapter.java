package com.dawandeapp.mydictcollectandlearn.zHelpClasses;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.dawandeapp.mydictcollectandlearn.DictsClasses.Dict;
import com.dawandeapp.mydictcollectandlearn.DictsClasses.Pair;
import com.dawandeapp.mydictcollectandlearn.Learning.FinishActivity;
import com.dawandeapp.mydictcollectandlearn.Learning.Modes.TaskChooseTranslation;
import com.dawandeapp.mydictcollectandlearn.Learning.Modes.TaskTrustCard;
import com.dawandeapp.mydictcollectandlearn.Learning.Modes.TaskTypeTranslation;
import com.dawandeapp.mydictcollectandlearn.R;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class LearningAdapter
{
    private LearningView parent;
    private Dict dict = null;
    private int[] modes = null;
    private Button btn_continue;
    private TextView tx_counter;
    private int correctAnswers;
    private Queue<Task> tasksQueue = new LinkedList<>();

    public LearningAdapter(Dict dict, int[] modes, Button btn_continue, TextView tx_counter, LearningView learningView) {
        this.dict = dict;
        this.modes = modes;
        this.btn_continue = btn_continue;
        this.tx_counter = tx_counter;
        this.parent = learningView;

        setCorrespondingTasks();

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                M.d(String.valueOf(tasksQueue.size()));
                if (tasksQueue.size()!=0) {
                    M.d(String.valueOf(tasksQueue.size()!=0));
                    nextLearning();
                }
                else {
                    M.d(String.valueOf(tasksQueue.size()!=0));
                    M.d("finishing learning");
                    finishLearning();
                }
            }
        });

        startLearning();
    }

    public void setCorrespondingTasks() {
        Random random = new Random();
        for (Pair pair : dict.getPairs()) {
            switch (modes[random.nextInt(modes.length)]) {   // новое число = [0;n)
                case 0:
                    tasksQueue.add(new TaskTypeTranslation(pair, dict.getDictType(), this));
                    break;
                case 1:
                    tasksQueue.add(createTaskChooseTranslation(pair));
                    break;
                case 2:
                    tasksQueue.add(new TaskTrustCard(pair, dict.getDictType(), this));
            }
        }
    }

    public void startLearning() {
        btn_continue.setEnabled(false);
        Task task = tasksQueue.poll();
        //TODO: try instanceof task.viewholder
        if (task != null) {
            if (task instanceof TaskTrustCard) {
                btn_continue.setVisibility(View.GONE);
            }
            else {
                btn_continue.setVisibility(View.VISIBLE);
            }
            LearningAdapter.VHolder[] vh = task.getViewHolder(parent);
            task.fillViewHolder(vh);
            parent.addView(vh[0].getView());
        }
    }

    public TaskChooseTranslation createTaskChooseTranslation(Pair pairAns) {
        HashSet<Pair> set = new HashSet<>();
        set.add(pairAns);
        for (Random random = new Random(); set.size() != TaskChooseTranslation.getNUM();) {
            int pos = random.nextInt() % dict.size();
            set.add(dict.getPair((pos<0)?-pos:pos));
        }
        Pair[] pairsAll = new Pair[set.size()];
        pairsAll = set.toArray(pairsAll);

        return new TaskChooseTranslation(pairAns, pairsAll, dict.getDictType(), this);
    }

    public void notifyTaskCompleted(boolean isSuccessful) {
        btn_continue.setEnabled(true);
        Resources res = tx_counter.getResources();
        tx_counter.setText(String.format(res.getString(R.string.tx_Learning_counter),
                dict.size() - tasksQueue.size(),
                dict.size()));
        correctAnswers += isSuccessful ? 1 : 0;
    }

    private void nextLearning() {
        parent.removeAllViews();
        startLearning();
    }

    private void finishLearning() {
        //Передаём название словаря, чтобы его открыть
        Intent intent = new Intent(parent.getContext(), FinishActivity.class);
        intent.putExtra("toFinish_dictName", dict.getDictName());
        intent.putExtra("toFinish_dictSize", dict.size());
        intent.putExtra("toFinish_correctAnswers", correctAnswers);
        parent.getContext().startActivity(intent);
    }


    public static abstract class Task<VH extends VHolder>
    {
        String question;
        String answer;
        protected LearningAdapter LAdapter;

        public Task(Pair pair, int dictType, LearningAdapter ladapter) {
            if (dictType==1 || (new Random()).nextBoolean()) {
                question = pair.getForeign().toLowerCase();
                answer = pair.getTranslation().toLowerCase();
                LAdapter = ladapter;
            }
            else {
                question = pair.getTranslation().toLowerCase();
                answer = pair.getForeign().toLowerCase();
            }
        }

        public String getQuestion() {
            return question;
        }
        public boolean checkAnswer(String givenAnswer) {
            return (answer.equals(givenAnswer.trim().toLowerCase()));
        }
        public String getAnswer() {
            return answer;
        }

        public abstract VH[] getViewHolder(ViewGroup parent);
        public abstract void fillViewHolder(VH[] holder);
        public Context getContext() {
            return LAdapter.parent.getContext();
        }
    }

    public abstract static class VHolder {
        View itemView;
        public VHolder(View itemView) {
            if (itemView == null) {
                M.d("itemView не может быть нулевым");
                throw new IllegalArgumentException("itemView не может быть нулевым");
            } else {
                this.itemView = itemView;
            }
        }
        public Context getContext() {
            return itemView.getContext();
        }
        public View getView() { return itemView; }
    }
}
