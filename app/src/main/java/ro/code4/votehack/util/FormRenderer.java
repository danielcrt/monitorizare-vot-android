package ro.code4.votehack.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import ro.code4.votehack.R;
import ro.code4.votehack.constants.QuestionType;
import ro.code4.votehack.net.model.Answer;
import ro.code4.votehack.net.model.Question;
import ro.code4.votehack.net.model.response.ResponseAnswer;
import ro.code4.votehack.widget.AnswerCheckbox;
import ro.code4.votehack.widget.AnswerCheckboxWithDetails;
import ro.code4.votehack.widget.AnswerRadioButton;
import ro.code4.votehack.widget.AnswerRadioButtonWithDetails;
import ro.code4.votehack.widget.AnswerRadioGroup;

public class FormRenderer {
    public static View renderQuestion(Context context, Question question) {
        switch (QuestionType.from(question.getTypeId())) {
            case SINGLE_OPTION:
                return renderSingleAnswerQuestion(context, question);
            case MULTIPLE_OPTIONS:
                return renderMultipleAnswersQuestion(context, question);
            case UNKNOWN:
            default:
                Toast.makeText(context, "Eroare in interpretarea sectiunii", Toast.LENGTH_SHORT).show();
                return new View(context);
        }
    }

    private static View renderMultipleAnswersQuestion(Context context, Question question) {
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        int marginBottom = context.getResources().getDimensionPixelSize(R.dimen.question_option_margin);
        for (Answer answer : question.getAnswerList()) {
            int responseIndex = getPositionResponseIndex(answer.getId(), question.getRaspunsuriIntrebare());
            if (answer.hasManualInput()) {
                AnswerCheckboxWithDetails child = new AnswerCheckboxWithDetails(context);
                setMargins(child, 0, 0, 0, marginBottom);
                child.setAnswer(answer);
                child.setDetail(getDetailFromAnswer(question, responseIndex));
                child.setChecked(responseIndex != -1);
                layout.addView(child, layout.getChildCount());
            } else {
                AnswerCheckbox child = new AnswerCheckbox(context, null, R.attr.customAnswerCheckbox);
                setMargins(child, 0, 0, 0, marginBottom);
                child.setAnswer(answer);
                child.setChecked(responseIndex != -1);
                layout.addView(child, layout.getChildCount());
            }
        }

        return layout;
    }

    private static View renderSingleAnswerQuestion(Context context, Question question) {
        AnswerRadioGroup group = new AnswerRadioGroup(context);
        int marginBottom = context.getResources().getDimensionPixelSize(R.dimen.question_option_margin);
        for(Answer answer : question.getAnswerList()) {
            int responseIndex = getPositionResponseIndex(answer.getId(), question.getRaspunsuriIntrebare());
            if (answer.hasManualInput()) {
                AnswerRadioButtonWithDetails child = new AnswerRadioButtonWithDetails(context);
                setMargins(child, 0, 0, 0, marginBottom);
                child.setOnCheckedChangeListener(group);
                child.setAnswer(answer);
                child.setDetail(getDetailFromAnswer(question, responseIndex));
                child.setChecked(responseIndex != -1);
                group.addView(child, group.getChildCount());
            } else {
                AnswerRadioButton child = new AnswerRadioButton(context, null, R.attr.customAnswerRadioButton);
                setMargins(child, 0, 0, 0, marginBottom);
                child.setOnCheckedChangeListener(group);
                child.setAnswer(answer);
                child.setChecked(responseIndex != -1);
                group.addView(child, group.getChildCount());
            }
        }

        return group;
    }

    private static String getDetailFromAnswer(Question question, int responseIndex) {
        return responseIndex != -1 ? question.getRaspunsuriIntrebare().get(responseIndex).getValue() : "";
    }

    private static int getPositionResponseIndex(Integer idOptiune, List<ResponseAnswer> raspunsuriIntrebare) {
        for(int i=0; i< raspunsuriIntrebare.size(); i++){
            if(raspunsuriIntrebare.get(i).getIdOptiune().equals(idOptiune)){
                return i;
            }
        }
        return -1;
    }

    private static void setMargins(View view, int left, int top, int right, int bottom) {
        if (view.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
            p.setMargins(left, top, right, bottom);
            view.requestLayout();
        }
    }

}
