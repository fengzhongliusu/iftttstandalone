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

import java.awt.BorderLayout;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.peer.LabelPeer;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author cs
 */
public class ModifyTaskFrame extends JFrame{
       private static subPanel3 p1;
       private static subPanel4 p2;
       private static JComboBox jcbo;
       private static int selectedIndex;
       private JButton ensure;
       private JButton reset;
       private JButton cancel;
       private String taskDescription;
       private String taskOutMsg;
       private String jcbo_list;
       
       public static void setSelect(int index){
           selectedIndex = index;
       }
       
       //新建任务时更新修改任务窗口的函数
       public static void setModify(String taskName,int Time_Mail,int Blog_Mail,String Date,String Time,String SpeMail,String SpePwd,
           String Blog_ID,String Blog_pwd,String content,String MyMail,String MyPwd,String toMail,int jbThis,int jbThat){
           if(Time_Mail==0){                                                    //定时
               p1.setTime(Date, Time,jbThis);
               p1.setvisible(jbThis);
           }else{                                                               //收到邮件
               p1.setMail(SpeMail,SpePwd,jbThis);
               p1.setvisible(jbThis);
           }
           if(Blog_Mail==0){                                                    //发微博
               p2.setBlog(Blog_ID, Blog_pwd, content,jbThat);
               p2.setvisible(jbThat);
           }else{                                                               //发邮件
               p2.setSendMail(MyMail, MyPwd, toMail, content,jbThat);
               p2.setvisible(jbThat);
           }
           jcbo.addItem(taskName);    //修改窗口任务列表下拉框增加一个任务
           jcbo.setSelectedItem(taskName);
       }
       
    public ModifyTaskFrame(){
        JPanel part1 = new JPanel(new FlowLayout(FlowLayout.LEADING));
        jcbo = new JComboBox();
        jcbo.setPreferredSize(new Dimension(100,25));
        JLabel label=new JLabel("需要修改的任务：");
        label.setFont(new Font("Courier",Font.BOLD,15));
        part1.add(label);
        part1.add(jcbo);
        part1.setPreferredSize(new Dimension(0,35));
        
        JPanel part2 = new JPanel(new GridLayout(1,2,20,5));
        p1 = new subPanel3();
        p2 = new subPanel4();
        part2.add(p1);
        part2.add(p2);
        
        JPanel part3 = new JPanel(new FlowLayout(FlowLayout.CENTER,100,8));
        ensure = new JButton("确定");
        reset = new JButton("重置");
        cancel = new JButton("取消");
        part3.add(ensure);
        part3.add(reset);
        part3.add(cancel);
        
        this.setLayout(new BorderLayout(10,10));
        this.add(part1,BorderLayout.NORTH);
        this.add(part2,BorderLayout.CENTER);
        this.add(part3,BorderLayout.SOUTH);
        
        ensure.addActionListener(new ActionListener() {
            //确定按钮的响应事件
            @Override
            public void actionPerformed(ActionEvent e) {
               int index = jcbo.getSelectedIndex();
                if (index >= 0) {
                    Task temp_task = (Task) MainProcess.task_list.get(index);
                    int TimeorMail;
                    int BlogorMail;
                    if (p1.get_jList() == 0) {
                        TimeorMail = 0;
                        taskDescription = "THIS:定时\n时间："
                                + p1.get_Date() + " " + p1.get_Time();
                    } else {
                        TimeorMail = 1;
                        taskDescription = "THIS:接收邮件\n邮箱：" + p1.get_SpeMail();
                    }
                    if (p2.get_jList() == 0) {
                        BlogorMail = 0;
                        taskDescription += "\nTHAT:发微博";
                    } else {
                        BlogorMail = 1;
                        taskDescription += "\nTHAT:发邮件\n发送方：" + p2.get_myMail() + "\n接收方: " + p2.getToMail();
                    }
                    taskOutMsg = "任务已被修改";
                    //修改该任务在任务列表的存储 
                    temp_task.modify_task(taskDescription, taskOutMsg, TimeorMail, BlogorMail, p1.get_Date(), p1.get_Time(), p1.get_SpeMail(),
                            p1.get_SpePwd(), p2.get_blog_ID(), p2.get_blog_pwd(), p2.get_con(), p2.get_myMail(), p2.get_mailPwd(), p2.getToMail());
                    if (MainFrame.getSeletedIndex() == index) {                          //如果主界面当前显示的是该条记录，在修改后更新显示
                        MainFrame.ModifyMsg(temp_task.getTaskDescrip(), temp_task.getTaskOutMsg());
                    }
                    closeWin();
                }
            }
        });
        
        reset.addActionListener(new ActionListener() {
        //重置按钮的响应事件
            @Override
            public void actionPerformed(ActionEvent e) {
               resetTask();
            }
        });
        
        cancel.addActionListener(new ActionListener() {
           //取消按钮的响应事件
            @Override
            public void actionPerformed(ActionEvent e) {
              closeWin();
            }
        });      
        
        jcbo.addItemListener(new ItemListener() {
            //组合框的响应事件
            @Override
            public void itemStateChanged(ItemEvent e) {
               selectedIndex = jcbo.getSelectedIndex();
                System.out.println("修改界面选择的是:"+selectedIndex);
               if(selectedIndex>=0){
               Task task_temp = (Task)MainProcess.task_list.get(selectedIndex);
               int TimeorMail = task_temp.getTime_Mail();
               int BlogorMail = task_temp.getBlog_Mail();
               //根据任务选择显示THIS和THAT
               p1.setvisible(TimeorMail);
               if(TimeorMail==0)    //显示时间
                    p1.setTime(task_temp.getDate(),task_temp.getTime() ,TimeorMail);
               else                //显示日期
                   p1.setMail(task_temp.getSpeMail(),task_temp.getSpePwd(),TimeorMail);
              
               p2.setvisible(BlogorMail);
               if(BlogorMail==0)   //显示发微博
                  p2.setBlog(task_temp.getBlog_ID(),task_temp.getBlog_pwd(),task_temp.getContent(),BlogorMail);
               else                //显示发邮件
                   p2.setSendMail(task_temp.getMyMail(),task_temp.getMyPwd(),task_temp.getToMail(),task_temp.getContent(),BlogorMail);
               }
            }
        });
    }
    
