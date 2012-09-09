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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

/**
 * Performs requests against the put.io web service. This class can be used
 * across multiple threads provided each thread invokes
 * {@link #setThreadCredentials(String, String)}.
 * 
 * @author Mark Manes
 */
public class Requestor {
    private final ThreadLocal<String> api_key = new ThreadLocal<String>();
    private final ThreadLocal<String> api_secret = new ThreadLocal<String>();
    private final JSONSerializer json;
    private static Requestor instance;

    /**
     * Returns the singleton instance.
     */
    public static Requestor instance() {
        if (instance == null) {
            synchronized (Requestor.class) {
                if (instance == null)
                    instance = new Requestor();
            }
        }
        return instance;
    }

    private Requestor() {
        json = new JSONSerializer();

        // Prevent unneeded JSON elements from being serialized
        json.exclude("*.class");
        json.include("api_key");
        json.include("api_secret");
        json.include("params");
    }

    /**
     * Sets the put.io API credentials to use for this thread. Each API must
     * invoke this method before invoking any requests.
     * 
     * @param api_key
     *            API key
     * @param api_secret
     *            API secret
     */
    public void setThreadCredentials(String api_key, String api_secret) {
        this.api_key.set(api_key);
        this.api_secret.set(api_secret);
    }

    /**
     * Requests the Item from the server.
     * 
     * @param i
     *            Item to retrieve
     * @return InputStream
     */
    public InputStream getItemStream(Item i) throws Exception {
        if (i.isDir())
            throw new Exception("Item must be a file");

        String token = new User().acctoken();

        URL url = new URL(i.getStreamUrl() + "/atk/" + token);
        URLConnection conn = url.openConnection();
        return conn.getInputStream();
    }

    /**
     * Performs a request against the put.io server.
     * 
     * @param req
     *            Request object to send
     * @return {@link Response} returned from server
     */
    Response makeRequest(Request req) throws Exception {
        if (api_key.get() == null || api_secret.get() == null)
            throw new Exception("Credentials not set on this thread");

        URL url = new URL("http://api.put.io/v1/" + req.getPath());
        URLConnection conn = url.openConnection();

        conn.setRequestProperty("Accept", "application/json");
        conn.setDoOutput(true);

        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        String json = new RequestSerializer(req).toString();
        System.out.println(json);

        wr.write("request=" + json);
        wr.flush();

        StringBuffer sb = new StringBuffer();
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line;

        while ((line = rd.readLine()) != null)
            sb.append(line);

        wr.close();
        rd.close();

        json = sb.toString();
        System.out.println(json);

        Response response = new Response();

        return new JSONDeserializer<Response>().use((String) null, response).deserialize(json);
    }

    /**
     * Simple encapsulation of the API credentials to serialize as a JSON
     * object.
     */
    public class RequestSerializer {
        private String api_key;
        private String api_secret;
        private Map<String, Object> params;

        RequestSerializer(Request req) {
            this.api_key = Requestor.this.api_key.get();
            this.api_secret = Requestor.this.api_secret.get();
            this.params = req.getParams();
        }

        @Override
        public String toString() {
            return json.deepSerialize(this);
        }

        public String getApi_key() {
            return api_key;
        }

        public String getApi_secret() {
            return api_secret;
        }

        public Map<String, Object> getParams() {
            return params;
        }

    }

}
