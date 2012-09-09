/*
 *  Copyright (c) 2010 Mark Manes
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.acmerocket.jputio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Mark Manes
 * 
 */
public class RequestorTest {
    @Before
    public void setup() {
        Requestor unit = Requestor.instance();
        unit.setThreadCredentials("TEST", "TEST");
    }

    @Test
    public void testRequestorCredentials() throws Exception {

        final AtomicBoolean hasCreds = new AtomicBoolean();

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Requestor.instance().makeRequest(new Request(""));
                    hasCreds.set(true);
                }
                catch (Exception e) {
                    hasCreds.set(false);
                }

            }
        });
        t.start();
        t.join();

        assertFalse(hasCreds.get());
    }

    @Test
    public void testItem() throws Exception {
        Item item = new Item();
        // Dir dir = item.dirmap();
        List<Item> items = item.listChildren();

        Item test = item.createFolder("test");
        Item test2 = item.createFolder("test2");

        assertEquals(test.info().getName(), test.getName());
        test2 = test2.move(test.getId());

        items = test.listChildren();

        boolean found = false;
        for (Item i : items) {
            if (i.getName().equals(test2.getName()))
                found = true;
        }

        assertTrue(found);
        test.delete();

        try {
            // This should be gone off the server now
            test2.delete();
            throw new Exception("subdirectory should have been deleted");
        }
        catch (Exception e) {

        }

        items = new Item().list();
        for (Item i : items) {
            if (i.getName().equals("baconbrats.jpg")) {
                /* InputStream is = */Requestor.instance().getItemStream(i);
            }
        }

    }

    @Test
    public void testMessages() throws Exception {
        Message msg = new Message();
        /* List<Message> msgs = */msg.list();
    }

    @Test
    public void testUser() throws Exception {
        User user = new User();
        user.info();
        user.acctoken();
        user.friends();
    }

    @Test
    public void testTransfer() throws Exception {
        Transfer t = new Transfer();
        URL url = new URL();
        List<URL> urls = url.extractUrls("http://releases.ubuntu.com/9.10/ubuntu-9.10-desktop-amd64.iso.torrent");
        URLList list = url.analyze(urls);

        List<Transfer> xfers = t.add(Arrays.asList(list.getTorrent()));
        xfers = t.list();

        for (Transfer xfer : xfers) {
            if (xfer.getName().equals("ubuntu-9.10-desktop-amd64.iso"))
                return;
        }

        throw new Exception("Did not add transfer");
    }

    @Test
    public void testURL() throws Exception {
        String s = "http://releases.ubuntu.com/9.10/ubuntu-9.10-desktop-amd64.iso.torrent";
        URL url = new URL();
        List<URL> urls = url.extractUrls(s);
        URLList urllist = url.analyze(urls);
        assertNotNull(urllist.getTorrent());
        assertEquals(urllist.getTorrent()[0].getUrl(), s);
    }

    @Test
    public void testSubscriptions() throws Exception {
        Subscription sub = new Subscription();
        List<Subscription> subs = sub.list();

        sub = subs.get(0);
        sub.setName("foo");
        sub.edit();

        Subscription sub2 = new Subscription();
        sub2.setId(sub.getId());
        sub2.info();

        assertEquals(sub.getName(), sub2.getName());

    }

    @Test
    public void testFriends() throws Exception {
        User u = new User();
        List<User> friends = u.friends();
        for (User friend : friends) {
            System.out.println(friend.getName());
        }
    }
}
