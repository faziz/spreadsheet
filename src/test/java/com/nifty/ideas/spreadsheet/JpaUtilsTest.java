package com.nifty.ideas.spreadsheet;

import javax.persistence.EntityManagerFactory;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class JpaUtilsTest {
    
    private static EntityManagerFactory emf;    
    
    @BeforeClass
    public static void setUpClass() {
        emf = JpaUtils.getEntityManagerFactory();
    }
    

    /**
     * Test of getEntityManagerFactory method, of class JpaUtils.
     */
    @Test
    public void testGetEntityManagerFactory() {
        assertNotNull(emf);
    }
    
}
