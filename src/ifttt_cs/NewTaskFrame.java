/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifttt_cs;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.peer.LabelPeer;
import java.beans.PropertyChangeListener;
import java.util.regex.Pattern;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDesktopPane;
import javax.swing.JFormattedTextField;
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
import sun.io.ByteToCharCp1025;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author cs
 */
public class NewTaskFrame extends JFrame {

    private JTextField task_input;
    private String task_name;
    private int TimeorMail;
    private int BlogorMail;
    private String taskDesciption;
    private String taskOutMsg;
    private JButton ensure;
    private JButton reset;
    private JButton cancel;
    private subPanel1 p1;
    private subPanel2 p2;

    public NewTaskFrame() {

        JPanel part1 = new JPanel();
        task_input = new JTextField();
        task_input.setColumns(20);
        JLabel task_label = new JLabel("任务名称：", 2);
        task_label.setFont(new Font("Courier", Font.BOLD, 15));
        part1.add(task_label);
        part1.add(task_input);
        part1.setPreferredSize(new Dimension(0, 35));

        JPanel part2 = new JPanel(new GridLayout(1, 2, 20, 5));
        p1 = new subPanel1();                                                   //IF THIS子面板
        p2 = new subPanel2();                                                   //THEN THAT子面板
        part2.add(p1);
        part2.add(p2);

        JPanel part3 = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 8));
        ensure = new JButton("确定");
        reset = new JButton("重置");
        cancel = new JButton("取消");
        part3.add(ensure);
        part3.add(reset);
        part3.add(cancel);

        this.setLayout(new BorderLayout(10, 10));
        this.add(part1, BorderLayout.NORTH);
        this.add(part2, BorderLayout.CENTER);
        this.add(part3, BorderLayout.SOUTH);

        task_input.getDocument().addDocumentListener(new DocumentListener() {
            //任务名称
            @Override
            public void insertUpdate(DocumentEvent e) {
                task_name = task_input.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                task_name = task_input.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                task_name = task_input.getText();
            }
        });

        reset.addActionListener(new ActionListener() {
            //重置按钮
            @Override
            public void actionPerformed(ActionEvent e) {
                resetMsg();
            }
        });

        cancel.addActionListener(new ActionListener() {
            //取消按钮
            @Override
            public void actionPerformed(ActionEvent e) {
                resetMsg();
                closeWin();
            }
        });

        ensure.addActionListener(new ActionListener() {
            //确定按钮
            @Override
            public void actionPerformed(ActionEvent e) {
                if (p1.get_jList().equals("时钟")) {
                    TimeorMail = 0;
                    taskDesciption = "THIS:定时\n时间："
                            + p1.get_Date() + " " + p1.get_Time();
                } else {
                    TimeorMail = 1;
                    taskDesciption = "THIS:接收邮件\n邮箱：" + p1.get_SpeMail();
                }
                if (p2.getjList().equals("发微博")) {
                    BlogorMail = 0;
                    taskDesciption += "\nTHAT:发微博\n";
                } else {
                    BlogorMail = 1;
                    taskDesciption += "\nTHAT:发邮件\n发送方: " + p2.get_myMail() + "\n接收方: " + p2.getToMail();
                }

                if (TimeorMail != 1) {
                    taskOutMsg = "新建的任务";
                    Task task = new Task(task_name, taskDesciption, taskOutMsg, TimeorMail, BlogorMail, p1.get_Date(), p1.get_Time(), p1.get_SpeMail(), p1.get_SpePwd(),
                            p2.get_blog_ID(), p2.get_blog_pwd(), p2.get_con(), p2.get_myMail(), p2.get_mailPwd(), p2.getToMail());
                    MainProcess.task_list.add(task);   //把新建的任务加入任务列表
                    //新建任务后在主界面更新显示
                    MainFrame.setMsg(task_name, taskDesciption, taskOutMsg);
                    //同时也要更新修改页面的显示信息(因为这些界面显示的是最近新建的任务)
                    ModifyTaskFrame.setModify(task_name, TimeorMail, BlogorMail, p1.get_Date(), p1.get_Time(), p1.get_SpeMail(),
                            p1.get_SpePwd(), p2.get_blog_ID(), p2.get_blog_pwd(), p2.get_con(), p2.get_myMail(), p2.get_mailPwd(), p2.getToMail(), TimeorMail, BlogorMail);
                    //删除查看框内增加一个任务
                    DeltTaskFrame.addJList(task_name);
                    resetMsg();                                                     //重置输入框
                    closeWin();
                } else {
                    //判断指定邮箱的格式是否正确
                    boolean mailTrue;
                    if (p1.get_SpeMail() == null) {
                        mailTrue = false;
                    } else {
                        mailTrue = Pattern.compile("\\w+@gmail\\.\\w+").matcher(p1.get_SpeMail()).matches();
                    }

                    if (mailTrue) {
                        taskOutMsg = "新建的任务";
                        Task task = new Task(task_name, taskDesciption, taskOutMsg, TimeorMail, BlogorMail, p1.get_Date(), p1.get_Time(), p1.get_SpeMail(), p1.get_SpePwd(),
                                p2.get_blog_ID(), p2.get_blog_pwd(), p2.get_con(), p2.get_myMail(), p2.get_mailPwd(), p2.getToMail());
                        MainProcess.task_list.add(task);   //把新建的任务加入任务列表
                        //新建任务后在主界面更新显示
                        MainFrame.setMsg(task_name, taskDesciption, taskOutMsg);
                        //同时也要更新修改页面的显示信息(因为这些界面显示的是最近新建的任务)
                        ModifyTaskFrame.setModify(task_name, TimeorMail, BlogorMail, p1.get_Date(), p1.get_Time(), p1.get_SpeMail(),
                                p1.get_SpePwd(), p2.get_blog_ID(), p2.get_blog_pwd(), p2.get_con(), p2.get_myMail(), p2.get_mailPwd(), p2.getToMail(), TimeorMail, BlogorMail);
                        //删除查看框内增加一个任务
                        DeltTaskFrame.addJList(task_name);
                        resetMsg();                                                     //重置输入框
                        closeWin();
                    } else {
                        MainFrame.setTip();
                    }
                }
            }
        });

    }

    //重置新建信息
    public void resetMsg() {
        task_input.setText("");
        p1.resetThis();
        p2.resetThat();
    }

    //"取消"关闭窗口
    public void closeWin() {
        this.setVisible(false);
    }
}

