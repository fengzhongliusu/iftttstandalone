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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.print.attribute.standard.MultipleDocumentHandling;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.basic.BasicListUI;

/**
 *
 * @author cs
 */
public class DeltTaskFrame extends JFrame {

    private String[] taskList;
    private String[] taskDescription;
    private static JList jlist;                                                //列表框
    private static DefaultListModel dlm;                                       //列表框内容
    private JTextArea textArea;                                                //任务描述文本区域
    private JButton cancle;
    private JButton remove;
    private int removeIndex;
    private int SelectedIndex;

    public static void addJList(String str) {                                  //新建任务列表增加一项
        dlm.addElement(str);
        jlist.setModel(dlm);
    }

    public int getRemoveIndex() {                                             //获取删除的那一项的下表
        return removeIndex;
    }

    public void closeWin() {                                                   //关闭窗口
        this.setVisible(false);
    }

    public DeltTaskFrame() {
        /*任务列表面板*/
        JPanel subPanel1 = new JPanel(new BorderLayout());
        jlist = new JList();
        jlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);           //单选模式
        dlm = new DefaultListModel();                                          //列表的内容
        jlist.setModel(dlm);                                                   //设置列表的内容

        JScrollPane listScrollPane = new JScrollPane(jlist);                   //滚动条
        listScrollPane.setPreferredSize(new Dimension(200, 200));
        JLabel label1 = new JLabel("任务列表:");
        label1.setFont(new Font("Courier", Font.BOLD, 13));
        subPanel1.add(label1, BorderLayout.NORTH);
        subPanel1.add(listScrollPane, BorderLayout.CENTER);
        /*任务列表面板*/

        /**
         * 描述信息列表*
         */
        JPanel subJPane2 = new JPanel(new BorderLayout());
        JLabel lable2 = new JLabel("选中任务的详细信息：");
        lable2.setFont(new Font("Courier", Font.BOLD, 13));
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        JScrollPane textScrollPane = new JScrollPane(textArea);
        textScrollPane.setPreferredSize(new Dimension(200, 200));
        subJPane2.add(lable2, BorderLayout.NORTH);
        subJPane2.add(textScrollPane, BorderLayout.CENTER);
        /**
         * 描述信息列表*
         */

        /*中间的两个面板合体*/
        JPanel subJPane3 = new JPanel(new GridLayout(1, 2, 20, 5));
        subJPane3.add(subPanel1);
        subJPane3.add(subJPane2);
        subJPane3.setBorder(BorderFactory.createEtchedBorder());
        /*中间的两个面板合体*/

        /*按钮面板*/
        JPanel subJPane4 = new JPanel(new FlowLayout(FlowLayout.CENTER, 100, 10));
        remove = new JButton("删除");
        subJPane4.add(remove);
        cancle = new JButton("退出");
        subJPane4.add(cancle);
        /*按钮面板*/

        /*加入frame*/
        this.setLayout(new BorderLayout());
        JLabel label3 = new JLabel("查看/删除任务");
        JPanel subPanel5 = new JPanel();
        subPanel5.add(label3);
        label3.setFont(new Font("Courier", Font.BOLD, 15));
        add(subPanel5, BorderLayout.NORTH);
        add(subJPane3, BorderLayout.CENTER);
        add(subJPane4, BorderLayout.SOUTH);
        /*加入frame*/

        /*列表框应答*/
        jlist.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int indice = jlist.getSelectedIndex();
                SelectedIndex = indice;
                System.out.println("删除框选择的条目是：" + SelectedIndex);
                if (indice >= 0) {
                    textArea.setText(((Task) MainProcess.task_list.get(indice)).getTaskDescrip());
                }
            }
        });
        /*列表框应答*/

        /*删除按钮应答*/
        remove.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                //MainFrame.setSelect(0);  
                //ModifyTaskFrame.setSelect(0);
                int index = SelectedIndex;
                System.out.println("删除前选择的是:" + index);
                if (index >= 0) {
                    MainFrame.setSelect(0);  
                    ModifyTaskFrame.setSelect(0);    
                    dlm.removeElementAt(SelectedIndex);
                    System.out.println("删除后选择的是:" + SelectedIndex);
                    jlist.updateUI();
                    textArea.setText("");

                    if (((Task) MainProcess.task_list.get(index)).isStart()) {
                        ((Task) MainProcess.task_list.get(index)).stopTask();     //暂停选中的任务
                    }

                    MainProcess.task_list.remove(index);                          //任务列表删除该任务
                    MainFrame.remUpdate(index);                                   //主界面更新显示
                    ModifyTaskFrame.remUpdate(index);                             //修改界面更新显示
                }
            }
        });
        /*删除按钮应答*/

        /*退出按钮应答*/
        cancle.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeWin();
            }
        });
        /*退出按钮应答*/
    }

}
