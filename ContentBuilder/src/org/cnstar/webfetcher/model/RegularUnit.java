package org.cnstar.webfetcher.model;

public  class RegularUnit
{
	int step;
	String xpath;
	String option;
	String mapping;
	boolean repeat;
	
	
	public boolean isRepeat() {
		return repeat;
	}
	public void setRepeat(boolean repeat) {
		this.repeat = repeat;
	}
	/**
	 * @return the step
	 */
	public int getStep() {
	    return step;
	}
	/**
	 * @param step the step to set
	 */
	public void setStep(int step) {
	    this.step = step;
	}
	/**
	 * @return the xpath
	 */
	public String getXpath() {
	    return xpath;
	}
	/**
	 * @param xpath the xpath to set
	 */
	public void setXpath(String xpath) {
	    this.xpath = xpath;
	}
	/**
	 * @return the option
	 */
	public String getOption() {
	    return option;
	}
	/**
	 * @param option the option to set
	 */
	public void setOption(String option) {
	    this.option = option;
	}
	/**
	 * @return the mapping
	 */
	public String getMapping() {
	    return mapping;
	}
	/**
	 * @param mapping the mapping to set
	 */
	public void setMapping(String mapping) {
	    this.mapping = mapping;
	}

	
	
}

