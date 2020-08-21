package io.quarkus.ext.querydsl.demo.pojos;

import java.io.Serializable;

import javax.annotation.Generated;

import io.quarkus.runtime.annotations.RegisterForReflection;

/**
 * Employees is a Querydsl bean type
 */
@RegisterForReflection
@SuppressWarnings("serial")
@Generated("2020-08-21")
public class Employees implements Serializable {

    private java.time.LocalDate birthDate;

    private Integer empNo;

    private String firstName;

    private String gender;

    private java.time.LocalDate hireDate;

    private String lastName;

    public java.time.LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(java.time.LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public Integer getEmpNo() {
        return empNo;
    }

    public void setEmpNo(Integer empNo) {
        this.empNo = empNo;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public java.time.LocalDate getHireDate() {
        return hireDate;
    }

    public void setHireDate(java.time.LocalDate hireDate) {
        this.hireDate = hireDate;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "birthDate = " + birthDate + ", empNo = " + empNo + ", firstName = " + firstName + ", gender = " + gender + ", hireDate = " + hireDate + ", lastName = " + lastName;
    }

    /**
     * Subclass may override this method
     */
    @Override
    public Employees clone() throws CloneNotSupportedException {
        Employees employees = new Employees();
        employees.birthDate = this.birthDate;
        employees.empNo = this.empNo;
        employees.firstName = this.firstName;
        employees.gender = this.gender;
        employees.hireDate = this.hireDate;
        employees.lastName = this.lastName;
        return employees;
    }
}
