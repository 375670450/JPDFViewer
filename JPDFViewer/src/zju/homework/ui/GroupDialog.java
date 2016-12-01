package zju.homework.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class GroupDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonJoin;
    private JButton buttonCreate;
    private JTextField groupIdField;

    public GroupDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonJoin);
        setMinimumSize(new Dimension(300, 100));
        setResizable(false);

        buttonJoin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onJoinGroup();
            }
        });

        buttonCreate.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCreateGroup();
            }
        });

    }

    public String getInpuNumber(){
        return groupIdField.getText();
    }

    public abstract void onJoinGroup();

    public abstract void onCreateGroup() ;


}
