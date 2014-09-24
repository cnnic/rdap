package org.rdap.port43.service.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.restfulwhois.rdap.port43.service.command.Command;
import org.restfulwhois.rdap.port43.service.command.CommandOption;
import org.restfulwhois.rdap.port43.service.command.CommandParser;

/**
 * command parser test.
 * 
 * @author jiashuo
 * 
 */
public class CommandParserTest {

    @Test
    public void testParse_domain_query() {
        Command command = CommandParser.parse("cnnic.cn");
        assertNotNull(command);
        assertEquals(CommandOption.IP_OR_DOMAIN_QUERY, command.getCommandType());
        assertNotNull(command.getArgumentList());
        assertTrue(command.getArgumentList().size() > 0);
        assertEquals("cnnic.cn", command.getArgumentList().get(0));
    }

    @Test
    public void testParse_ip_query() {
        Command command = CommandParser.parse("218.241.1.1");
        assertNotNull(command);
        assertEquals(CommandOption.IP_OR_DOMAIN_QUERY, command.getCommandType());
        assertNotNull(command.getArgumentList());
        assertTrue(command.getArgumentList().size() > 0);
        assertEquals("218.241.1.1", command.getArgumentList().get(0));
    }

    @Test
    public void testParse_domain_searchByName() {
        Command command = CommandParser.parse("domains cnnic.cn");
        assertNotNull(command);
        assertEquals(CommandOption.DOMAIN_SEARCH, command.getCommandType());
        assertNotNull(command.getArgumentList());
        assertTrue(command.getArgumentList().size() > 0);
        assertEquals("cnnic.cn", command.getArgumentList().get(0));
    }
    @Test
    public void testParse_domain_searchByNsLdhName() {
        Command command = CommandParser.parse("domains nsLdhName=1.in-addr.arpa");
        assertNotNull(command);
        assertEquals(CommandOption.DOMAIN_SEARCH, command.getCommandType());
        assertNotNull(command.getArgumentList());
    }
    @Test
    public void testParse_domain_searchByNsIp() {
        Command command = CommandParser.parse("domains nsIp=218.241.111.96");
        assertNotNull(command);
        assertEquals(CommandOption.DOMAIN_SEARCH, command.getCommandType());
        assertNotNull(command.getArgumentList());
    }

    @Test
    public void testParse_nameserver_query() {
        Command command = CommandParser.parse("nameserver ns.cnnic.cn");
        assertNotNull(command);
        assertEquals(CommandOption.NAMESERVER_QUERY, command.getCommandType());
        assertNotNull(command.getArgumentList());
        assertTrue(command.getArgumentList().size() > 0);
        assertEquals("ns.cnnic.cn", command.getArgumentList().get(0));
    }

    @Test
    public void testParse_nameserver_search() {
        Command command = CommandParser.parse("nameservers ns*.cnnic.cn");
        assertNotNull(command);
        assertEquals(CommandOption.NAMESERVER_SEARCH, command.getCommandType());
        assertNotNull(command.getArgumentList());
        assertTrue(command.getArgumentList().size() > 0);
        assertEquals("ns*.cnnic.cn", command.getArgumentList().get(0));
    }

    @Test
    public void testParse_nameserver_search_by_ip() {
        Command command = CommandParser.parse("nameservers ip=218.241.1.1");
        assertNotNull(command);
        assertEquals(CommandOption.NAMESERVER_SEARCH, command.getCommandType());
        assertNotNull(command.getArgumentList());
    }

    @Test
    public void testParse_entity_query() {
        Command command = CommandParser.parse("entity john");
        assertNotNull(command);
        assertEquals(CommandOption.ENTITY_QUERY, command.getCommandType());
        assertNotNull(command.getArgumentList());
        assertTrue(command.getArgumentList().size() > 0);
        assertEquals("john", command.getArgumentList().get(0));
    }

    @Test
    public void testParse_entity_search_by_fn() {
        Command command = CommandParser.parse("entities fn=john");
        assertNotNull(command);
        assertEquals(CommandOption.ENTITY_SEARCH, command.getCommandType());
    }

    @Test
    public void testParse_entity_search_by_handle() {
        Command command = CommandParser.parse("entities handle=handle_john");
        assertNotNull(command);
        assertEquals(CommandOption.ENTITY_SEARCH, command.getCommandType());
    }

}
