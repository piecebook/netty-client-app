package com.pb.client.form.ui;

import com.pb.client.form.daemon.ChatDaemon;
import com.pb.client.sdk.model.MsgPipe;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by piecebook on 2016/9/13.
 */
public class Main {
    private JList friends;
    private JPanel info_panel;
    private JLabel username;
    private JPanel friends_panel;
    private JPanel main_panel;
    private JButton add_friend;


    public Main() {
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        Object o = theList.getModel().getElementAt(index);
                        String select_user = o.toString();
                        System.out.println(o.toString());
                        Integer win = MsgPipe.friends_win.get(select_user);
                        if (win == 0) {
                            MsgPipe.friends_win.put(select_user, 1);
                            Thread thread = new Thread(new ChatDaemon(MsgPipe.friends.get(select_user)));
                            thread.start();
                        }
                    }
                }
            }
        };
        friends.addMouseListener(mouseListener);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("Main");
        frame.setContentPane(new Main().main_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public JList getFriends() {
        return friends;
    }

    public JPanel getInfo_panel() {
        return info_panel;
    }

    public JLabel getUsername() {
        return username;
    }

    public JPanel getFriends_panel() {
        return friends_panel;
    }

    public JPanel getMain_panel() {
        return main_panel;
    }

    public JButton getAdd_friend() {
        return add_friend;
    }
}
