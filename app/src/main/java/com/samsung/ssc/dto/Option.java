package com.samsung.ssc.dto;


public class Option {


	private String optionValue;
	private int sequence;
	private int surveyOptionId;
	private int surveyQuestionId;
	private boolean IsAffirmative;

	/**
	 * @return the optionValue
	 */
	public String getOptionValue() {
		return optionValue;
	}

	/**
	 * @param optionValue
	 *            the optionValue to set
	 */
	public void setOptionValue(String optionValue) {
		this.optionValue = optionValue;
	}

	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return sequence;
	}

	/**
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	/**
	 * @return the surveyOptionId
	 */
	public int getSurveyOptionId() {
		return surveyOptionId;
	}

	/**
	 * @param surveyOptionId
	 *            the surveyOptionId to set
	 */
	public void setSurveyOptionId(int surveyOptionId) {
		this.surveyOptionId = surveyOptionId;
	}

	/**
	 * @return the surveyQuestionId
	 */
	public int getSurveyQuestionId() {
		return surveyQuestionId;
	}

	/**
	 * @param surveyQuestionId
	 *            the surveyQuestionId to set
	 */
	public void setSurveyQuestionId(int surveyQuestionId) {
		this.surveyQuestionId = surveyQuestionId;
	}

	public boolean isIsAffirmative() {
		return IsAffirmative;
	}

	public void setIsAffirmative(boolean isAffirmative) {
		IsAffirmative = isAffirmative;
	}

}
