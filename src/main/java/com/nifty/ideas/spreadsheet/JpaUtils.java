package com.nifty.ideas.spreadsheet;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JpaUtils {
    
    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY;

    static {
        try {            
            ENTITY_MANAGER_FACTORY = Persistence.
                createEntityManagerFactory("documents_pu");
            
        } catch (Throwable ex) {
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }
    
    public static EntityManagerFactory getEntityManagerFactory(){
        return ENTITY_MANAGER_FACTORY;
    }

}