    //重置修改界面的输入窗口
    public void resetTask(){
        p1.resetThis();
        p2.resetThat();
    }
    
    //关闭修改窗口
     public void closeWin(){
        this.setVisible(false);
    }
     
    //删除一个任务时修改界面所做的修改
    public static void remUpdate(int index){
        jcbo.removeItemAt(index);
        if(selectedIndex<0){
            p1.resetThis();
            p2.resetThat();
        }
    }
}

/*This面板类*/
class subPanel3 extends JPanel{                                                
       private JPanel selectPanel;
       private JPanel p1_sub;
       private JPanel p1;
       private JTextField text_date;
       private JTextField text_time;
       private JTextField MailID;
       private JPasswordField MailPwd;
       private JComboBox jcbo;
       private JPanel blank;
       
       private int jcbo_list;
       private String Date;
       private String Time;
       private String Specified_Mail;                        //指定接收邮件的邮箱
       private String Specified_pwd;            

      
    public subPanel3(){
        String[] str = {"时间","邮件"};
        jcbo = new JComboBox(str);
        
       /*根据选择显示*/
       p1 = new JPanel(new GridLayout(2,2));
       p1.add(new JLabel("日期："));
       text_date = new JTextField();
       text_time = new JTextField();
        p1.add(text_date);
        p1.add(new JLabel("时间："));
        p1.add(text_time);
        
        p1_sub = new JPanel(new GridLayout(3,2));
        MailID = new JTextField();
        MailPwd = new JPasswordField();
        p1_sub.add(new JLabel("指定邮箱:"));
        p1_sub.add(MailID);
        p1_sub.add(new JLabel("密码:"));
        p1_sub.add(MailPwd);
        p1_sub.setVisible(false);
        
         selectPanel = new JPanel(new BorderLayout());
         selectPanel.add(p1,BorderLayout.NORTH);
         selectPanel.add(p1_sub,BorderLayout.CENTER);
        /**/
        
        JPanel lablePanel = new JPanel();
        JLabel label = new JLabel("IF THIS");
        label.setFont(new Font("Courier",Font.BOLD,22));
        lablePanel.add(label);
        //列表框选择this
        JPanel p2 = new JPanel(new GridLayout(2,1));
        p2.add(jcbo);
        
        JPanel p3 = new JPanel(new GridLayout(2,1));
        p3.add(lablePanel);
        p3.add(p2);
        
        blank = new JPanel();
        
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEtchedBorder());
        this.add(p3,BorderLayout.NORTH);
        this.add(selectPanel,BorderLayout.CENTER);     
        this.add(blank,BorderLayout.SOUTH);
        jcbo.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
               jcbo_list = jcbo.getSelectedIndex();
               if(jcbo_list==0){                                                //显示时间输入框
                   resetThis();
                   p1.setVisible(true);
                   p1_sub.setVisible(false);
                   blank.setPreferredSize(new Dimension(200,120));
               }else{                                                           //显示收邮件输入框
                   resetThis();
                   p1_sub.setVisible(true);
                   p1.setVisible(false);
                   blank.setPreferredSize(new Dimension(200,95));
               }
            }
        });
        
        text_date.getDocument().addDocumentListener(new DocumentListener() {
           //日期
            @Override
            public void insertUpdate(DocumentEvent e) {
                Date = text_date.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               Date = text_date.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               Date = text_date.getText();
            }
        });
        
        text_time.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
               Time = text_time.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               Time = text_time.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               Time = text_time.getText();
            } 
        });
        
        MailID.getDocument().addDocumentListener(new DocumentListener() {
            //邮箱
            @Override
            public void insertUpdate(DocumentEvent e) {
               Specified_Mail = MailID.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               Specified_Mail = MailID.getText(); 
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               Specified_Mail = MailID.getText();
            }
        });
        
        MailPwd.getDocument().addDocumentListener(new DocumentListener() {
            //邮箱密码
            @Override
            public void insertUpdate(DocumentEvent e) {
                Specified_pwd = MailPwd.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               Specified_pwd = MailPwd.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Specified_pwd = MailPwd.getText();
            }
        });
        
    }

    //清空This各框的信息
    public void resetThis(){
        this.text_date.setText("");
        this.text_time.setText("");
        this.MailID.setText("");
        this.MailPwd.setText("");
    }
    
    //设置时间显示
    public void setTime(String date,String time,int jList){
        this.text_date.setText(date);
        this.text_time.setText(time);
        this.jcbo.setSelectedIndex(jList);
        
    }
    
    //设置邮件显示
    public void setMail(String MailID,String MailPwd,int jList){
        this.MailID.setText(MailID);
        this.MailPwd.setText(MailPwd);
        this.jcbo.setSelectedIndex(jList);
    }
    
    //根据选择显示日期框或邮件框
    public void setvisible(int select){
       if(select==0){
           p1.setVisible(true);
           p1_sub.setVisible(false);
           blank.setPreferredSize(new Dimension(200,120));
       }
       else{
           p1.setVisible(false);
           p1_sub.setVisible(true);
           blank.setPreferredSize(new Dimension(200,95));
       }
    }
    
     public String get_Date(){
        return Date;
    }
     
    public String get_Time(){
        return Time;
    }
    
    public String get_SpeMail(){
        return Specified_Mail;
    }
    
    public String get_SpePwd(){
        return Specified_pwd;
    }
    
    public int get_jList(){
        return jcbo_list;
    }
}

