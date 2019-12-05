package com.sajjad.sqlite.SQLitePackage;

public class PersonModel {

    private int id, age;
   private String personName;
   private byte[] imageBytes;

    PersonModel(int id, String personName, int age, byte[] imageBytes) {
        this.id = id;
        this.age = age;
        this.personName = personName;
        this.imageBytes = imageBytes;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public String getPersonName() {
        return personName;
    }

    public byte[] getImageBytes() {
        return imageBytes;
    }
}
