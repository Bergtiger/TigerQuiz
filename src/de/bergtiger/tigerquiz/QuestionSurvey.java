package de.bergtiger.tigerquiz;

import java.util.ArrayList;
import java.util.List;

public class QuestionSurvey extends Question{

	public QuestionSurvey(String title, List<Answer> answers, int size) {
		super(title, answers, size);
	}

	@Override
	boolean getCorrect(int slot) {
		//TODO save answer
		return true;
	}

	@Override
	Question copy() {
		List<Answer> answers = null;
		if((this.answers != null) && (!this.answers.isEmpty())) {
			answers = new ArrayList<Answer>();
			for(Answer answer : this.answers) {
				answers.add(answer.copy());
			}
		}
		return new QuestionSurvey(this.title, answers, this.size);
	}
}