class subPanel1 extends JPanel {

    private final String[] trig = {"时钟", "邮件"};
    ;
        private final JComboBox jcbo;
    private JTextField textDate;
    private JTextField textTime;
    private JTextField MailID;
    private JPasswordField MailPwd;

    private JPanel p1_sub;
    private JPanel p1;
    private int Time_Mail;
    private JPanel blank;
    private JLabel tipLabel0;
    private JLabel tipLabel1;
    private JLabel tipLabel2;

    private String jcbo_list;
    private String Date;
    private String Time;
    private String Specified_Mail;                        //指定接收邮件的邮箱
    private String Specified_pwd;

    public subPanel1() {
        jcbo = new JComboBox(trig);
        jcbo_list = "时钟";
        /*THIS信息填写栏，根据组合框的选择来选择显示p1还是p1_sub*/
        p1 = new JPanel(new GridLayout(2, 2));
        p1.add(new JLabel("日期："));
        textDate = new JTextField();
        textTime = new JTextField();
        p1.add(textDate);
        p1.add(new JLabel("时间："));
        p1.add(textTime);
        p1.setVisible(true);

        p1_sub = new JPanel(new GridLayout(3, 2));
        MailID = new JTextField();
        MailPwd = new JPasswordField();
        p1_sub.add(new JLabel("指定邮箱:"));
        p1_sub.add(MailID);
        p1_sub.add(new JLabel("密码:"));
        p1_sub.add(MailPwd);
        p1_sub.setVisible(false);

        JPanel selectPanel = new JPanel(new BorderLayout());
        selectPanel.add(p1, BorderLayout.NORTH);
        selectPanel.add(p1_sub, BorderLayout.CENTER);
        /*THIS信息填写栏*/

        JPanel lablePanel = new JPanel();
        JLabel label = new JLabel("IF THIS");
        label.setFont(new Font("Courier", Font.BOLD, 22));
        lablePanel.add(label);
        //列表框选择this
        JPanel p2 = new JPanel(new GridLayout(2, 1));
        p2.add(jcbo);

        JPanel p3 = new JPanel(new GridLayout(2, 1));
        p3.add(lablePanel);
        p3.add(p2);

        blank = new JPanel(new GridLayout(6, 1));                              //为了对齐加入的空白面板
        tipLabel0 = new JLabel("Tips:");
        tipLabel1 = new JLabel("日期格式为2000-11-12");                        //日期输入提示
        tipLabel2 = new JLabel("时间格式为12:00:00");
        blank.add(tipLabel0);
        blank.add(tipLabel1);
        blank.add(tipLabel2);

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEtchedBorder());
        this.add(p3, BorderLayout.NORTH);
        this.add(selectPanel, BorderLayout.CENTER);
        this.add(blank, BorderLayout.SOUTH);

