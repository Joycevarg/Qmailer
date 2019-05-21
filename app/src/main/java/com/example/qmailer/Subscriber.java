package com.example.qmailer;

public class Subscriber {

    private String email;
    private String name;
//    private String college;
    private int question;

    public Subscriber()
    {
        email="";
        name="";
//        college="";
    }

    public Subscriber(String email,String name,int q)
    {
        this.email=email;
        this.name=name;
//        this.college=college;
        this.question=q;
    }

    public String getEmail()
    {
        return email;

    }

    public String getName()
    {
        return name;

    }

//    public String getCollege()
//    {
//        return college;
//
//    }
    public int getQuestion()
    {
        return question;

    }

}
