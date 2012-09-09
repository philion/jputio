libputio ver 0.1

https://sourceforge.net/projects/libputio/

Copyright (c) 2010 Mark Manes <markmanes@gmail.com>

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.

---

LibPutIo provides a Java interface to the http://put.io online storage
services.

This API is designed to be a simple and feature complete interface for 
accessing the put.io web services.  Because of this,  it is not designed 
for extensibility.

For a detailed description of all API methods, please refer to
https://www.put.io/service/api/server

* Usage

All requests through the API methods are passed through the Requestor
object, which is a singleton, but allows for multiple sets of credentials
to be set on a per-thread basis.  These credentials must be set on the
current thread before accessing any API methods.

A simple example to get you started is:

import putio.*;

Requestor.setThreadCredentials("<your_api_key>", "<your_api_secret>");
User u = new User().info();

------

When User.info() returns, it will be populated with all of the information
provided by put.io for the logged in user.

* Support

Please refer to http://sourceforge.net/projects/libputio/support for help
in using this API.


