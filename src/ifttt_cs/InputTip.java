/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ifttt_cs;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 此类的主要功能是用于弹出提示框
 *
 * @author cs
 */
public class InputTip extends JFrame {

    private JLabel Tips;

    public void setMsg(String Msg) {
        Tips.setText(Msg);
    }

    public InputTip() {
        JPanel panel0 = new JPanel();
        Tips = new JLabel();
        panel0.add(Tips);
        JPanel panel1 = new JPanel();
        JButton ensure = new JButton("确定");
        panel1.add(ensure);
        this.setLayout(new BorderLayout());
        add(panel0, BorderLayout.CENTER);
        add(panel1, BorderLayout.SOUTH);

        ensure.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                closeWIin();
            }
        });
    }

    public void closeWIin() {
        this.setVisible(false);
    }
}
