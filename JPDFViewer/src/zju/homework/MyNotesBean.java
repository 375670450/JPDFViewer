package zju.homework;

import com.qoppa.pdf.PDFException;
import com.qoppa.pdf.annotations.Annotation;
import com.qoppa.pdf.annotations.IAnnotationFactory;
import com.qoppa.pdf.dom.IPDFDocument;
import com.qoppa.pdf.dom.IPDFPage;
import com.qoppa.pdf.javascript.Console;
import com.qoppa.pdf.source.PDFSource;
import com.qoppa.pdfNotes.PDFNotesBean;
import com.qoppa.pdfViewer.PDFViewerBean;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * Created by stardust on 2016/11/28.
 */
public class MyNotesBean extends PDFNotesBean{

    private String documentPath = null;

    private Set hasUploaded = new HashSet<Annotation>();

    private String account = null;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Override
    public void startEdit(Annotation annot, boolean useDefault, boolean  isSticky) {
        // Call PDFNotesBean to set its own properties
        super.startEdit(annot, useDefault, isSticky);
        if( account != null )
            annot.setCreator(account);
    }

    @Override
    protected void loadPDF(PDFSource pdfSource, boolean b) throws PDFException {
        super.loadPDF(pdfSource, b);
        documentPath = pdfSource.getPath();
    }

    public String getFilePath(){
        return documentPath;
    }

    public File exportNewAnnotations() throws PDFException, IOException{
        Set result = new HashSet<Annotation>();
        IPDFDocument document = this.getDocument();
        for(int i=0; i<document.getPageCount(); i++){
            for(Annotation annotation : document.getIPage(i).getAnnotations()){
                if( !hasUploaded.contains(annotation) ){
                    hasUploaded.add(annotation);
                    result.add(annotation);
                }
            }
        }
        File tmpFile = File.createTempFile("upload", ".annotations");
        this.getMutableDocument().exportAnnotsAsFDF(tmpFile.getAbsolutePath(), result);
//        System.out.println(tmpFile.getAbsolutePath());
        return tmpFile;
    }

    public void importNewAnnotations(InputStream is) throws PDFException{
        this.getMutableDocument().importAnnotsFromFDF(is);
    }

}
