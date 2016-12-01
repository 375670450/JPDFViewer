package zju.homework;

import com.fasterxml.jackson.core.type.TypeReference;
import com.qoppa.pdf.PDFException;
import com.qoppa.pdf.annotations.Annotation;
import com.qoppa.pdf.annotations.Circle;
import com.qoppa.pdf.annotations.FreeText;
import com.qoppa.pdf.annotations.IAnnotationFactory;
import com.qoppa.pdfNotes.AnnotToolbar;
import com.qoppa.pdfNotes.MutableDocument;
import com.qoppa.pdfNotes.PDFNotesBean;
import com.qoppa.pdfNotes.settings.FreeTextTool;
import com.qoppa.pdfViewer.SelectToolbar;
import com.sun.org.apache.xml.internal.security.utils.*;
import com.sun.org.apache.xml.internal.security.utils.Base64;
import zju.homework.ui.AccountInfoDialog;
import zju.homework.ui.GroupDialog;
import zju.homework.ui.LoginDialog;


import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.font.TextAttribute;
import java.io.*;
import java.util.*;
import java.util.List;

/**
 * Created by stardust on 2016/11/28.
 */

public class SimpleFrame extends JFrame implements ActionListener
{

    private final static String FREETEXT_CORRECT = "FreeTextCorrect";
    private final static String STAMP_CORRECT = "StampCorrect";
    private final static String RED_CIRCLE = "RedCircle";

    private final static String[] iconImgs = new String[]{
            "resources/user-icon.png",                // LOGIN/ACCOUNT BUTTON
            "resources/group-icon.png",             // GROUP BUTTON
            "resources/sync-icon.png",              // SYNC ANNOTATION BUTTON
    };

    private JPanel userInfoPanel = null;
    private JPanel groupInfoPanel = null;
    private JPanel jPanel = null;

    private JButton[] buttons = null;

    private MyNotesBean m_NotesBean = null;

    private Account account = null;

    private String groupId = null;

    private NetworkManager networkManager = new NetworkManager();


//    public static void main (String [] args)
//    {
//        SimpleFrame sf = new SimpleFrame();
//        sf.setVisible(true);
//    }
    /**
     * This method initializes
     *
     */
    public SimpleFrame()
    {
        super();
        initialize();
    }

    /**
     * This method initializes this
     *
     */
    private void initialize()
    {
        this.setBounds(new Rectangle(0, 0, 1400, 800));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(getJPanel());
        this.setTitle("JPDFViewer");
        this.setLocationRelativeTo(null);

        this.initButtons();
        this.initToolbars();
        this.updateAccountInfo(null);
        this.updateGroupInfo(null);
//        this.updateAccountInfo(new Account("asdasd", "asdasd", null));

        try {
            this.getNotesBean().loadPDF("E:\\Documents\\01-1434092018-2654688.pdf");
        }catch (PDFException ex){
            ex.printStackTrace();
        }
    }

    private void initToolbars(){
        SelectToolbar upToolbar = m_NotesBean.getSelectToolbar();
        userInfoPanel= new JPanel(new FlowLayout(FlowLayout.TRAILING));

        upToolbar.add(userInfoPanel);

        AnnotToolbar belowToolbar =  m_NotesBean.getAnnotToolbar();
        groupInfoPanel  = new JPanel(new FlowLayout(FlowLayout.TRAILING));
        groupInfoPanel.setVisible(true);
        belowToolbar.add(groupInfoPanel);
    }

