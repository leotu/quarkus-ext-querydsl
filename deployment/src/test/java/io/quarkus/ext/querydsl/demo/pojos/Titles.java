package io.quarkus.ext.querydsl.demo.pojos;

import java.io.Serializable;

import javax.annotation.Generated;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Titles is a Querydsl bean type
 */
@RegisterForReflection
@SuppressWarnings("serial")
@Generated("2020-08-21")
public class Titles implements Serializable {

    private Integer empNo;

    private java.time.LocalDate fromDate;

    private String title;

    private java.time.LocalDate toDate;

    public Integer getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Integer empNo) {
        this.empNo = empNo;
    }

    public java.time.LocalDate getFromDate() {
        return fromDate;
    }

    public void setFromDate(java.time.LocalDate fromDate) {
        this.fromDate = fromDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public java.time.LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(java.time.LocalDate toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "empNo = " + empNo + ", fromDate = " + fromDate + ", title = " + title + ", toDate = " + toDate;
    }

    /**
     * Subclass may override this method
     */
    @Override
    public Titles clone() throws CloneNotSupportedException {
        Titles titles = new Titles();
        titles.empNo = this.empNo;
        titles.fromDate = this.fromDate;
        titles.title = this.title;
        titles.toDate = this.toDate;
        return titles;
    }
}
