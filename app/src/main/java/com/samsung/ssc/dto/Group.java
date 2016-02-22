package com.samsung.ssc.dto;

import java.util.ArrayList;
import java.util.List;

import com.samsung.ssc.util.Helper;

public class Group {
	private String hintImageName;

	private boolean active;

	private boolean dailyQuote;

	private int moduleId;

	private List<Option> options;

	private int productGroupId;

	// TODO: check type, Currently returning null
	private int productTypeId;

	private String question;

	private int questionTypeId;

	// TODO: check type, Currently returning null
	private String quoteDate;

	private int roleId;

	private int sequence;

	private int surveyQuestionId;

	private ArrayList<Child> childs;

	public String getHintImageName() {
		return hintImageName;
	}

	public void setHintImageName(String hintImageName) {
		this.hintImageName = hintImageName;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isDailyQuote() {
		return dailyQuote;
	}

	public void setDailyQuote(boolean dailyQuote) {
		this.dailyQuote = dailyQuote;
	}

	public int getModuleId() {
		return moduleId;
	}

	public void setModuleId(int moduleId) {
		this.moduleId = moduleId;
	}

	public List<Option> getOptions() {
		return options;
	}

	public void setOptions(List<Option> options) {
		this.options = options;
	}

	public int getProductGroupId() {
		return productGroupId;
	}

	public void setProductGroupId(int productGroupId) {
		this.productGroupId = productGroupId;
	}

	public int getProductTypeId() {
		return productTypeId;
	}

	public void setProductTypeId(int productTypeId) {
		this.productTypeId = productTypeId;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getQuestionTypeId() {
		return questionTypeId;
	}

	public void setQuestionTypeId(int questionTypeId) {
		this.questionTypeId = questionTypeId;
	}

	public String getQuoteDate() {
		return quoteDate;
	}

	public void setQuoteDate(String quoteDate) {
		this.quoteDate = quoteDate;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

	public int getSurveyQuestionId() {
		return surveyQuestionId;
	}

	public void setSurveyQuestionId(int surveyQuestionId) {
		this.surveyQuestionId = surveyQuestionId;
	}

	public ArrayList<Child> getChilds() {
		return childs;
	}

	public void setChilds(ArrayList<Child> childs) {
		this.childs = childs;
	}

	public boolean anyChildHasValue() {
		boolean flag = false;

		for (Child child : getChilds()) {
			if (!Helper.isNullOrEmpty(child.getValue())) {
				flag = true;
			}
		}

		return flag;

	}

}
