package com.example.cnlive.jsonobject;

/**
 * Created by CNLive on 2016/3/11.
 */
public class Person  {
    private String id;
    private String name;
    private String version;

    public Person(String id, String name, String version) {
        this.id = id;
        this.name = name;
        this.version = version;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
