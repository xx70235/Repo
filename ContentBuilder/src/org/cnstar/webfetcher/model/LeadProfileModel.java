package org.cnstar.webfetcher.model;

public class LeadProfileModel {
    
private int zlid;
private int catid;
private String fname;
private String lname;
private String email;
private String address;
private String state;
private String city;
private String zip;
private String phone;
private String sex;
private String ssn;
private String phone_stateNum;
private String phone_areaNum;
private String phone_exchangeNum;
private String phone_lastNum;

public LeadProfileModel()
{
    
}


/**
 * @return the zlid
 */
public int getZlid() {
    return zlid;
}
/**
 * @param zlid the zlid to set
 */
public void setZlid(int zlid) {
    this.zlid = zlid;
}
/**
 * @return the catid
 */
public int getCatid() {
    return catid;
}
/**
 * @param catid the catid to set
 */
public void setCatid(int catid) {
    this.catid = catid;
}
/**
 * @return the fname
 */
public String getFname() {
    return fname;
}
/**
 * @param fname the fname to set
 */
public void setFname(String fname) {
    this.fname = fname;
}
/**
 * @return the lname
 */
public String getLname() {
    return lname;
}
/**
 * @param lname the lname to set
 */
public void setLname(String lname) {
    this.lname = lname;
}
/**
 * @return the email
 */
public String getEmail() {
    return email;
}
/**
 * @param email the email to set
 */
public void setEmail(String email) {
    this.email = email;
}
/**
 * @return the address
 */
public String getAddress() {
    return address;
}
/**
 * @param address the address to set
 */
public void setAddress(String address) {
    this.address = address;
}
/**
 * @return the state
 */
public String getState() {
    return state;
}
/**
 * @param state the state to set
 */
public void setState(String state) {
    this.state = state;
}
/**
 * @return the city
 */
public String getCity() {
    return city;
}
/**
 * @param city the city to set
 */
public void setCity(String city) {
    this.city = city;
}
/**
 * @return the zip
 */
public String getZip() {
    return zip;
}
/**
 * @param zip the zip to set
 */
public void setZip(String zip) {
    this.zip = zip;
}
/**
 * @return the phone
 */
public String getPhone() {
    return phone;
}
/**
 * @param phone the phone to set
 */
public void setPhone(String phone) {
    this.phone = phone;
}
/**
 * @return the sex
 */
public String getSex() {
    return sex;
}
/**
 * @param sex the sex to set
 */
public void setSex(String sex) {
    this.sex = sex;
}
/**
 * @return the ssn
 */
public String getSsn() {
    return ssn;
}
/**
 * @param ssn the ssn to set
 */
public void setSsn(String ssn) {
    this.ssn = ssn;
}


/**
 * @return the phone_stateNum
 */
public String getPhone_stateNum() {
    return phone_stateNum;
}


/**
 * @param phone_stateNum the phone_stateNum to set
 */
public void setPhone_stateNum(String phone_stateNum) {
    this.phone_stateNum = phone_stateNum;
}


/**
 * @return the phone_areaNum
 */
public String getPhone_areaNum() {
    return phone_areaNum;
}


/**
 * @param phone_areaNum the phone_areaNum to set
 */
public void setPhone_areaNum(String phone_areaNum) {
    this.phone_areaNum = phone_areaNum;
}


/**
 * @return the phone_exchangeNum
 */
public String getPhone_exchangeNum() {
    return phone_exchangeNum;
}


/**
 * @param phone_exchangeNum the phone_exchangeNum to set
 */
public void setPhone_exchangeNum(String phone_exchangeNum) {
    this.phone_exchangeNum = phone_exchangeNum;
}


/**
 * @return the phone_lastNum
 */
public String getPhone_lastNum() {
    return phone_lastNum;
}


/**
 * @param phone_lastNum the phone_lastNum to set
 */
public void setPhone_lastNum(String phone_lastNum) {
    this.phone_lastNum = phone_lastNum;
}



public LeadProfileModel fakeModel()
{
	address = "341 N Yonge St";
	fname = "Emma"; 
	lname = "Jones";
	zip = "32174";
	zlid= 6440;
	catid = 3;
	email="Emma.Jones@hotmail.com";
	state = "FL";
	city = "Ormond beach";
	phone="3866157393";
	phone_areaNum="386";
	phone_exchangeNum="615";
	phone_lastNum="7393";
	sex="F";
	ssn="12113412";
	return this;
	
	
}
}

