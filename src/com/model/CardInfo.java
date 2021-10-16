package com.model;


public class CardInfo {
    private String firstName;
    private String lastName;
    private String nationalCode;
    private String phone;
    private int year;
    private int month;
    private int day;
    private String birthDay;
    private String country;
    private String state;
    private String city;
    private String gender;
    private String contact;
    private byte[] picture;
    private boolean seizure;
    private boolean ADHD;
    private boolean communicationProblem;

    private String custodialName;
    private String custodialLastName;
    private String CSSOID;
    private String custodialNationalCode;
    private String custodialPhone;
    private String autismName;
    private String autismLastName;
    private String autismNationalCode;
    private String ASSOID;
    private byte[] qrCode;
    private String picHash;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getNationalCode() {
        return nationalCode;
    }

    public void setNationalCode(String nationalCode) {
        this.nationalCode = nationalCode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public String getBirthDay() {
        return String.valueOf(year).concat("/")+String.valueOf(month).concat("/")+String.valueOf(day);
    }

    public void setBirthDay(String birthDay) {
        this.birthDay = birthDay;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public boolean isSeizure() {
        return seizure;
    }

    public void setSeizure(boolean seizure) {
        this.seizure = seizure;
    }

    public boolean isADHD() {
        return ADHD;
    }

    public void setADHD(boolean ADHD) {
        this.ADHD = ADHD;
    }

    public boolean getCommunicationProblem() {
        return communicationProblem;
    }

    public void setCommunicationProblem(boolean communicationProblem) {
        this.communicationProblem = communicationProblem;
    }

    public String getCustodialName() {
        return custodialName;
    }

    public void setCustodialName(String custodialName) {
        this.custodialName = custodialName;
    }

    public String getCustodialLastName() {
        return custodialLastName;
    }

    public void setCustodialLastName(String custodialLastName) {
        this.custodialLastName = custodialLastName;
    }

    public String getCSSOID() {
        return CSSOID;
    }

    public void setCSSOID(String CSSOID) {
        this.CSSOID = CSSOID;
    }

    public String getCustodialNationalCode() {
        return custodialNationalCode;
    }

    public void setCustodialNationalCode(String custodialNationalCode) {
        this.custodialNationalCode = custodialNationalCode;
    }

    public String getCustodialPhone() {
        return custodialPhone;
    }

    public void setCustodialPhone(String custodialPhone) {
        this.custodialPhone = custodialPhone;
    }

    public String getAutismName() {
        return autismName;
    }

    public void setAutismName(String autismName) {
        this.autismName = autismName;
    }

    public String getAutismLastName() {
        return autismLastName;
    }

    public void setAutismLastName(String autismLastName) {
        this.autismLastName = autismLastName;
    }

    public String getAutismNationalCode() {
        return autismNationalCode;
    }

    public void setAutismNationalCode(String autismNationalCode) {
        this.autismNationalCode = autismNationalCode;
    }

    public String getASSOID() {
        return ASSOID;
    }

    public void setASSOID(String ASSOID) {
        this.ASSOID = ASSOID;
    }

    public boolean isCommunicationProblem() {
        return communicationProblem;
    }

    public byte[] getQrCode() {
        return qrCode;
    }

    public void setQrCode(byte[] qrCode) {
        this.qrCode = qrCode;
    }

    public String getPicHash() {
        return picHash;
    }

    public void setPicHash(String picHash) {
        this.picHash = picHash;
    }
}
