package com.samsung.ssc.dto;

import java.util.List;

public class Question {

	/**
	 *
	 */
    private int moduleID;
	private List<Option> optionList;
	private int productGroupID;
	private int productTypeId;
	private String questionText;
	private int questionTypeID;
	private int sequenceQuestion;
	private int surveyQuestionId;
	private String userResponse;
	private int textLength;
	private int dependentOptionID;
	private String surveyQuestionImage;
	private int moduleCode;
	
	
	private String subQuestionID;
	
	
	private boolean isMandatory;
	private int repeaterTypeID;
	private String repeaterText;
	private int repeatMaxTimes;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + surveyQuestionId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question other = (Question) obj;
		if (surveyQuestionId != other.surveyQuestionId)
			return false;
		return true;
	}

	/**
	 * @return the moduleId
	 */
	public int getModuleId() {
		return moduleID;
	}

	/**
	 * @param moduleId
	 *            the moduleId to set
	 */
	public void setModuleId(int moduleId) {
		this.moduleID = moduleId;
	}

	/**
	 * @return the options
	 */
	public List<Option> getOptions() {

		return optionList;
	}

	/**
	 * @param options
	 *            the options to set
	 */
	public void setOptions(List<Option> options) {
		this.optionList = options;
	}

	/**
	 * @return the productGroupId
	 */
	public int getProductGroupId() {
		return productGroupID;
	}

	/**
	 * @param productGroupId
	 *            the productGroupId to set
	 */
	public void setProductGroupId(int productGroupId) {
		this.productGroupID = productGroupId;
	}

	/**
	 * @return the productTypeId
	 */
	public int getProductTypeId() {
		return productTypeId;
	}

	/**
	 * @param productTypeId
	 *            the productTypeId to set
	 */
	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	/**
	 * @return the question
	 */
	public String getQuestion() {
		return questionText;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(String question) {
		this.questionText = question;
	}

	/**
	 * @return the questionTypeId
	 */
	public int getQuestionTypeId() {
		return questionTypeID;
	}

	/**
	 * @param questionTypeId
	 *            the questionTypeId to set
	 */
	public void setQuestionTypeId(int questionTypeId) {
		this.questionTypeID = questionTypeId;
	}

	/**
	 * @return the sequence
	 */
	public int getSequence() {
		return sequenceQuestion;
	}

	/**
	 * @param sequence
	 *            the sequence to set
	 */
	public void setSequence(int sequence) {
		this.sequenceQuestion = sequence;
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

	/**
	 * @return the textLength
	 */
	public int getTextLength() {
		return textLength;
	}

	/**
	 * @param textLength
	 *            the textLength to set
	 */
	public void setTextLength(int textLength) {
		this.textLength = textLength;
	}

	/**
	 * @return the dependentOptionID
	 */
	public int getDependentOptionID() {
		return dependentOptionID;
	}

	/**
	 * @param dependentOptionID
	 *            the dependentOptionID to set
	 */
	public void setDependentOptionID(int dependentOptionID) {
		this.dependentOptionID = dependentOptionID;
	}

	public int getModuleCode() {
		return moduleCode;
	}

	public void setModuleCode(int moduleCode) {
		this.moduleCode = moduleCode;
	}

	public String getSurveyQuestionImage() {
		return surveyQuestionImage;
	}

	public void setSurveyQuestionImage(String surveyQuestionImage) {
		this.surveyQuestionImage = surveyQuestionImage;
	}

	public String getUserResponse() {
		return userResponse;
	}

	public void setUserResponse(String userResponse) {
		this.userResponse = userResponse;
	}

	public int getRepeatMaxTimes() {
		return repeatMaxTimes;
	}

	public void setRepeatMaxTimes(int repeatMaxTimes) {
		this.repeatMaxTimes = repeatMaxTimes;
	}

	public String getRepeaterText() {
		return repeaterText;
	}

	public void setRepeaterText(String repeaterText) {
		this.repeaterText = repeaterText;
	}

	public int getRepeaterTypeID() {
		return repeaterTypeID;
	}

	public void setRepeaterTypeID(int repeaterTypeID) {
		this.repeaterTypeID = repeaterTypeID;
	}

	public boolean isMandatory() {
		return isMandatory;
	}

	public void setMandatory(boolean isMandatory) {
		this.isMandatory = isMandatory;
	}

	public String getSubQuestionID() {
		return subQuestionID;
	}

	public void setSubQuestionID(String subQuestionID) {
		this.subQuestionID = subQuestionID;
	}

}
