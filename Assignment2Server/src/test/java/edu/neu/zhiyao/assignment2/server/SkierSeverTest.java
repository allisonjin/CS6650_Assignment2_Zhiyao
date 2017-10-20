/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.neu.zhiyao.assignment2.server;

import edu.neu.zhiyao.assignment2.server.entity.RFIDLiftData;
import javax.ws.rs.core.Response;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author allisonjin
 */
public class SkierSeverTest {
    
    public SkierSeverTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getVert method, of class SkierSever.
     */
    @Test
    public void testGetVert() {
        System.out.println("getVert");
        String skierId = "123";
        int dayNum = 0;
        SkierSever instance = new SkierSever();
        String expResult = "123";
        String result = instance.getVert(skierId, dayNum);
        assertEquals(expResult, result);
    }

    /**
     * Test of postRFIDLiftData method, of class SkierSever.
     */
    @Test
    public void testPostRFIDLiftData() {
        System.out.println("postRFIDLiftData");
        RFIDLiftData data = new RFIDLiftData(0, 1, 123, 10, 100);
        SkierSever instance = new SkierSever();
        Response expResult = null;
        Response result = instance.postRFIDLiftData(data);
        assertEquals(201, result.getStatus());
    }
    
}
