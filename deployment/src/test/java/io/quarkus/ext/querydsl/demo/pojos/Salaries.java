package io.quarkus.ext.querydsl.demo.pojos;

import java.io.Serializable;

import javax.annotation.Generated;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Salaries is a Querydsl bean type
 */
@RegisterForReflection
@SuppressWarnings("serial")
@Generated("2020-08-21")
public class Salaries implements Serializable {

    private Integer empNo;

    private java.time.LocalDate fromDate;

    private Integer salary;

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

    public Integer getSalary() {
        return salary;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public java.time.LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(java.time.LocalDate toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "empNo = " + empNo + ", fromDate = " + fromDate + ", salary = " + salary + ", toDate = " + toDate;
    }

    /**
     * Subclass may override this method
     */
    @Override
    public Salaries clone() throws CloneNotSupportedException {
        Salaries salaries = new Salaries();
        salaries.empNo = this.empNo;
        salaries.fromDate = this.fromDate;
        salaries.salary = this.salary;
        salaries.toDate = this.toDate;
        return salaries;
    }
}
