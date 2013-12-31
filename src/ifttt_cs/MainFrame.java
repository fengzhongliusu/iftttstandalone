 /* To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifttt_cs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.KeyStroke;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

/**
 *
 * @author cs
 */
public class MainFrame extends JFrame {

    private JFrame new_taskFrame;                                             //新建窗口
    private JFrame mdfy_tasFrame;                                             //修改窗口
    private JFrame delt_tasTaskFrame;                                         //删除窗口

    private static InputTip input_tips;
    private static JComboBox jcbo;
    private static JTextArea descip_task;                                    //输出当前任务的信息！！！！！
    private static JTextArea out_msg;
    private static int selectedIndex;                                         //当前序号

    private SimpleDateFormat df;                                              //设置日期格式
    private Task temp_task;

    /*指定邮箱格式错误弹出提示框*/
    public static void setTip() {
        input_tips.setMsg("邮箱格式错误，请重新输入");
        input_tips.setVisible(true);
    }
    
    /*获取当前选择框的记录个数*/
    public static int getItemNum() {
        return jcbo.getItemCount();
    }
    
    /*获取当前提示框显示的条目*/
    public static int getSeletedIndex() {
        return selectedIndex;
    }
    
    /*设置当前显示条目的序号*/
    public static void setSelect(int x) {                                      
        selectedIndex = x;
    }
    
    /*设置输出信息的内容*/
    public static void setOutMsg(String msg) {
        out_msg.setText(msg);
    }
    
    /*输出执行信息*/
    public static void appendMsg(String msg){
        out_msg.append("\n"+msg);
    }
    
    /*在输出信息框内增加显示一条信息*/
    public static void appendOutMsg(String msg) {
        out_msg.append("\n");
        out_msg.append(msg);
    }

    /*新建一个任务，在主界面更新*/
    public static void setMsg(String jcbo_list, String taskDescrip, String outMsg) {
        jcbo.addItem(jcbo_list);
        jcbo.setSelectedItem(jcbo_list);
    }

    /*修改一个任务，更新界面*/
    public static void ModifyMsg(String descrip, String outMsg) {
        out_msg.setText(outMsg);
        descip_task.setText(descrip);

    }

    /*删除一个任务，更新界面*/
    public static void remUpdate(int index) {                                 //删除指定序列号的任务
        jcbo.removeItemAt(index);
        if (selectedIndex < 0) {
            out_msg.setText("");
            descip_task.setText("");
        }
    }

    public void init() {
        new_taskFrame = new NewTaskFrame();
        new_taskFrame.setTitle("新建任务");
        new_taskFrame.setSize(500, 400);
        new_taskFrame.setLocationRelativeTo(null);
        new_taskFrame.setVisible(false);

        mdfy_tasFrame = new ModifyTaskFrame();
        mdfy_tasFrame.setTitle("修改任务");
        mdfy_tasFrame.setSize(500, 400);
        mdfy_tasFrame.setLocationRelativeTo(null);
        mdfy_tasFrame.setVisible(false);

        delt_tasTaskFrame = new DeltTaskFrame();
        delt_tasTaskFrame.setTitle("查看任务");
        delt_tasTaskFrame.setSize(350, 380);
        delt_tasTaskFrame.setLocationRelativeTo(null);
        delt_tasTaskFrame.setVisible(false);

        input_tips = new InputTip();
        input_tips.setTitle("提示：");
        input_tips.setSize(200, 110);
        input_tips.setLocationRelativeTo(null);
        input_tips.setVisible(false);
    }

