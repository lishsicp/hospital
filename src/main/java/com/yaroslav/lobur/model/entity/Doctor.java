package com.yaroslav.lobur.model.entity;


import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Doctor doctor = (Doctor) o;
    return id == doctor.id && numberOfPatients == doctor.numberOfPatients && Objects.equals(category, doctor.category) && Objects.equals(user, doctor.user);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, category, user, numberOfPatients);
  }

  @Override
  public String toString() {
    return "Doctor{" +
            "id=" + id +
            ", category=" + category +
            ", user=" + user +
            ", numberOfPatients=" + numberOfPatients +
            '}';
  }
}
