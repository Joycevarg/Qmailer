package com.example.qmailer;

public class question {

    private String Title;
    private String Link;
    private int Question;

    public question(){

    }
    public question(String t,String l,int q)
    {
        Title=t;
        Link=l;
        Question=q;
    }

    public String getTitle()
    {
        return Title;
    }

    public String getLink()
    {
        return Link;
    }
    public int getQuestion()
    {
        return Question;
    }
}
