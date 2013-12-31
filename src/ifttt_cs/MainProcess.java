/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifttt_cs;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;

/**
 *
 * @author cs
 */
public class MainProcess {

    public static List task_list = new ArrayList();

    public static void main(String[] args) throws InterruptedException {
        MainFrame frame = new MainFrame();
        frame.setTitle(null);
        frame.setSize(600, 500);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        while (true) {                                                            //监控当前选中的任务是否完成                        
            if (MainFrame.getItemNum() >MainFrame.getSeletedIndex() && MainFrame.getSeletedIndex()>=0) {
                if (((Task) task_list.get(MainFrame.getSeletedIndex())).isComplete()) { //任务完成
                   ((Task) task_list.get(MainFrame.getSeletedIndex())).setTaskOutMsg("该任务已完成...");
                    MainFrame.setOutMsg(((Task) task_list.get(MainFrame.getSeletedIndex())).getTaskOutMsg());                    
                }

                Thread.sleep(1000);
            }
        }
    }
}