/*That面板类*/
class subPanel4 extends JPanel{
        private  JScrollPane scrollPane;
        private JPanel p1;
        private JPanel p1_sub;
        private JComboBox jcbo;
        private JTextField blogID;      //微博ID
        private JPasswordField blogPwd; //微博密码
        private JTextField MyMail;
        private JPasswordField MyPwd;
        private JTextField toMail;
        private JTextArea content;
        
       private int jcbo_list;
       private String blog_ID;
       private String blog_Pwd;
       private String myMail;
       private String mailPwd;
       private String toMailID;
       private String contentStr;
    public subPanel4(){
        String[] str = {"发微博","发邮件"};
        jcbo = new JComboBox(str);
        JPanel p2 = new JPanel(new GridLayout(2,1));
        p2.add(jcbo);
        
        JPanel lablePanel = new JPanel();
        JLabel label = new JLabel("THEN THAT");
        label.setFont(new Font("Courier",Font.BOLD,22));
        lablePanel.add(label);
        
        //根据任务内容选择显示p1，p1_sub
        p1 = new JPanel(new GridLayout(2,2));
        p1.add(new JLabel("ID："));
        blogID= new JTextField();
        blogID.setEditable(true);
        blogPwd = new  JPasswordField();
        p1.add(blogID);
        p1.add(new JLabel("密码："));
        p1.add(blogPwd);
        p1.setVisible(true);
        content = new JTextArea(20,20);
        scrollPane = new JScrollPane(content);
        scrollPane.setPreferredSize(new Dimension(200,120));
        scrollPane.setBorder(new TitledBorder("微博内容："));
        
        p1_sub = new JPanel(new GridLayout(3,2));
        p1_sub.add(new JLabel("邮箱:"));
        MyMail = new JTextField();
        p1_sub.add(MyMail);
        p1_sub.add(new JLabel("密码:"));
        MyPwd = new JPasswordField();
        p1_sub.add(MyPwd);
        p1_sub.add(new JLabel("接收邮箱:"));
        toMail = new JTextField();
        p1_sub.add(toMail);
        p1_sub.setVisible(false);
        
        JPanel selectPanel  = new JPanel(new BorderLayout());
        selectPanel.add(p1,BorderLayout.NORTH);
        selectPanel.add(p1_sub,BorderLayout.CENTER);
        /**/
        
        JPanel p3 = new JPanel(new GridLayout(2,1));
        p3.add(lablePanel);
        p3.add(p2);
        
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEtchedBorder());
        add(p3,BorderLayout.NORTH);
        add(selectPanel,BorderLayout.CENTER);
        add(scrollPane,BorderLayout.SOUTH);
        