        jcbo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Time_Mail = jcbo.getSelectedIndex();
                if (Time_Mail == 0) {                                           //显示时间输入框
                    jcbo_list = "时钟";
                    p1.setVisible(true);
                    p1_sub.setVisible(false);
                    tipLabel0.setVisible(true);
                    tipLabel1.setVisible(true);
                    tipLabel2.setVisible(true);
                    blank.setPreferredSize(new Dimension(200, 120));
                } else {                                                        //显示邮件输入框
                    jcbo_list = "邮件";
                    p1_sub.setVisible(true);
                    p1.setVisible(false);
                    tipLabel0.setVisible(false);
                    tipLabel1.setVisible(false);
                    tipLabel2.setVisible(false);
                    blank.setPreferredSize(new Dimension(200, 95));
                }
            }
        });

        textDate.getDocument().addDocumentListener(new DocumentListener() {
            //日期
            @Override
            public void insertUpdate(DocumentEvent e) {
                Date = textDate.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Date = textDate.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Date = textDate.getText();
            }
        });

        textTime.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void insertUpdate(DocumentEvent e) {
                Time = textTime.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                Time = textTime.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                Time = textTime.getText();
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

    public String get_Date() {
        return Date;
    }

    public String get_Time() {
        return Time;
    }
    
    //指定接收邮件的
    public String get_SpeMail() {
        return Specified_Mail;
    }

    public String get_SpePwd() {
        return Specified_pwd;
    }

    public String get_jList() {
        return jcbo_list;
    }

    public void resetThis() {
        textDate.setText("2013-11-20");
        textTime.setText("");
        MailID.setText("chengshuonew@gmail.com");
        MailPwd.setText("cs357951");
    }
}

class subPanel2 extends JPanel {

    private final String[] THAT = {"发微博", "发邮件"};
    private JTextField textID;
    private JPasswordField textPwd;
    private JTextField MyMail;
    private JPasswordField MyPwd;
    private JTextField toId;
    private JTextArea content;
    private JScrollPane scrollPane;   //邮件内容滑动条
    private final JComboBox jcbo;
    private JPanel p1;
    private JPanel p1_sub;
    private int Weibo_Mail;

    private String jcbo_list;
    private String blog_ID;
    private String blog_Pwd;
    private String myMail;
    private String mailPwd;
    private String toMailID;
    private String contentStr;

