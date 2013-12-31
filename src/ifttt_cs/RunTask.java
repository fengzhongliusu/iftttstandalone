/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifttt_cs;

/**
 *
 * @author cs
 */
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import weibo4j.Timeline;
import weibo4j.Weibo;
import weibo4j.http.AccessToken;
import weibo4j.model.Status;
import weibo4j.model.WeiboException;
import weibo4j.util.BareBonesBrowserLaunch;

/*
 * 多线程运行类别
 * referance:http://blog.csdn.net/zapldy/article/details/3971579
 *           http://houfeng0923.iteye.com/blog/1014475
 */
public class RunTask extends Thread{ 
    private String SpeMail;
    private String SpePwd;
    private String date;
    private String time;
    private String BlogID;
    private String BlogPwd;
    private String myMail;
    private String myPwd;
    private String toMail;
    private String content;
    private String setedTime;
    private String TaskDescrip;
    private String taskOutMsg;
    private int TimeorMail;
    private int BlogorMail;
    private boolean completedSign;                                              //完成标志
    private boolean stopSign;                                                   //停止标志
    private boolean endSign;                                                    //结束标志
    private boolean runSign;                                                    //是否已调用run函数
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");   //设置日期格式

    public String getOutMsg(){
       return taskOutMsg;
    }
    
    public boolean getComplete() {
        return completedSign;
    }
    
    public boolean getStop(){
        return stopSign;
    }
    
    public boolean getEnd(){
        return endSign;
    }
    
    public boolean getRun(){
        return runSign;
    }
    
    public void setStop(boolean stop){
        this.stopSign = stop;
    }
    
    public void setEnd(boolean end){
        this.stopSign = end;
        this.endSign = end;
    }
    
    public RunTask(String taskDescrip, String out_msg, int Time_Mail, int Blog_Mail, String Date, String Time, String SpeMail, String SpePwd,
            String Blog_ID, String Blog_pwd, String content, String MyMail, String MyPwd, String toMail) {
        this.completedSign = false;                                             //任务完成标志
        this.stopSign = true;                                                   
        this.endSign = true;
        this.runSign = false;
        this.TaskDescrip = taskDescrip;
        this.taskOutMsg = out_msg;
        this.TimeorMail = Time_Mail;
        this.BlogorMail = Blog_Mail;
        this.date = Date;
        this.time = Time;
        this.SpeMail = SpeMail;
        this.SpePwd = SpePwd;
        this.BlogID = Blog_ID;
        this.BlogPwd = Blog_pwd;
        this.content = content;
        this.myMail = MyMail;
        this.myPwd = MyPwd;
        this.toMail = toMail;
    }

