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
import java.util.List;
import java.util.Map;

import flexjson.ObjectBinder;
import flexjson.ObjectFactory;
import static com.acmerocket.jputio.Util.*;

/**
 * Represents a user on the put.io server.
 * 
 * @author Mark Manes
 * 
 */
public class User implements ObjectFactory {
    private String name;
    private Integer friendsCount;
    private Long bwAvailLastMonth;
    private Long sharedSpace;
    private Long sharedItems;
    private Long diskQuota;
    private Long diskQuotaAvailable;
    private Long bwQuota;

    /**
     * @return A new User object representing the currently logged on user.
     */
    public User info() throws Exception {
        Request req = new Request("user?method=info");
        List<Map<String, ?>> results = getResults(req);

        if (results.isEmpty())
            return null;

        return bind(results.get(0), User.class);
    }

    /**
     * Retrieves friends of the currently logged on user.
     * 
     * @return List of User objects
     */
    public List<User> friends() throws Exception {
        Request req = new Request("user?method=friends");
        return bindArray(getResults(req), User.class);
    }

    /**
     * Retrieves a new account token from the server.
     * 
     * @return String token to be used for streaming
     */
    public String acctoken() throws Exception {
        Request req = new Request("user?method=acctoken");
        List<Map<String, ?>> results = getResults(req);

        if (results.isEmpty())
            return null;

        return (String) results.get(0).get("token");

    }

    @Override
    public Object instantiate(ObjectBinder context, Object value, Type targetType, Class targetClass) {
        if (value instanceof Map && targetClass == User.class) {
            Map<String, ?> map = (Map<String, ?>) value;
            User u = new User();
            u.name = stringVal(map.get("name"));
            u.friendsCount = intVal(map.get("friendsCount"));
            u.bwAvailLastMonth = longVal(map.get("bw_avail_last_month"));
            u.sharedSpace = longVal(map.get("shared_space"));
            u.sharedItems = longVal(map.get("shared_items"));
            u.diskQuota = longVal(map.get("disk_quota"));
            u.diskQuotaAvailable = longVal(map.get("disk_quota_available"));
            u.bwQuota = longVal(map.get("bw_quota"));
            return u;
        }
        throw context.cannotConvertValueToTargetType(value, targetClass);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFriendsCount() {
        return friendsCount;
    }

    public void setFriendsCount(Integer friendsCount) {
        this.friendsCount = friendsCount;
    }

    public Long getBwAvailLastMonth() {
        return bwAvailLastMonth;
    }

    public void setBwAvailLastMonth(Long bwAvailLastMonth) {
        this.bwAvailLastMonth = bwAvailLastMonth;
    }

    public Long getSharedSpace() {
        return sharedSpace;
    }

    public void setSharedSpace(Long sharedSpace) {
        this.sharedSpace = sharedSpace;
    }

    public Long getSharedItems() {
        return sharedItems;
    }

    public void setSharedItems(Long sharedItems) {
        this.sharedItems = sharedItems;
    }

    public Long getDiskQuota() {
        return diskQuota;
    }

    public void setDiskQuota(Long diskQuota) {
        this.diskQuota = diskQuota;
    }

    public Long getDiskQuotaAvailable() {
        return diskQuotaAvailable;
    }

    public void setDiskQuotaAvailable(Long diskQuotaAvailable) {
        this.diskQuotaAvailable = diskQuotaAvailable;
    }

    public Long getBwQuota() {
        return bwQuota;
    }

    public void setBwQuota(Long bwQuota) {
        this.bwQuota = bwQuota;
    }
}
