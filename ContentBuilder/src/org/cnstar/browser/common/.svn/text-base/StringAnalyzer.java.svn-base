/*********************************************************************************
 *
 * Copyright (C) 2007  Micael Gallego Carrillo,
 * 					   Alberto Pérez García-Plaza,
 * 					   LADyR Team.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Micael Gallego Carrillo
 *     Alberto Pérez García-Plaza
 *     LADyR Team (http://www.ladyr.es/)
 * 
 * Contact information: info-ladyr at gsyc.es
 *
 *********************************************************************************/
package es.ladyr.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class StringAnalyzer {
	
	private static final String IS_FULL_POSTCODE_REGEX = "^(([A-Za-z]{1,2}[\\d]{1,2}|GIR|[A-Za-z]{1,2}[\\d][A-Za-z])[\\s-]*([\\d][A-Za-z]{2}))$";
	private final static String IS_PARTIAL_POSTCODE_REGEX = "^([A-Za-z]{1,2}[\\d]{1,2}|GIR|[A-Za-z]{1,2}[\\d][A-Za-z])$";
	private static final String FULL_POSTCODE_REGEX = "^(([A-Za-z]{1,2}[\\d]{1,2}|GIR|[A-Za-z]{1,2}[\\d][A-Za-z])[\\s-]*([\\d][A-Za-z]{2}))$";
	private final static String PARTIAL_POSTCODE_REGEX = "^([A-Za-z]{1,2}[\\d]{1,2}|GIR|[A-Za-z]{1,2}[\\d][A-Za-z])$";
	private final static String COMPARE_REGEX = "[^\\w\\d\\.,&]*+";

	
	/**
	 * Changes sets of \n, \r, \s and &nbsp; (non-breaking space) chars,
	 * even if they are duplicated or combined, into a single whitespaces
	 * @param text
	 * @return
	 */
	public static String normalize(String text){
		if ( text != null ){
			return trim(text.replaceAll("[\u00A0\\n\\r\\s]+", " "));
		} else {
			return null;
		}
	}
	
	public static List<String> normalize (List<String> textList) {
		List<String> results = new ArrayList<String>();
		for ( String text: textList ) {
			results.add(normalize(text));
		}
		return results;
	}
	
	/**
	 * Remove heading and trailing spaces. Support <code>null</code> values.
	 * @return	the String trimmed or <code>null</code> if <code>str</code>
	 * 	is <code>null</code>.
	 */
	public static String trim(String str){
		if ( str == null ){
			return null;
		} else {
			return str.trim();
		}
	}
	
	/**
	 * 
	 * @param postCodeStr
	 * @return	a String containing a full UK postal code if that 
	 * 	code is part of the input String. <code>null</code> in any
	 * 	other case.
	 */
	public static String parseFullPostCode( String postCodeStr ){
		return extract(postCodeStr, FULL_POSTCODE_REGEX);
	}
	
	/**
	 * 
	 * @param postCodeStr
	 * @return	a String containing a partial UK postal code if that 
	 * 	code is part of the input String. <code>null</code> in any
	 * 	other case.
	 */
	public static String parsePartialPostCode( String postCodeStr ){
		return extract(postCodeStr, PARTIAL_POSTCODE_REGEX);
	}
	
	/**
	 * Checks postal codes and detect if the input String is a 
	 * partial UK postal code.
	 * @param postCode
	 * @return	<code>true</code> if the input String corresponds
	 * 	with the format of a partial UK postal code. In any other
	 *  case returns <code>false</code>
	 */
	public static boolean isPartialPostCode(String postCode){
		if ( postCode != null &&
				!postCode.equals("") && 
				 matches(postCode, IS_PARTIAL_POSTCODE_REGEX) ){
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Checks postal codes and detect if the input String is a 
	 * full UK postal code.
	 * @param postCode
	 * @return	<code>true</code> if the input String corresponds
	 * 	with the format of a UK postal code. In any other case
	 * 	returns <code>false</code>
	 */
	public static boolean isFullUKPostCode(String postCode){
		if ( postCode != null &&
				!postCode.equals("") && 
				 matches(postCode, IS_FULL_POSTCODE_REGEX) ){
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * Substitute all characters matching <code>[^\\w\\d\\.,&]*+</code>
	 * for single spaces, trims the result, and then compare the two
	 * Strings. Supports <code>null</code> values. 
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static boolean compareNormalized(String str1, String str2){
		
		if ( str1 == null && str2 == null ){
			return true;
		} else if ( str1 == null || str2 == null ) {
			return false;
		}
		
		str1 = str1.replaceAll(COMPARE_REGEX, " ").trim();
		str2 = str2.replaceAll(COMPARE_REGEX, " ").trim();
		
		return str1.equalsIgnoreCase(str2); 
	}
	
	
	/**
	 * 
	 * @param str	String we want to check
	 * @param regEx	regular expression we have to check
	 * @return	<code>true</code> if String <code>str</code> matches the
	 *	pattern regEx. <code>false</code> in any other case.
	 */
	public static boolean matches(String str, String regEx){
		try {
			Pattern p = Pattern.compile(regEx,
					Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			Matcher m = p.matcher(str);
	
			if (m.find()) {
				 return true;
			}
		} catch (NullPointerException e){
			return false;
		}
		
		return false;
		
	}
	
	/**
	 * 
	 * @param text
	 * @param regEx	The text we want to extract must correspond to group number 1
	 * 	within the regular expression. <b>This parameter can be null and means
	 *  that nothing has to be done with the text</b>, see return section for 
	 *  more details.
	 * @return	If the regular expression is <code>null</code>,
	 *  then the input String is returned without changes. If no matches found, the
	 *  method returns <code>null</code>. In any other case, returns the text.
	 *  <code>String</code> that matches with the first group of the regular
	 *  expression. 
	 */
	public static String extract(String text, String regEx){

		if ( regEx == null || regEx.equals("") ) {
			return text;
		} 
		
		try {
			Pattern p = Pattern.compile(regEx,
					Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			Matcher m = p.matcher(text);
	
			if (m.find()) {
				 return m.group(1);
			}
		} catch (NullPointerException e){
			return null;
		}
		
		return null;
	}

	/**
	 * 
	 * @param str
	 * @param regEx	The text we want to extract must correspond to group number 1
	 * 	within the regular expression. <b>This parameter can be null and means
	 *  that nothing has to be done with the text</b>, see return section for 
	 *  more details.
	 * @return	text that matches with the regular expression trimmed. If the
	 *	regular expression is <code>null</code>, then returns the whole text
	 *	trimmed. If regular expression fails, or the input text
	 *  does not match the regular expression, <code>null</code> is 
	 *  returned. 
	 */
	public static String extractTrim(String str, String regEx) {
		String match = extract(str,regEx);
		return trim(match);
	}
	
	/**
	 * 
	 * @param str
	 * @param regEx	The text we want to extract must correspond to group number 1
	 * 	within the regular expression. <b>This parameter can be null and means
	 *  that nothing has to be done with the text</b>, see return section for 
	 *  more details.
	 * @return	text that matches with the regular expression
	 * 	normalized. If the regular expression is <code>null</code>,	then returns
	 *  the whole text normalized. If regular expression fails, or the input text
	 *  does not match the regular expression, <code>null</code> is 
	 *  returned. 
	 */
	public static String extractNorm(String str, String regEx) {
		String match = extract(str,regEx);
		return normalize(match);
	}
	
	/**
	 * 
	 * @param text
	 * @param regExp
	 * @return	An array with [start index of the subsequence captured by the group 0,
	 *  offset after the last character of the subsequence captured by the group 0,
	 *  start index of the subsequence captured by the group 1,
	 *  offset after the last character of the subsequence captured by the group 1,
	 *  the input subsequence captured by the group 1] or <code>null</code> if the
	 *  text doesn't match the regular expression. 
	 * @throws Exception
	 * @throws PatternSyntaxException
	 */
	public static String[] extractGroups(String text, String regExp) throws Exception, PatternSyntaxException {
		
		if ( regExp == null || regExp.equals("") ) {
			return new String[] {
					"0",
					Integer.toString( text.length() ),
					"0",
					Integer.toString( text.length() ),
					text };
		} 
		try {
			Pattern p = Pattern.compile(regExp,
					Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
			Matcher m = p.matcher(text);			
			if ( m.find() && m.groupCount() > 0 ){
				return new String[] {
					Integer.toString( m.start(0) ),
					Integer.toString( m.end(0) ),
					Integer.toString( m.start(1) ), 
					Integer.toString( m.end(1) ),
					m.group(1)};
			}
		} catch(PatternSyntaxException e){
			throw e;
			
		} catch (Exception e){
			throw e;
		}
		
		return null;
	}	

	/**
	 * 
	 * @param text
	 * @param regEx	the <code>Integer</code> we want to extract must
	 *  correspond to group number 1 within the regular expression.
	 *  <b>This parameter can be null and means that nothing has to
	 *  be done with the text</b>, see return section for more details.
	 * @return	Integer corresponding to the first group within the
	 * 	regular expression. If the regular expression is <code>null</code>,
	 * 	then returns the <code>Integer</code> corresponding to the whole
	 *  text. If parsing fails, regular expression fails, or the input text
	 *  does not match the regular expression, <code>null</code> is 
	 *  returned.
	 */
	public static Integer extractInt(String text, String regEx) {
		String value = extract(text, regEx);
		if( value == null ){
			return null;
		} else {
			try {
				return Integer.parseInt(value);
			} catch(NumberFormatException e){
				return null;
			}
		}
	}
	
	public static List<Integer> extractInt(List<String> texts, String regEx) {
		List<Integer> ilist = new ArrayList<Integer>();
		
		for ( String str: texts ) {
			ilist.add(extractInt(str, regEx));
		}
		
		return ilist;
	}
	
	/**
	 * 
	 * @param integer	an <code>Integer</code> in <code>String</code>
	 * 	format.
	 * @return	the <code>Integer</code> corresponding to the 
	 * 	input text. <code>null</code> if the input text does not
	 * 	correspond with an <code>Integer</code>.
	 */
	public static Integer parseInt(String integer) {
		return extractInt(integer, null);
	}
	
	public static List<Integer> parseInt( List<String> integers ) {
		List<Integer> ilist = new ArrayList<Integer>();
		
		for ( String str: integers ) {
			ilist.add(parseInt(str));
		}
		
		return ilist;
	}
	
	/**
	 * 
	 * @param text
	 * @param regEx	the <code>BigDecimal</code> we want to extract must 
	 * 	correspond to group number 1 within the regular expression.
	 *  <b>This parameter can be null and means that nothing has to be
	 *  done with the text</b>, see return section for more details.
	 * @return	<code>BigDecimal</code> corresponding to the first group
	 *  within the regular expression. If the regular expression is
	 *  <code>null</code>, then returns the <code>BigDecimal</code>
	 *  corresponding to the whole text. If parsing fails, regular
	 *  expression fails, or the input text does not match the regular
	 *  expression, <code>null</code> is returned. 
	 */
	public static BigDecimal extractBigDecimal(String text, String regEx) {
		String value = extract(text,regEx);
		if(value == null){
			return null;
		} else {
			try {
				return new BigDecimal(value);
			} catch(NumberFormatException e){
				return null;
			}
		}
	}
	
	/**
	 * 
	 * @param bigDecimal	a <code>BigDecimal</code in 
	 * 	<code>String</code> format.
	 * @return	the <code>BigDecimal</code> corresponding to the 
	 * 	input text. <code>null</code> if the input text does not
	 * 	correspond with a <code>BigDecimal</code>.
	 */
	public static BigDecimal parseBigDecimal(String bigDecimal) {
		return extractBigDecimal(bigDecimal, null);
	}
	
}
