package com.yaroslav.lobur.model.entity;


public class Category implements Entity {

  private long id;
  private String name;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String category) {
    this.name = category;
  }

}
