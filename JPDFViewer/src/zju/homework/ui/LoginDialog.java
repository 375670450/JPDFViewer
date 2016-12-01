package zju.homework.ui;

import zju.homework.Account;
import zju.homework.NetworkManager;
import zju.homework.Util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class LoginDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonLogin;
    private JButton buttonRegister;
    private JTextField emailField;
    private JPasswordField passwordField;

    public LoginDialog() {
        setContentPane(contentPane);
        setModal(true);
        getRootPane().setDefaultButton(buttonLogin);
        setMinimumSize(new Dimension(400, 400));
        setResizable(false);

        buttonLogin.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onLogin();
            }
        });

        buttonRegister.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onRegister();
            }
        });

        emailField.setText("test@email.com");
        passwordField.setText("passwd");

    }

    public final Account formAccount(){
        return new Account(emailField.getText()
                , new String(passwordField.getPassword())
                , null);
    }

    public abstract void onLogin() ;

    public abstract void onRegister();

}
