package de.bergtiger.tigerquiz;

import java.util.ArrayList;
import java.util.List;

public class QuestionQuiz extends Question {

	public QuestionQuiz(String title, boolean obligation, List<Answer> answers, int size) {
		super(title, obligation, answers, size);
	}

	@Override
	boolean getCorrect(int slot) {
		//never trust your code! double check it always
		if((this.answers != null) && (!this.answers.isEmpty())){
			//find the answer - change to hashmap ???
			for (int i = 0; i < this.answers.size(); i++) {
				if(this.answers.get(i).getSlot() == slot){
					return this.answers.get(i).getCorrect();
				}
			}
		}
		return false;
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
		return new QuestionQuiz(this.title, this.obligation, answers, this.size);
	}
}
