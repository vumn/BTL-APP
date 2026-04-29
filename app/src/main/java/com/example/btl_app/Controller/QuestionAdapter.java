package com.example.btl_app.Controller;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.btl_app.Model.Question;
import com.example.btl_app.R;

import java.util.List;

public class QuestionAdapter extends BaseAdapter {

    static class ViewHolderQuestion {
        TextView lvIdQuestion;
        TextView lvContentQuestion;
        TextView lvAnswersQuestion;
        TextView lvCorrectQuestion;
        TextView lvLevelQuestion;
    }

    private Context context;
    private List<Question> questionList;

    public QuestionAdapter(Context context, List<Question> questionList) {
        this.context = context;
        this.questionList = questionList;
    }

    @Override
    public int getCount() {
        return questionList.size();
    }

    @Override
    public Object getItem(int i) {
        return questionList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ViewHolderQuestion viewHolderQuestion;

        if(view == null)
        {
            LayoutInflater inflater = LayoutInflater.from(context);
            view = inflater.inflate(R.layout.item_question, viewGroup, false);
            viewHolderQuestion = new ViewHolderQuestion();
            viewHolderQuestion.lvContentQuestion = view.findViewById(R.id.lvContentQuestion);
            viewHolderQuestion.lvIdQuestion = view.findViewById(R.id.lvIdQuestion);
            viewHolderQuestion.lvAnswersQuestion = view.findViewById(R.id.lvAnswersQuestion);
            viewHolderQuestion.lvCorrectQuestion = view.findViewById(R.id.lvCorrectQuestion);
            viewHolderQuestion.lvLevelQuestion = view.findViewById(R.id.lvLevelQuestion);

            view.setTag(viewHolderQuestion);
        }else{
            viewHolderQuestion = (ViewHolderQuestion) view.getTag();
        }


        Question question = questionList.get(i);

        viewHolderQuestion.lvIdQuestion.setText(question.getQuestionId());
        viewHolderQuestion.lvContentQuestion.setText(question.getContent());
        if(question.getAnswers() != null)
        {
            viewHolderQuestion.lvAnswersQuestion.setText(TextUtils.join(", ", question.getAnswers()));
        }else{
            viewHolderQuestion.lvAnswersQuestion.setText("");
        }
        viewHolderQuestion.lvCorrectQuestion.setText(String.valueOf(question.getCorrectIndex()));
        viewHolderQuestion.lvLevelQuestion.setText(String.valueOf(question.getLevel()));

        return view;
    }
}
