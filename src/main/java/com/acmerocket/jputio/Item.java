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
 * Represents and manages folders or files stored on the server.  This class initializes
 * to represent the root folder.
 *
 * @author Mark Manes
 *
 */
public class Item implements ObjectFactory
{
    private String name;
    private String downloadUrl;
    private String parentId;
    private String contentType;
    private String fileIconUrl;
    private String streamUrl;
    private String screenshotUrl;
    private Boolean dir;
    private String thumbUrl;
    private String type;
    private String id = "0";
    private Long size;

    /**
     * @return a list of the root level folders and files.
     */
    public List<Item> list() throws Exception
    {
        return list( null );
    }

    /**
     * @return a list of all children of this item.
     * @throws Exception if this item is not a folder
     */
    public List<Item> listChildren() throws Exception
    {
        checkDir();

        return list( id );
    }

    private List<Item> list( String parent ) throws Exception
    {
        Request req = new Request( "files?method=list" );
        Map<String, Object> params = req.getParams();

        if( parent != null )
            params.put( "parent_id", id );

        return bindArray( getResults( req ), Item.class );
    }

    /**
     * Creates a folder under the current item.
     *
     * @param name name of folder to create
     * @return Item representing the new folder
     * @throws Exception if this item is not a folder
     */
    public Item createFolder( String name ) throws Exception
    {
        checkDir();

        Request req = new Request( "files?method=create_dir" );
        Map<String, Object> params = req.getParams();
        params.put( "name", name );
        params.put( "parent_id", id );

        return bind( getResults( req ).get( 0 ), Item.class );
    }

    /**
     * This method is only useful when not initialized to retrieve
     * the root folder properties.
     *
     * @return an Item object representing the current item.
     */
    public Item info() throws Exception
    {
        Request req = new Request( "files?method=info" );
        Map<String, Object> params = req.getParams();
        params.put( "id", id );
        return bind( getResults( req ).get( 0 ), Item.class );

    }

    /**
     * Renames the current item.  The returned Item should be
     * used for all further access to the server item.
     *
     * @param newname The new name of this item
     * @return Item representing the new item on the server
     * @throws Exception
     */
    public Item rename( String newname ) throws Exception
    {
        Request req = new Request( "files?method=rename" );
        Map<String, Object> params = req.getParams();
        params.put( "id", id );
        params.put( "name", newname );

        return bind( getResults( req ).get( 0 ), Item.class );
    }

    /**
     * Moves an item to a new folder.
     *
     * @param newParentId Id of the destination folder
     * @return A new reference to the server item
     */
    public Item move( String newParentId ) throws Exception
    {
        Request req = new Request( "files?method=move" );
        Map<String, Object> params = req.getParams();
        params.put( "id", id );
        params.put( "parent_id", newParentId );

        return bind( getResults( req ).get( 0 ), Item.class );
    }

    /**
     * Deletes the server item referenced by this object.
     */
    public void delete() throws Exception
    {
        Request req = new Request( "files?method=delete" );
        Map<String, Object> params = req.getParams();
        params.put( "id", id );

        getResults( req );
    }

    /**
     * Retrieves a complete map of {@link Dir} objects representing
     * the entire directory tree on the server.
     */
    public Dir dirmap() throws Exception
    {
        Request req = new Request( "files?method=dirmap" );
        return bind( getResults( req ).get( 0 ), Dir.class );
    }

    private void checkDir() throws Exception
    {
        if( dir != null && !dir )
            throw new Exception( "Item is not a folder" );
    }

    @Override
    public Object instantiate( ObjectBinder context, Object value, Type targetType, Class targetClass )
    {
        if( value instanceof Map && targetClass == Item.class )
        {
            Map<String, ?> map = (Map<String, ?>) value;
            Item i = new Item();
            i.name = stringVal( map.get( "name" ) );
            i.contentType = stringVal( map.get( "content_type" ) );
            i.dir = (Boolean) map.get( "is_dir" );
            i.downloadUrl = stringVal( map.get( "download_url" ) );
            i.fileIconUrl = stringVal( map.get( "file_icon_url" ) );
            i.id = stringVal( map.get( "id" ) );
            i.parentId = stringVal( map.get( "parent_id" ) );
            i.screenshotUrl = stringVal( map.get( "screenshot_url" ) );
            i.size = longVal( map.get( "size" ) );
            i.streamUrl = stringVal( map.get( "stream_url" ) );
            i.thumbUrl = stringVal( map.get( "thumb_url" ) );
            i.type = stringVal( map.get( "type" ) );
            return i;
        }
        throw context.cannotConvertValueToTargetType( value, targetClass );
    }

    @Override
    public String toString()
    {
        return "Name: " + name + " Type: " + type + " Id: " + id;
    }

    public String getName()
    {
        return name;
    }

    public String getDownloadUrl()
    {
        return downloadUrl;
    }

    public String getParentId()
    {
        return parentId;
    }

    public String getContentType()
    {
        return contentType;
    }

    public String getFileIconUrl()
    {
        return fileIconUrl;
    }

    public String getStreamUrl()
    {
        return streamUrl;
    }

    public String getScreenshotUrl()
    {
        return screenshotUrl;
    }

    public Boolean isDir()
    {
        return dir;
    }

    public String getThumbUrl()
    {
        return thumbUrl;
    }

    public String getType()
    {
        return type;
    }

    public String getId()
    {
        return id;
    }

    public Long getSize()
    {
        return size;
    }
}
