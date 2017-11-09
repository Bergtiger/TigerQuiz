package de.bergtiger.tigerquiz;

public class QuestionQuiz extends Question {

	@Override
	boolean getCorrect(int slot) {
		//never trust your code! double check it always
		if(this.answers != null && this.answers.isEmpty()){
			//find the answer - change to hashmap ???
			for (int i = 0; i < this.answers.size(); i++) {
				if(this.answers.get(i).getSlot() == slot){
					return this.answers.get(i).getCorrect();
				}
			}
		}
		return false;
	}

}
