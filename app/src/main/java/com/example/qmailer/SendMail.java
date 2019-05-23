package com.example.qmailer;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


public class SendMail extends AsyncTask<Void,Void,Void> {


    private Context context;
    private Session session;
    public static int max,count;

    private String email;
    private String subject;
    private String message;
    private boolean last;
    NotificationHelper nh=new NotificationHelper();



    public SendMail(Context context, String email, String subject, String message,boolean last){

        this.context = context;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.last=last;
    }

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//            count=0;
//        progressDialog = ProgressDialog.show(context,"Sending message","Please wait...",false,false);
//    }

    public void sendCompEmail()
    {
        String email = Config.ADMIN_EMAIL;
        String subject = "Qmailer";
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("yyyy / MM / dd ");
        String message = "Hi Admin,<br> Today's mails have been sent out.<br>DATED : "+ mdformat.format(calendar.getTime());
        Log.d("Hey",message);

        SendMail sm = new SendMail(context, email, subject, message,true);

        sm.execute();
    }

    public void afterMails()
    {
        sendCompEmail();
        nh.sendedNotification(1002);
        Log.d("Successfully", "Emails sent");
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);

//        progressDialog.dismiss();
//        NotificationHelper nh=new NotificationHelper();
        if(last==false) {

            if ((++count) < max)
                nh.sendingNotification(1002, count, max);
            else
                afterMails();
        }

//        Toast.makeText(context,"Message Sent",Toast.LENGTH_LONG).show();
    }

    @Override
    protected Void doInBackground(Void... params) {

        Properties props = new Properties();


        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");


        session = Session.getDefaultInstance(props,
                new javax.mail.Authenticator() {

                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Config.EMAIL, Config.PASSWORD);
                    }
                });

        try {

            MimeMessage mm = new MimeMessage(session);


            mm.setFrom(new InternetAddress(Config.EMAIL));

            mm.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            mm.setSubject(subject);

            mm.setText(message,"UTF-8", "html");


            Transport.send(mm);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
        return null;
    }
}