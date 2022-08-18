package com.yaroslav.lobur.model.entity;


import com.yaroslav.lobur.model.entity.enums.AppointmentStatus;
import com.yaroslav.lobur.model.entity.enums.AppointmentType;

import java.util.Date;

public class Appointment implements Entity {

  private long id;
  private Date date;
  private String title;
  private AppointmentStatus status;
  private AppointmentType type;
  private User user;
  private HospitalCard hospitalCard;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public HospitalCard getHospitalCard() {
    return hospitalCard;
  }

  public void setHospitalCard(HospitalCard hospitalCard) {
    this.hospitalCard = hospitalCard;
  }

  public AppointmentStatus getStatus() {
    return status;
  }

  public void setStatus(AppointmentStatus status) {
    this.status = status;
  }

  public AppointmentType getType() {
    return type;
  }

  public void setType(AppointmentType type) {
    this.type = type;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }
}