        jcbo.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
               jcbo_list = jcbo.getSelectedIndex();
               if(jcbo_list==0){                                                //显示发微博输入框
                   resetThat();
                   p1.setVisible(true);
                   p1_sub.setVisible(false);
                   scrollPane.setBorder(new TitledBorder("微博内容："));
                   scrollPane.setPreferredSize(new Dimension(200,120));
                   content.setText("");
               }else{                                                           //显示发邮件输入框
                   resetThat();
                   p1.setVisible(false);
                   p1_sub.setVisible(true);
                   scrollPane.setBorder(new TitledBorder("邮件内容："));
                   scrollPane.setPreferredSize(new Dimension(200,95));
                   content.setText("");
               }
            }
        });
        
        blogID.getDocument().addDocumentListener(new DocumentListener() {
            //博客邮箱
            @Override
            public void insertUpdate(DocumentEvent e) {
                blog_ID = blogID.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               blog_ID = blogID.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                blog_ID = blogID.getText();
            }
        });
        
        blogPwd.getDocument().addDocumentListener(new DocumentListener() {
            //博客密码
            @Override
            public void insertUpdate(DocumentEvent e) {
               blog_Pwd = blogPwd.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               blog_Pwd = blogPwd.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               blog_Pwd = blogPwd.getText();
            }
        });
        
        MyMail.getDocument().addDocumentListener(new DocumentListener() {
            //邮箱ID
            @Override
            public void insertUpdate(DocumentEvent e) {
               myMail = MyMail.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               myMail = MyMail.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               myMail = MyMail.getText();
            }
        });
        
        MyPwd.getDocument().addDocumentListener(new DocumentListener() {
            //邮箱密码
            @Override
            public void insertUpdate(DocumentEvent e) {
               mailPwd = MyPwd.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               mailPwd = MyPwd.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
               mailPwd = MyPwd.getText();
            }
        });
        
        toMail.getDocument().addDocumentListener(new DocumentListener() {
             //接收方邮箱
            @Override
            public void insertUpdate(DocumentEvent e) {
                toMailID = toMail.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                toMailID = toMail.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                  toMailID = toMail.getText();
            }
        });
        
        content.getDocument().addDocumentListener(new DocumentListener() {
            //微博邮件内容
            @Override
            public void insertUpdate(DocumentEvent e) {
               contentStr = content.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
               contentStr = content.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
              contentStr = content.getText();
            }
        });
    }
    
    public String get_blog_ID(){
         return blog_ID;
     }
    
     public String get_blog_pwd(){
         return blog_Pwd;
     }
     
     public String get_con(){
         return contentStr;
     }
     
     public String get_myMail(){
         return myMail;
     }
     
     public String get_mailPwd(){
         return mailPwd;
     }
     
     public String getToMail(){
         return toMailID;
     }
     
    public int get_jList(){
        return jcbo_list;
    }
    
    //设置微博信息
    public void setBlog(String blogID,String blogPwd,String content,int jList){
        this.blogID.setText(blogID);
        this.blogPwd.setText(blogPwd);
        this.content.setText(content);
        this.jcbo.setSelectedIndex(jList);
    } 
    
    //设置邮件信息
    public void setSendMail(String myMail,String myPwd,String toMail,String content,int jList){
        this.MyMail.setText(myMail);
        this.MyPwd.setText(myPwd);
        this.toMail.setText(toMail);
        this.content.setText(content);
        this.jcbo.setSelectedIndex(jList);
    }
    
    //清空That各框
     public void resetThat(){
        this.blogID.setText("18252063039");
        this.blogPwd.setText("cs12345");
        this.content.setText("");
        this.MyMail.setText("chengshuo357951@gmail.com");
        this.MyPwd.setText("chengshuo357951");
        this.toMail.setText("1324480595@qq.com");
        this.content.setText("");
    }
     
     //根据选择显示微博框或邮件框
    public void setvisible(int select){
       if(select==0){
           p1.setVisible(true);
           p1_sub.setVisible(false);
           scrollPane.setPreferredSize(new Dimension(200,120));
       }
       else{
           p1.setVisible(false);
           p1_sub.setVisible(true);
           scrollPane.setPreferredSize(new Dimension(200,95));
       }
    }
}