    public subPanel2() {
        jcbo = new JComboBox(THAT);
        jcbo_list = "发微博";
        JPanel p2 = new JPanel(new GridLayout(2, 1));
        p2.add(jcbo);

        JPanel labelPanel = new JPanel();
        JLabel label = new JLabel("THEN THAT");
        label.setFont(new Font("Courier", Font.BOLD, 22));
        labelPanel.add(label);
        /*根据列表选择显示p1或p1_sub*/
        p1 = new JPanel(new GridLayout(2, 2));
        p1.add(new JLabel("ID："));
        textID = new JTextField();
        textID.setEditable(true);
        textPwd = new JPasswordField();
        textPwd.setVisible(true);
        p1.add(textID);
        p1.add(new JLabel("密码："));
        p1.add(textPwd);
        content = new JTextArea(20, 20);
        content.setText("");
        scrollPane = new JScrollPane(content);
        scrollPane.setBorder(new TitledBorder("微博内容："));
        scrollPane.setPreferredSize(new Dimension(200, 120));

        p1_sub = new JPanel(new GridLayout(3, 2));
        p1_sub.add(new JLabel("邮箱:"));
        MyMail = new JTextField();
        p1_sub.add(MyMail);
        p1_sub.add(new JLabel("密码:"));
        MyPwd = new JPasswordField();
        p1_sub.add(MyPwd);
        p1_sub.add(new JLabel("接收邮箱:"));
        toId = new JTextField();
        p1_sub.add(toId);
        p1_sub.setVisible(false);

        JPanel selectPanel = new JPanel(new BorderLayout());
        selectPanel.add(p1, BorderLayout.NORTH);
        selectPanel.add(p1_sub, BorderLayout.CENTER);
        /*根据列表选择显示*/

        JPanel p3 = new JPanel(new GridLayout(2, 1));
        p3.add(labelPanel);
        p3.add(p2);

        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEtchedBorder());
        add(p3, BorderLayout.NORTH);
        add(selectPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        jcbo.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Weibo_Mail = jcbo.getSelectedIndex();
                if (Weibo_Mail == 0) {                                          //微博信息输入框
                    jcbo_list = "发微博";
                    p1.setVisible(true);
                    p1_sub.setVisible(false);
                    scrollPane.setBorder(new TitledBorder("微博内容："));
                    scrollPane.setPreferredSize(new Dimension(200, 120));
                    content.setText("");
                } else {                                                        //邮件信息输入框
                    jcbo_list = "发邮件";
                    p1.setVisible(false);
                    p1_sub.setVisible(true);
                    scrollPane.setBorder(new TitledBorder("邮件内容："));
                    scrollPane.setPreferredSize(new Dimension(200, 100));
                    content.setText("");
                }
            }
        });
        
        textID.getDocument().addDocumentListener(new DocumentListener() {
            //博客邮箱
            @Override
            public void insertUpdate(DocumentEvent e) {
                blog_ID = textID.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                blog_ID = textID.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                blog_ID = textID.getText();
            }
        });

        textPwd.getDocument().addDocumentListener(new DocumentListener() {
            //博客密码
            @Override
            public void insertUpdate(DocumentEvent e) {
                blog_Pwd = textPwd.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                blog_Pwd = textPwd.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                blog_Pwd = textPwd.getText();
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

        toId.getDocument().addDocumentListener(new DocumentListener() {
            //接收方邮箱
            @Override
            public void insertUpdate(DocumentEvent e) {
                toMailID = toId.getText();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                toMailID = toId.getText();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                toMailID = toId.getText();
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

    public String get_blog_ID() {
        return blog_ID;
    }

    public String get_blog_pwd() {
        return blog_Pwd;
    }

    public String get_con() {
        return contentStr;
    }

    public String get_myMail() {
        return myMail;
    }

    public String get_mailPwd() {
        return mailPwd;
    }

    public String getToMail() {
        return toMailID;
    }

    public String getjList() {
        return jcbo_list;
    }

    public void resetThat() {
        textID.setText("18252063039");
        textPwd.setText("cs12345");
        MyMail.setText("chengshuo357951@gmail.com");   //
        MyPwd.setText("chengshuo357951");
        toId.setText("1324480595@qq.com");    //1324480595@qq.com
        content.setText("");
    }
}
