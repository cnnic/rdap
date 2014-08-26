package org.rdap.port43.service.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Test;

/**
 * command parser test.
 * 
 * @author jiashuo
 * 
 */
public class CommandParserTest {

    @Test
    public void testParse_domain_query() {
        Command command = CommandParser.parse("whois cnnic.cn");
        assertNotNull(command);
        assertEquals(CommandOption.IP_OR_DOMAIN_QUERY, command.getCommandType());
        assertNotNull(command.getArgumentList());
        assertTrue(command.getArgumentList().size() > 0);
        assertEquals("cnnic.cn", command.getArgumentList().get(0));
    }

    @Test
    public void testParse_ip_query() {
        Command command = CommandParser.parse("whois 218.241.1.1");
        assertNotNull(command);
        assertEquals(CommandOption.IP_OR_DOMAIN_QUERY, command.getCommandType());
        assertNotNull(command.getArgumentList());
        assertTrue(command.getArgumentList().size() > 0);
        assertEquals("218.241.1.1", command.getArgumentList().get(0));
    }

    @Test
    public void testParse_domain_search() {
        Command command = CommandParser.parse("whois --domains cnnic.cn");
        assertNotNull(command);
        assertEquals(CommandOption.DOMAIN_SEARCH, command.getCommandType());
        assertNotNull(command.getArgumentList());
        assertTrue(command.getArgumentList().size() > 0);
        assertEquals("cnnic.cn", command.getArgumentList().get(0));
    }

    @Test
    public void testParse_nameserver_query() {
        Command command = CommandParser.parse("whois --nameserver ns.cnnic.cn");
        assertNotNull(command);
        assertEquals(CommandOption.NAMESERVER_QUERY, command.getCommandType());
        assertNotNull(command.getArgumentList());
        assertTrue(command.getArgumentList().size() > 0);
        assertEquals("ns.cnnic.cn", command.getArgumentList().get(0));
    }

    @Test
    public void testParse_nameserver_search() {
        Command command =
                CommandParser.parse("whois --nameservers ns*.cnnic.cn");
        assertNotNull(command);
        assertEquals(CommandOption.NAMESERVER_SEARCH, command.getCommandType());
        assertNotNull(command.getArgumentList());
        assertTrue(command.getArgumentList().size() > 0);
        assertEquals("ns*.cnnic.cn", command.getArgumentList().get(0));
    }

    @Test
    public void testParse_nameserver_search_by_ip() {
        Command command =
                CommandParser.parse("whois --nameservers --ip=218.241.1.1");
        assertNotNull(command);
        assertEquals(CommandOption.NAMESERVER_SEARCH, command.getCommandType());
        assertNotNull(command.getArgumentList());
        Map<String, String> allOptionsMap = command.getAllOptionsMap();
        assertNotNull(allOptionsMap);
        assertTrue(allOptionsMap
                .containsKey(CommandOption.NAMESERVER_SEARCH_BY_IP.getOption()));
        assertEquals("218.241.1.1",
                allOptionsMap.get(CommandOption.NAMESERVER_SEARCH_BY_IP
                        .getOption()));
    }

    @Test
    public void testParse_entity_query() {
        Command command = CommandParser.parse("whois --entity john");
        assertNotNull(command);
        assertEquals(CommandOption.ENTITY_QUERY, command.getCommandType());
        assertNotNull(command.getArgumentList());
        assertTrue(command.getArgumentList().size() > 0);
        assertEquals("john", command.getArgumentList().get(0));
    }

    @Test
    public void testParse_entity_search_by_fn() {
        Command command = CommandParser.parse("whois --entities --fn=john");
        assertNotNull(command);
        assertEquals(CommandOption.ENTITY_SEARCH, command.getCommandType());
        Map<String, String> allOptionsMap = command.getAllOptionsMap();
        assertNotNull(allOptionsMap);
        assertTrue(allOptionsMap.containsKey(CommandOption.ENTITY_SEARCH_FN
                .getOption()));
        assertEquals("john",
                allOptionsMap.get(CommandOption.ENTITY_SEARCH_FN.getOption()));
    }

    @Test
    public void testParse_entity_search_by_handle() {
        Command command =
                CommandParser.parse("whois --entities --handle=handle_john");
        assertNotNull(command);
        assertEquals(CommandOption.ENTITY_SEARCH, command.getCommandType());
        Map<String, String> allOptionsMap = command.getAllOptionsMap();
        assertNotNull(allOptionsMap);
        assertTrue(allOptionsMap.containsKey(CommandOption.ENTITY_SEARCH_HANDLE
                .getOption()));
        assertEquals("handle_john",
                allOptionsMap.get(CommandOption.ENTITY_SEARCH_HANDLE
                        .getOption()));
    }

}