    private void initButtons(){

        AnnotToolbar toolbar = m_NotesBean.getAnnotToolbar();
        Dimension buttonSize =  toolbar.getjbImage().getPreferredSize();
        Icon icon = toolbar.getjbImage().getIcon();
        buttons = new JButton[iconImgs.length];

        try {
           for (int i=0; i<iconImgs.length; i++){
               buttons[i] = new JButton();
               buttons[i].setVisible(true);
               buttons[i].setSize(buttonSize);
               Image img = ImageIO.read(getClass().getResource(iconImgs[i]));
               buttons[i].setIcon(new ImageIcon(img.getScaledInstance(icon.getIconWidth(), icon.getIconHeight(), Image.SCALE_DEFAULT)));
               m_NotesBean.getSelectToolbar().add(buttons[i]);
           }


           buttons[0].addActionListener(new ActionListener() {      // Login Button
               @Override
               public void actionPerformed(ActionEvent e) {
                   if( account == null )
                       showLogin();
                   else
                       showAccount();
               }
           });

            buttons[1].addActionListener(new ActionListener() {     // Join Group Button
                @Override
                public void actionPerformed(ActionEvent e) {
                    if( account == null ){
                        JOptionPane.showMessageDialog(null, "You have to login first");
                    } else if( m_NotesBean.getDocument() == null ){
                        JOptionPane.showMessageDialog(null, "You have to open a document first");
                    } else{
                        showGroup();
                    }
                }
            });

            buttons[2].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
//                    uploadAnnotation();
//                    downloadAnnotations();
                    if( account == null || groupId == null ){
                        JOptionPane.showMessageDialog(null, "You are offline");
                    }else{
                        uploadAnnotation();
                        downloadAnnotations();
                    }
                }
            });
            buttons[2].setVisible(false);

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }


    /*
    *  Download annotations from server
    *
    * */
    private void downloadAnnotations(){

        SwingWorker worker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {
                String result = networkManager.getJson(NetworkManager.downloadAnnotUrl + groupId);
                List<AnnotationData> list = (List<AnnotationData>) Util.jsonToObject(result, new TypeReference<List<AnnotationData>>(){});
                for(AnnotationData data : list){
//                    File tmp = File.createTempFile("download", ".annotations");
//                    FileOutputStream os = new FileOutputStream(tmp);
//                    os.write(data.getData().getBytes());
//                    System.out.println(tmp.getAbsolutePath());
//                    Util.base64ToFile(data.getData(), tmp);
                        m_NotesBean.importNewAnnotations(new ByteArrayInputStream(Base64.decode(data.getData())));
                }
                return null;
            }

        };
        worker.execute();

    }

    /*
    *  Upload annotations in document
    *
    * */
    private void uploadAnnotation(){
        // get annotations to upload

        try{
            final File tmpFile = m_NotesBean.exportNewAnnotations();

            SwingWorker worker = new SwingWorker() {                        // update document annotations asynchronously
                @Override
                protected Object doInBackground() throws Exception {

                    AnnotationData data = new AnnotationData(account.getEmail(),
                            Util.inputStreamToBase64(new FileInputStream(tmpFile)),
                            groupId);
                    String result = networkManager.postJson(NetworkManager.uploadAnnotUrl, Util.objectToJson(data));
                    if( !result.equals("Upload Annotation Success") )
                        System.err.println("upload annotation failed");
                    return null;
                }

            };
            worker.execute();

        }catch (PDFException ex){
            ex.printStackTrace();
        }catch (IOException ex){
            ex.printStackTrace();
        }


    }

    private void showGroup(){
        GroupDialog dialog = new GroupDialog() {
            @Override
            public void onJoinGroup() {
                String groupId = this.getInpuNumber();

                String result = networkManager.getJson(NetworkManager.joinGroupUrl + groupId);
                if( result.equals("Group Not Found") ){
                    JOptionPane.showMessageDialog(this, result);
                }else{      // got the pdfData(BASE64) of group
                    JOptionPane.showMessageDialog(this, "Join Group Success");
                    updateGroupInfo(groupId);
                    try {
                        File file = File.createTempFile("jpdfviewer", ".pdf");
                        Util.base64ToFile(result, file);
                        m_NotesBean.loadPDF(file.getAbsolutePath());
                    }catch (IOException ex){
                        ex.printStackTrace();
                    }catch (PDFException ex){
                        ex.printStackTrace();
                    }
                    dispose();
                }
            }

            @Override
            public void onCreateGroup() {
                String filePath = m_NotesBean.getFilePath();
                Group group = new Group();
                try {
                    String pdfData = Util.inputStreamToBase64(new FileInputStream(new File(filePath)));
                    group.setPdfData(pdfData);
                    group.setCreator(account.getEmail());
                    group.setGroupId(this.getInpuNumber());

                    String result = networkManager.postJson(NetworkManager.createGroupUrl, Util.objectToJson(group));
                    JOptionPane.showMessageDialog(this, result);
                    if( result.equals("Create Group Success") ){
                        updateGroupInfo(group.getGroupId());
                        dispose();
                    }
                }catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        };

        dialog.pack();
        dialog.setVisible(true);

    }

    private void showAccount(){

        AccountInfoDialog dialog = new AccountInfoDialog();
        dialog.setAccount(account);
        dialog.setVisible(true);
        dialog.pack();
        dialog.setModal(true);
    }

    private void showLogin(){

        LoginDialog loginDialog = new LoginDialog(){
            @Override
            public void onLogin()  {
                Account input = formAccount();
                String result = networkManager.postJson(NetworkManager.loginUrl, Util.objectToJson(input));
                if( result.equals("Login Success") ){
//                                account = input;
                    updateAccountInfo(input);
                    JOptionPane.showMessageDialog(this, result);
                    dispose();
                }else{
                    JOptionPane.showMessageDialog(this, result);
                }
            }

            @Override
            public void onRegister() {
                Account input = formAccount();
                String result = networkManager.postJson(NetworkManager.registerUrl, Util.objectToJson(input));
                if( result.equals("Register Success") ){
//                                account = input;
                    updateAccountInfo(input);
                    JOptionPane.showMessageDialog(this, result);
                    dispose();
                }else {
                    JOptionPane.showMessageDialog(this, result);
                }
            }
        };
        loginDialog.setVisible(true);
        loginDialog.pack();
        loginDialog.setModal(true);
    }

    private void updateGroupInfo(String newGroup){
        this.groupId = newGroup;
        groupInfoPanel.removeAll();
        JLabel label = null;
        if( this.groupId == null ){
            label = new JLabel("Offline");
            buttons[2].setVisible(false);
        }else {
            label = new JLabel("Group ID: " + this.groupId);
            buttons[2].setVisible(true);
        }
        groupInfoPanel.add(label);
    }

    private void updateAccountInfo(Account newAccount){
        this.account = newAccount;
        if( this.account == null ){
            userInfoPanel.removeAll();
            userInfoPanel.setVisible(false);
            m_NotesBean.setAccount(null);
        }else {
            userInfoPanel.setVisible(true);
            m_NotesBean.setAccount(account.getEmail());
            JLabel welcomeLabel = new JLabel("Welcome, ");
            JLabel accountLabel = new JLabel(this.account.getEmail());

            accountLabel.addMouseListener(new MouseAdapter() {
                Font originFont;

                @Override
                public void mouseEntered(MouseEvent e) {
                    super.mouseEntered(e);
                    originFont = e.getComponent().getFont();
                    Map attributes = originFont.getAttributes();
                    attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
                    attributes.put(TextAttribute.FOREGROUND, Color.BLUE);
                    e.getComponent().setFont(originFont.deriveFont(attributes));
                    e.getComponent().setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    super.mouseExited(e);
                    e.getComponent().setFont(originFont);
                    e.getComponent().setCursor(Cursor.getDefaultCursor());
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                    int result = JOptionPane.showConfirmDialog(e.getComponent(), "Log out?");
                    if( result == JOptionPane.OK_OPTION ){
                        updateAccountInfo(null);
                    }
                }
            });
            userInfoPanel.add(welcomeLabel);
            userInfoPanel.add(accountLabel);
        }
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel()
    {
        if (jPanel == null)
        {
            jPanel = new JPanel();
            jPanel.setLayout(new BorderLayout());
            jPanel.add(getNotesBean(), BorderLayout.CENTER);
        }
        return jPanel;
    }

    /**
     * This method initializes PDFViewerBean
     *
     * @return com.qoppa.pdfViewer.PDFViewerBean
     */
    private PDFNotesBean getNotesBean()
    {
        if (m_NotesBean == null)
        {
            m_NotesBean = new MyNotesBean();

            // Create custom buttons
            JButton ftCorrect = new JButton ("FT");
            ftCorrect.setActionCommand(FREETEXT_CORRECT);
            ftCorrect.addActionListener(this);
            m_NotesBean.getAnnotToolbar().add (ftCorrect);

            JButton rsCorrect = new JButton ("RS");
            rsCorrect.setActionCommand(STAMP_CORRECT);
            rsCorrect.addActionListener(this);
            m_NotesBean.getAnnotToolbar().add (rsCorrect);

            // Red Circle
            JButton jbRedCircle = new JButton ("RC");
            jbRedCircle.setActionCommand(RED_CIRCLE);
            jbRedCircle.addActionListener(this);
            m_NotesBean.getAnnotToolbar().add (jbRedCircle);
        }
        return m_NotesBean;
    }

    public void actionPerformed(ActionEvent e)
    {
        MutableDocument doc = m_NotesBean.getMutableDocument();

        if(doc != null)
        {
            IAnnotationFactory factory = doc.getAnnotationFactory();
//            IPDFPage page = m_NotesBean.getDocument().getIPage(0);
//
//            try {
//                Vector<Annotation> annotations = page.getAnnotations();
//                for (Annotation annotation : annotations){
//                    System.out.println(annotation.getCreator());
//                }
//            }catch (PDFException ex){
//                ex.printStackTrace();
//            }


            if (e.getActionCommand() == FREETEXT_CORRECT)
            {
                FreeTextTool.setShowPropDialog(false);
                FreeText correctAnnot = factory.createFreeText("Correct");
                m_NotesBean.startEdit (correctAnnot, false, false);
            }
            else if (e.getActionCommand() == STAMP_CORRECT)
            {
                m_NotesBean.startEdit (factory.createRubberStamp ("Correct", Color.blue), false, false);
            }
            else if (e.getActionCommand() == RED_CIRCLE)
            {
                Circle redCircle = factory.createCircle(null);
                redCircle.setColor(Color.red);
                redCircle.setInternalColor(Color.blue);
                m_NotesBean.startEdit(redCircle, false, false);
            }
        }


    }
}
