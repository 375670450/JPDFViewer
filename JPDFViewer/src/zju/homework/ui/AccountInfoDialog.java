package zju.homework.ui;

import zju.homework.Account;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class AccountInfoDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JLabel emailLabel;
    private JLabel groupLabel;
    private JLabel iconLabel;

    public AccountInfoDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonOK);

        setMinimumSize(new Dimension(300, 400));
        setResizable(false);
        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
    }

    public void setAccount(Account account){
        try{
            Image img = ImageIO.read(getClass().getResource("../resources/user-icon.png"));
            iconLabel.setText("");
            iconLabel.setIcon(new ImageIcon(img.getScaledInstance(200, 200, Image.SCALE_DEFAULT)));
            emailLabel.setText(account.getEmail());
            String groupId = account.getGroupId();
            groupLabel.setText(groupId == null ? "None" : groupId);
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }



//
//    private void onJoinGroup() {
//        // add your code here
//        dispose();
//    }
//
//    private void onCreateGroup() {
//        // add your code here if necessary
//        dispose();
//    }

}
