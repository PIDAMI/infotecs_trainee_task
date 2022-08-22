package domain;

import json.JSON;

public class Student {
    private final Long id;
    private final String name;

    public Student(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Student)) {
            return false;
        }
        Student student = (Student) o;
        return student.toString().equals(this.toString());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static Student fromJSON(JSON json) {
        return new Student(json.cast("id", Long.class),
            json.cast("name", String.class));
    }

    public JSON toJSON() {
        return new JSON().set("id", id).set("name", name);
    }

    public String toString() {
        return "{\"id\" : " + id + ", \"name\" : " + name + "}";
    }
}