    public MainFrame() {
        init(); //初始化
        JMenuBar menu = new JMenuBar();
        this.setJMenuBar(menu);

        JMenu task = new JMenu("任务");
        task.setFont(new Font("Courier", Font.BOLD, 12));
        JMenu ctrl = new JMenu("控制");
        ctrl.setFont(new Font("Courier", Font.BOLD, 12));
        JMenu help = new JMenu("帮助");
        help.setFont(new Font("Courier", Font.BOLD, 12));
        menu.add(task);
        menu.add(ctrl);
        menu.add(help);

        JMenuItem new_task = new JMenuItem("新建任务");
        JMenuItem mdfy_task = new JMenuItem("修改任务");
        JMenuItem delt_task = new JMenuItem("查看/删除任务");
        //设置快捷键
        new_task.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_MASK));
        mdfy_task.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, InputEvent.CTRL_MASK));
        delt_task.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, InputEvent.CTRL_MASK));

        JMenuItem begin_task = new JMenuItem("开始任务");
        JMenuItem stop_task = new JMenuItem("暂停任务");
        JMenuItem end_task = new JMenuItem("结束任务");

        JMenuItem aboutMsg = new JMenuItem("About");

        task.add(new_task);
        task.addSeparator();
        task.add(mdfy_task);
        task.addSeparator();
        task.add(delt_task);

        ctrl.add(begin_task);
        ctrl.addSeparator();
        ctrl.add(stop_task);
        ctrl.addSeparator();
        ctrl.add(end_task);
        help.add(aboutMsg);

        //中间靠右的子面板
        JPanel p1 = new JPanel(new GridLayout(1, 2));
        /**
         * ***********************************************************
         */
        /*显示标题*/
        JPanel subPanel = new JPanel();
        jcbo = new JComboBox();
        jcbo.setPreferredSize(new Dimension(100, 25));
        JPanel subPanel_p1 = new JPanel(new FlowLayout());
        JLabel name = new JLabel("单机版IFTTT");
        name.setFont(new Font("SansSerif", Font.BOLD, 20));
        subPanel_p1.add(name);
        subPanel_p1.setPreferredSize(new Dimension(300, 50));

        /*中间选择需要运行定的任务*/
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 5));
        p2.add(new JLabel("选择需要运行的任务:"));
        p2.add(jcbo);
        /*中间选择需要运行定的任务*/

        /*下面的任务描述*/
        descip_task = new JTextArea();
        descip_task.setText("此处为任务描述。。");
        descip_task.setFont(new Font("Serif", Font.PLAIN, 14));
        descip_task.setLineWrap(true);
        descip_task.setWrapStyleWord(true);
        descip_task.setEditable(false);
        JScrollPane subPane_scrollPane = new JScrollPane(descip_task);
        subPane_scrollPane.setBorder(new TitledBorder("正在运行的任务描述："));
        subPane_scrollPane.setPreferredSize(new Dimension(300, 150));
        /*下面的任务描述*/

        subPanel.setLayout(new BorderLayout());
        subPanel.add(subPanel_p1, BorderLayout.NORTH);
        subPanel.add(p2, BorderLayout.CENTER);
        subPanel.add(subPane_scrollPane, BorderLayout.SOUTH);
        /**
         * **************************************************************
         */
        p1.add(subPanel);

        JTextArea instructions = new JTextArea(2, 5);
        instructions.setText("使用说明：\n"
                + "1.首先用户新建任务\n"
                + "2.然后再选择控制->运行任务来开始任务\n"
                + "3.可在下列文本区域查看当前任务的完整描述\n");
        instructions.setFont(new Font("Serif", Font.PLAIN, 14));
        instructions.setLineWrap(true);
        instructions.setWrapStyleWord(true);
        instructions.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(instructions);
        p1.add(scrollPane);

        out_msg = new JTextArea(2, 5);
        out_msg.setText("此处为当前任务的执行状况...\n");
        out_msg.setFont(new Font("Serif", Font.PLAIN, 14));
        out_msg.setLineWrap(true);
        out_msg.setWrapStyleWord(true);
        out_msg.setEditable(false);
        JScrollPane out_msgJScrollPane = new JScrollPane(out_msg);
        out_msgJScrollPane.setBorder(new TitledBorder("输出信息"));

        /*添加到mainFrame*/
        this.setLayout(new BorderLayout());
        p1.setPreferredSize(new Dimension(600, 250));
        add(p1, BorderLayout.NORTH);
        add(out_msgJScrollPane, BorderLayout.CENTER);
        /*添加到mainFrame*/

        /*添加监听事件*/
        jcbo.addItemListener(new ItemListener() {

            @Override
            public void itemStateChanged(ItemEvent e) {
                selectedIndex = jcbo.getSelectedIndex();
                System.out.println("主界面选择的是:" + selectedIndex);
                if (selectedIndex >= 0 && jcbo.getItemCount()>selectedIndex && 
                     MainProcess.task_list.size() > selectedIndex){                     
                    temp_task = (Task) MainProcess.task_list.get(selectedIndex);
                    descip_task.setText(temp_task.getTaskDescrip());
                    out_msg.setText(temp_task.getTaskOutMsg());
                }
            }
        });

        new_task.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new_taskFrame.setVisible(true);
            }
        });

        mdfy_task.addActionListener(new ActionListener() {
            //修改任务
            @Override
            public void actionPerformed(ActionEvent e) {
                mdfy_tasFrame.setVisible(true);
            }
        });

        delt_task.addActionListener(new ActionListener() {
            //删除任务
            @Override
            public void actionPerformed(ActionEvent e) {
                delt_tasTaskFrame.setVisible(true);
            }
        });

        begin_task.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jcbo.getItemCount() > selectedIndex && selectedIndex >= 0) {                   //组合框有内容
                    if (((Task) MainProcess.task_list.get(selectedIndex)).getEndSign()) {   //没结束
                        temp_task.setTaskOutMsg("该任务已开始...");
                        out_msg.setText("该任务已开始...");
                        ((Task) MainProcess.task_list.get(selectedIndex)).startTask();
                    }
                }
            }
        });

        stop_task.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jcbo.getItemCount() > selectedIndex && selectedIndex >= 0) {                   //组合框有内容                                          
                    if (((Task) MainProcess.task_list.get(selectedIndex)).getEndSign()) {      //没结束
                        ((Task) MainProcess.task_list.get(selectedIndex)).stopTask();                        
                        temp_task.setTaskOutMsg("该任务已暂停...");
                        out_msg.setText("该任务已暂停...");
                    }
                }
            }
        });

        end_task.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (jcbo.getItemCount() > selectedIndex && selectedIndex >= 0) {                     //组合框有内容     
                         ((Task) MainProcess.task_list.get(selectedIndex)).endTask();   
                        temp_task.setTaskOutMsg("该任务已结束...");
                        out_msg.setText("该任务已结束...");
                }
            }
        });

        aboutMsg.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                input_tips.setMsg("CS出品");
                input_tips.setVisible(true);
            }
        });
        /*添加监听事件*/
    }
}
