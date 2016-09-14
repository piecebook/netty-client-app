package com.pb.client.form.ui;

import com.pb.server.constant.PBCONSTANT;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by piecebook on 2016/9/14.
 */
public class SearchFriend {
    private JTextField search_key;
    private JButton search;
    private JList search_result;
    private JPanel searchform_panel;

    public SearchFriend() {
        MouseListener mouseListener = new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) {
                JList theList = (JList) mouseEvent.getSource();
                if (mouseEvent.getClickCount() == 2) {
                    int index = theList.locationToIndex(mouseEvent.getPoint());
                    if (index >= 0) {
                        Object o = theList.getModel().getElementAt(index);
                        String select_user = o.toString();
                        Add_Friend_Dialog dialog = new Add_Friend_Dialog(select_user, PBCONSTANT.ADD_FRIENDS_FLAG);
                        dialog.pack();
                        dialog.setVisible(true);
                    }
                }
            }
        };
        search_result.addMouseListener(mouseListener);
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("SearchFriend");
        SearchFriend searchFriend = new SearchFriend();
        add(searchFriend);
        frame.setContentPane(searchFriend.searchform_panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public static void add(SearchFriend searchFriend) {
        DefaultListModel models = new DefaultListModel();
        models.addElement("aaa");
        models.addElement("bbb");
        searchFriend.getSearch_result().setModel(models);
    }

    public JTextField getSearch_key() {
        return search_key;
    }

    public void setSearch_key(JTextField search_key) {
        this.search_key = search_key;
    }

    public JButton getSearch() {
        return search;
    }

    public void setSearch(JButton search) {
        this.search = search;
    }

    public JList getSearch_result() {
        return search_result;
    }

    public void setSearch_result(JList search_result) {
        this.search_result = search_result;
    }

    public JPanel getSearchform_panel() {
        return searchform_panel;
    }

    public void setSearchform_panel(JPanel searchform_panel) {
        this.searchform_panel = searchform_panel;
    }
}
