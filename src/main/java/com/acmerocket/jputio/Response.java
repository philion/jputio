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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;

/**
 * Encapsulates the raw response from a request.
 * 
 * @author Mark Manes
 */
public class Response implements ObjectFactory {
    private int total;
    private List<Map<String, ?>> results;
    private Boolean error;
    private String errorMessage;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Map<String, ?>> getResults() {
        return results;
    }

    public void setResults(List<Map<String, ?>> results) {
        this.results = results;
    }

    public Boolean isError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        if (value instanceof Map && (targetClass == Response.class || targetClass == null)) {
            Map<String, ?> map = (Map<String, ?>) value;
            Response r = new Response();
            r.error = (Boolean) map.get("error");
            r.errorMessage = (String) map.get("error_message");

            Object oResponse = map.get("response");
            if (oResponse instanceof Map) {
                Map<String, ?> response = (Map<String, ?>) oResponse;
                if (response.containsKey("total"))
                    r.total = (Integer) response.get("total");
                else
                    r.total = 0;

                Object oResults = response.get("results");
                if (oResults instanceof List) {
                    r.results = (List<Map<String, ?>>) oResults;
                }
                else {
                    List<Map<String, ?>> results = new ArrayList<Map<String, ?>>(1);
                    results.add((Map<String, ?>) response.get("results"));
                    r.results = results;
                }
            }

            return r;
        }
        return null;
    }
}
