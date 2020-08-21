package io.quarkus.ext.querydsl.demo.pojos;

import java.io.Serializable;

import javax.annotation.Generated;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * DeptEmp is a Querydsl bean type
 */
@RegisterForReflection
@SuppressWarnings("serial")
@Generated("2020-08-21")
public class DeptEmp implements Serializable {

    private String deptNo;

    private Integer empNo;

    private java.time.LocalDate fromDate;

    private java.time.LocalDate toDate;

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

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

    public java.time.LocalDate getToDate() {
        return toDate;
    }

    public void setToDate(java.time.LocalDate toDate) {
        this.toDate = toDate;
    }

    @Override
    public String toString() {
        return "deptNo = " + deptNo + ", empNo = " + empNo + ", fromDate = " + fromDate + ", toDate = " + toDate;
    }

    /**
     * Subclass may override this method
     */
    @Override
    public DeptEmp clone() throws CloneNotSupportedException {
        DeptEmp deptEmp = new DeptEmp();
        deptEmp.deptNo = this.deptNo;
        deptEmp.empNo = this.empNo;
        deptEmp.fromDate = this.fromDate;
        deptEmp.toDate = this.toDate;
        return deptEmp;
    }
}
