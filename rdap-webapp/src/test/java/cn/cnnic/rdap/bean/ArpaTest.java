package cn.cnnic.rdap.bean;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class ArpaTest {

	/**
     * test findObjectFromListById.
     */
    @Test
    public void testArpa() {
        
        Arpa arpa = null;
        
        arpa = Arpa.decodeArpa("2.0.0.in-addr.arpa");
        
        assertNotNull(arpa);
        assertEquals(arpa.getIpVersion().getName(), "v4");
        assertEquals(arpa.getEndLowAddress().toString(), "767");
        assertEquals(arpa.getStartLowAddress().toString(), "512");
        assertEquals(arpa.getStartHighAddress().toString(), "0");
        assertEquals(arpa.getEndHighAddress().toString(), "0");
        
        arpa = Arpa.decodeArpa("F.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.ip6.arpa");
        
        assertNotNull(arpa);
        assertEquals(arpa.getIpVersion().getName(), "v6");
        assertEquals(arpa.getEndLowAddress().toString(), "15");
        assertEquals(arpa.getStartLowAddress().toString(), "15");
        assertEquals(arpa.getEndHighAddress().toString(), "0");
        assertEquals(arpa.getStartHighAddress().toString(), "0");
        
        arpa = Arpa.decodeArpa("1.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.0.ip6.arpa");
        
        assertNotNull(arpa);
        assertEquals(arpa.getIpVersion().getName(), "v6");
        assertEquals(arpa.getEndLowAddress().toString(), "511");
        assertEquals(arpa.getStartLowAddress().toString(), "256");
        assertEquals(arpa.getEndHighAddress().toString(), "0");
        assertEquals(arpa.getStartHighAddress().toString(), "0");
        
        arpa = Arpa.decodeArpa("f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.f.ip6.arpa");
        
        assertNotNull(arpa);
        assertEquals(arpa.getIpVersion().getName(), "v6");
        
        assertEquals(arpa.getStartLowAddress().toString(), "18446744073709551615");
        assertEquals(arpa.getStartHighAddress().toString(), "18446744073709551615");
        assertEquals(arpa.getEndHighAddress().toString(), "18446744073709551615");
        assertEquals(arpa.getEndLowAddress().toString(), "18446744073709551615");
                        
    }

}
