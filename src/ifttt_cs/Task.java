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
public class Task {

    //private RunTask mytask;                                                    
    private RunTask myThread;                                                    //创建一个线程
    private boolean started;

    private String taskName;
    private String taskDescrip;
    private int Time_Mail;
    private int Blog_Mail;
    private String out_msg;
    /*THIS msg*/
    private String Time;
    private String Date;
    private String SpeMail;
    private String SpePwd;

    /*THAT msg*/
    private String Blog_ID;
    private String Blog_pwd;
    private String content;
    private String MyMail;
    private String MyPwd;
    private String toMail;

    public Task() {
        taskDescrip = "";
        taskName = "";
        Time = "";
        Date = "";
        SpeMail = "";
        SpePwd = "";
        Blog_ID = "";
        Blog_pwd = "";
        MyMail = "";
        MyPwd = "";
        toMail = "";
        content = "";
        out_msg = "";
    }

    //判断该任务是否已经开始
    public boolean isStart() {
        return started;
    }
    
    //判断该线程是否已经结束
    public boolean isComplete() {
        return myThread.getComplete();
    }

    public void setOutMsgTimely(){
        setTaskOutMsg(myThread.getOutMsg());
    }
    //开始该线程
    public void startTask() {
        started = true;
        if(!myThread.getRun()){                                      //之前没有进行.start()操作
          myThread.start();
        }
        else if(myThread.getEnd()){                                  //没有跳出外层循环
            myThread.setStop(true);
        }
        else{
        }
    }

    //暂停该线程
    public void stopTask() {
        if(started){                                            //任务已经开始
            myThread.setStop(false);
        }
        started = false;                                        //开始标志设置为false
    }
    
    //结束该线程
    public void endTask(){
        if(started || myThread.getRun()){             //正在运行的或者暂停的都可以结束
            myThread.setEnd(false);
        }
        started = false;
    }

    //任务的构造函数
    public Task(String taskName, String taskDescrip, String out_msg, int Time_Mail, int Blog_Mail, String Date, String Time, String SpeMail, String SpePwd,
            String Blog_ID, String Blog_pwd, String content, String MyMail, String MyPwd, String toMail) {
        this.myThread = new RunTask(taskDescrip, out_msg, Time_Mail, Blog_Mail, Date, Time, SpeMail, SpePwd, Blog_ID, Blog_pwd, content, MyMail, MyPwd, toMail);
        this.started = false;
        this.taskDescrip = taskDescrip;
        this.out_msg = out_msg;
        this.taskName = taskName;
        this.Time_Mail = Time_Mail;
        this.Blog_Mail = Blog_Mail;
        this.Date = Date;
        this.Time = Time;
        this.SpeMail = SpeMail;
        this.SpePwd = SpePwd;
        this.Blog_ID = Blog_ID;
        this.Blog_pwd = Blog_pwd;
        this.MyMail = MyMail;
        this.MyPwd = MyPwd;
        this.toMail = toMail;
        this.content = content;
    }

    //修改任务时更改任务信息
    public void modify_task(String taskDescrip, String out_msg, int Time_Mail, int Blog_Mail, String Date, String Time, String SpeMail, String SpePwd,
            String Blog_ID, String Blog_pwd, String content, String MyMail, String MyPwd, String toMail) {        
        if(started==true){                                                      //如果任务开始了则结束之，然后新建任务线程再开始
           endTask();
           myThread = new RunTask(taskDescrip, out_msg, Time_Mail, Blog_Mail, Date, Time, SpeMail, SpePwd, Blog_ID, Blog_pwd, content, MyMail, MyPwd, toMail);
           startTask();
        }
        else{
            this.myThread.mdfyRunMsg(taskDescrip, out_msg, Time_Mail, Blog_Mail, Date, Time, SpeMail, SpePwd, Blog_ID, Blog_pwd, content, MyMail, MyPwd, toMail);        
        }
        this.taskDescrip = taskDescrip;
        this.out_msg = out_msg;
        this.Time_Mail = Time_Mail;
        this.Blog_Mail = Blog_Mail;
        this.Date = Date;
        this.Time = Time;
        this.SpeMail = SpeMail;
        this.SpePwd = SpePwd;
        this.Blog_ID = Blog_ID;
        this.Blog_pwd = Blog_pwd;
        this.MyMail = MyMail;
        this.MyPwd = MyPwd;
        this.toMail = toMail;
        this.content = content;
    }

    public String gettaskName() {
        return taskName;
    }

    public String getTaskDescrip() {
        return taskDescrip;
    }

    public String getTaskOutMsg() {
        return out_msg;
    }

    public int getTime_Mail() {
        return Time_Mail;
    }

    public int getBlog_Mail() {
        return Blog_Mail;
    }

    public String getDate() {
        return Date;
    }

    public String getTime() {
        return Time;
    }

    public String getSpeMail() {
        return SpeMail;
    }

    public String getSpePwd() {
        return SpePwd;
    }

    public String getBlog_ID() {
        return Blog_ID;
    }

    public String getBlog_pwd() {
        return Blog_pwd;
    }

    public String getMyMail() {
        return MyMail;
    }

    public String getMyPwd() {
        return MyPwd;
    }

    public String getToMail() {
        return toMail;
    }

    public String getContent() {
        return content;
    }

    public void setTaskDescrip(String descrip) {
        taskDescrip = descrip;
    }

    public void setTaskOutMsg(String outMsg) {
        out_msg = outMsg;
    }

    public boolean getEndSign(){
        return myThread.getEnd();
    }
}
