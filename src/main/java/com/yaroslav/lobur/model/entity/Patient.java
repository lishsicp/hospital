package com.yaroslav.lobur.model.entity;

import com.yaroslav.lobur.model.entity.enums.PatientStatus;

import java.util.Date;

public class Patient implements Entity {

  private long id;
  private PatientStatus status;
  private Doctor doctor;
  private String firstname;
  private String lastname;
  private Date dateOfBirth;
  private String gender;
  private String email;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public PatientStatus getStatus() {
    return status;
  }

  public void setStatus(PatientStatus status) {
    this.status = status;
  }

  public Doctor getDoctor() {
    return doctor;
  }

  public void setDoctor(Doctor doctor) {
    this.doctor = doctor;
  }

  public String getFirstname() {
    return firstname;
  }

  public void setFirstname(String firstname) {
    this.firstname = firstname;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public Date getDateOfBirth() {
    return dateOfBirth;
  }

  public void setDateOfBirth(Date dateOfBirth) {
    this.dateOfBirth = dateOfBirth;
  }

  public String getGender() {
    return gender;
  }

  public void setGender(String gender) {
    this.gender = gender;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return "Patient{" +
            "id=" + id +
            ", status=" + status +
            ", doctor=" + doctor +
            ", firstname='" + firstname + '\'' +
            ", lastname='" + lastname + '\'' +
            ", dateOfBirth=" + dateOfBirth +
            ", gender='" + gender + '\'' +
            ", email='" + email + '\'' +
            '}';
  }
}
