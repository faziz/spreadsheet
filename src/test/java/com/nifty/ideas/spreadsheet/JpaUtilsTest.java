package com.nifty.ideas.spreadsheet;

import javax.persistence.EntityManagerFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class JpaUtilsTest {
    
    private static EntityManagerFactory emf;
    
    /**
     * Test of getEntityManagerFactory method, of class JpaUtils.
     */
    @Test
    public void testGetEntityManagerFactory() {
        emf = JpaUtils.getEntityManagerFactory();
        assertNotNull(emf);
    }
}
