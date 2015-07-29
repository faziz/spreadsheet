package com.nifty.ideas.spreadsheet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hibernate.Session;
import org.hibernate.jdbc.Work;
import org.junit.Test;
import static org.junit.Assert.*;

public class PersistSpreadsheetTest {

    @Test
    public void testPersist() throws SQLException, IOException {
        Workbook wb = createWorkbook();
        assertNotNull(wb);
        
        final EntityManagerFactory emf = JpaUtils.getEntityManagerFactory();        
        EntityManager em = emf.createEntityManager();

        Document doc = new Document();
        setContenct(doc, wb, em);
        assertNotNull(doc.getContent());
        assertWorkbook(doc);
        
        EntityTransaction tx = em.getTransaction();
        try{
            tx.begin();
            em.persist(doc);
            em.flush();
            tx.commit();        
        }catch(Exception ex) {
            tx.rollback();
        }
        
        em.close();
        
        em = emf.createEntityManager();
        doc = em.find(Document.class, doc.getId());
        assertNotNull(doc);
        assertNotNull(doc.getContent());
        assertWorkbook(doc);
        
    }

    private void setContenct(Document doc, Workbook wb, EntityManager em) {
        Session s = (Session) em.getDelegate();
        s.doWork(new BlobCreator(wb, doc));
    }

    private void assertWorkbook(Document doc) throws SQLException, IOException {
        
        
        try(InputStream is = doc.getContent().getBinaryStream()) {
            Workbook wb = new HSSFWorkbook(is);
            assertNotNull(wb);
            
            Sheet sheet = wb.getSheet("Sheet1");
            assertNotNull(sheet);
            
            Row row = sheet.getRow(0);
            assertNotNull(row);
            
            Cell cell = row.getCell(0);
            assertNotNull(cell);
            assertEquals("hello world.", cell.getStringCellValue());
        }
    }
    
    private class BlobCreator implements Work {

        public BlobCreator(Workbook wb, Document doc) {
            this.wb = wb;
            this.doc = doc;
        }
        
        private final Workbook wb;
        private final Document doc;

        @Override
        public void execute(Connection connection) throws SQLException {
            Blob blob = connection.createBlob();
            
            try (OutputStream os = new BufferedOutputStream(
                blob.setBinaryStream(1))){
                
                wb.write(os);
                
            }catch(IOException ioe){
                throw new IllegalStateException(ioe);
            }
            
            doc.setContent(blob);
        }
    }
    
    private Workbook createWorkbook() {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("Sheet1");
        Row row = sheet.createRow(0);
        Cell cell = row.createCell(0);
        cell.setCellValue("hello world.");
         
        return wb;
    }
}