    //修改任务时所用修改runTask信息的函数
    public void mdfyRunMsg(String taskDescrip, String out_msg, int Time_Mail, int Blog_Mail, String Date, String Time, String SpeMail, String SpePwd,
            String Blog_ID, String Blog_pwd, String content, String MyMail, String MyPwd, String toMail){
        this.TaskDescrip = taskDescrip;
        this.taskOutMsg = out_msg;
        this.TimeorMail = Time_Mail;
        this.BlogorMail = Blog_Mail;
        this.date = Date;
        this.time = Time;
        this.setedTime = Date+" "+Time;
        this.SpeMail = SpeMail;
        this.SpePwd = SpePwd;
        this.BlogID = Blog_ID;
        this.BlogPwd = Blog_pwd;
        this.content = content;
        this.myMail = MyMail;
        this.myPwd = MyPwd;
        this.toMail = toMail;
    }
    @Override
    public void run() {
        runSign = true;
        if (TimeorMail == 0 && BlogorMail == 1) {
            setedTime = date + " " + time;                                           //设定的时间
            /**
             * 定时发邮件*
             */
            while (endSign) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                }
                while (stopSign){ 
                    try {
                        Thread.sleep(1000);                                           //一秒钟循环一次
                        System.out.println(df.format(new Date()).toString());
                        System.out.println(TaskDescrip);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    if (setedTime.equals(df.format(new Date()).toString())) {   //当前时间与定时所设的时间相同
                        try {
                           MainFrame.appendMsg("正在发邮件...");
                            sendmail(myMail, myPwd, toMail, content); 
                            completedSign = true;
                        } catch (Exception ex) {
                            Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                    if (completedSign) {
                        MainFrame.appendMsg("邮件发送成功...");
                        taskOutMsg = "改任务已经完成...";
                        break;
                    }
                }
                if(completedSign){
                    endSign=false;               
                    break;        
                }
            }
            /**
             * *定时发邮件
             */

        } else if (TimeorMail == 0 && BlogorMail == 0) {
            /**
             * 定时发微博*
             */
            setedTime = date + " " + time;                                          //设定的时间
            while (endSign) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                }
                    while (stopSign) {                        
                        try {
                            Thread.sleep(1000);                                           //一秒钟循环一次
                            System.out.println(df.format(new Date()).toString());
                            System.out.println(TaskDescrip);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        if (setedTime.equals(df.format(new Date()).toString())) {//当前时间与定时所设的时间相同
                            try {
                                MainFrame.appendMsg("正在发微博...");
                                sendWeibo(content);
                                completedSign = true;
                            } catch (Exception ex) {
                                Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                        if (completedSign) {
                            MainFrame.appendMsg("微博发送成功...");
                            taskOutMsg = "改任务已经完成...";
                            break;
                        }
                    }
                if(completedSign){
                    endSign=false;
                    break;        
                }
            }

        } else if (TimeorMail == 1 && BlogorMail == 0) {
                /**
                 * 收邮件发微博*
                 */
                int now_count = 0;                     //记录每次执行recvmsg（）的个数
                int count=0;                               
                try {
                    count = recvmsg(SpeMail, SpePwd); //获取开始任务时邮件的个数
                } catch (MessagingException ex) {
                    Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                while (endSign) {
                    
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    while (stopSign) {                       
                        try {
                            Thread.sleep(60000);
                            System.out.println(TaskDescrip);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        try {
                            now_count = recvmsg(SpeMail, SpePwd);
                            System.out.println("现在邮件个数是："+now_count);
                            if (now_count>count) {              
                                try {
                                    MainFrame.appendMsg("正在发微博...");
                                    sendWeibo(content);
                                } catch (Exception ex) {
                                    Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                count = now_count;
                                MainFrame.appendMsg("微博发送成功...");
                            }
                        } catch (MessagingException ex) {
                            Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }                    
                    System.out.println(TaskDescrip+"该任务已暂停...");
                }
                System.out.println(TaskDescrip+"该任务已结束...");
                /**
                 * 收邮件发微博*
                 */
        } else {
            /**
             * 收邮件发邮件*
             */
            int now_count=0;                         //存储每次执行recvmsg()时获取的邮件个数
            int count=0;                               
                try {
                    count = recvmsg(SpeMail, SpePwd); //获取开始任务时邮件的个数
                } catch (MessagingException ex) {
                    Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                }
            while (endSign) {
                
                try {
                    Thread.sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                while (stopSign) {
                    try {
                        Thread.sleep(60000);
                        System.out.println(TaskDescrip);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    try {
                        now_count = recvmsg(SpeMail, SpePwd);
                        if (now_count>count) {              //现在邮件个数大于上一次检查时的个数
                            try {
                                MainFrame.appendMsg("正在发邮件...");
                                sendmail(myMail, myPwd, toMail, content);
                            } catch (Exception ex) {
                                Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                            }
                           count = now_count;
                           MainFrame.appendMsg("邮件发送成功...");
                        }
                    } catch (MessagingException ex) {
                        Logger.getLogger(RunTask.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }                    
                System.out.println(TaskDescrip+"该任务已暂停...");
            }
            System.out.println(TaskDescrip+"该任务已结束...");
            /*收邮件发邮件*/
        }

    }

    public void sendmail(String your_mail, String your_pwd, String dst_mail, String msg) throws Exception {
        String host = "smtp.gmail.com";
        String port = "465";
        Properties props = new Properties();
        props.setProperty("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.smtp.ssl.enable", "true");
        props.setProperty("mail.smtp.connectiontimeout", "5000");

        final String user = your_mail;
        final String pwd = your_pwd;
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pwd);
            }
        });
        session.setDebug(true);
        Transport transport = session.getTransport("smtps");
        transport.connect(host, user, pwd);
        //消息  
        MimeMessage message = new MimeMessage(session);
        setMailContent(message, msg);                                            //设置邮件内容
        message.setSubject("Mail From IFTTT");                                   //设置邮件主题 
        //消息发送者接收者设置  
        message.setFrom(new InternetAddress(user, "cs"));
        message.addRecipients(Message.RecipientType.TO, new InternetAddress[]{
            new InternetAddress(dst_mail)
        });
        message.saveChanges();
        //发送  
        Transport.send(message);
        transport.close();

    }

    //设定邮件内容  
    public void setMailContent(MimeMessage message, String msg) throws MessagingException {
        Multipart part = new MimeMultipart();
        BodyPart bodypart = new MimeBodyPart();
        bodypart.setText(msg);
        part.addBodyPart(bodypart);
        message.setContent(part);
    }

    //判断指定的邮箱里是否有在制定的时间之后接收到的邮件
    public int recvmsg(String username, String pwd) throws NoSuchProviderException, MessagingException {
        String host = "imap.gmail.com";

        Properties props = System.getProperties();
        props.setProperty("mail.store.protocol", "imaps");

        //get session
        Session session = Session.getDefaultInstance(props, null);

        Store store = session.getStore("imaps");
        store.connect(host, username, pwd);

        Folder folder = store.getFolder("Inbox");
        folder.open(Folder.READ_ONLY);

        Message message[] = folder.getMessages();
        folder.close(false);
        store.close();
        return message.length;
    }

    public void sendWeibo(String msg) {
          String access_token = "2.00pxfNdC0naRhz232bb5abb7mgtMOB";
          String statuses = msg;
	  Timeline tm = new Timeline();
	  tm.client.setToken(access_token);
	  try {
		Status status = tm.UpdateStatus(statuses);
	       } catch (WeiboException e) {
		  e.printStackTrace();
		}	
           }
}
