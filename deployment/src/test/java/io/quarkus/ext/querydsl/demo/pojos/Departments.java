package io.quarkus.ext.querydsl.demo.pojos;

import java.io.Serializable;

import javax.annotation.Generated;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Departments is a Querydsl bean type
 */
@RegisterForReflection
@SuppressWarnings("serial")
@Generated("2020-08-21")
public class Departments implements Serializable {

    private String deptName;

    private String deptNo;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getDeptNo() {
        return deptNo;
    }

    public void setDeptNo(String deptNo) {
        this.deptNo = deptNo;
    }

    @Override
    public String toString() {
        return "deptName = " + deptName + ", deptNo = " + deptNo;
    }

    /**
     * Subclass may override this method
     */
    @Override
    public Departments clone() throws CloneNotSupportedException {
        Departments departments = new Departments();
        departments.deptName = this.deptName;
        departments.deptNo = this.deptNo;
        return departments;
    }
}
