package com.yaroslav.lobur.model.entity;


public class Doctor implements Entity {

  private long id;
  private Category category;
  private User user;
  private long numberOfPatients;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }


  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public long getNumberOfPatients() {
    return numberOfPatients;
  }

  public void setNumberOfPatients(long numberOfPatients) {
    this.numberOfPatients = numberOfPatients;
  }
}
