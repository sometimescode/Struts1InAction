/*
 * $Header: /cvsroot/struts/logon/WEB-INF/src/java/app/UserDirectory.java,v 1.1.1.1 2002/08/15 15:50:56 thusted Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2002/08/15 15:50:56 $
 *
 * ====================================================================
 *
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 1999 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "The Jakarta Project", "Tomcat", and "Apache Software
 *    Foundation" must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */


/**
 * Implementation of <strong>Action</strong> that validates a user
 * logon.
 * <p>
 * :TODO: Javadocs.
 *
 * @todo Javadocs
 * @author George Franciscus
 * @version $Revision: 1.1.1.1 $ $Date: 2002/08/15 15:50:56 $
 */
package app;

import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Properties;

public class UserDirectory  {

    /**
     *
     */
    private static final String UserDirectoryFile =
        "resources/users.properties";


    /**
     *
     */
    private static final String UserDirectoryHeader =
        "${user}=${password}";

    /**
     *
     */
    private static UserDirectory userDirectory = null;


    /**
     *
     */
    private static Properties p;


    /**
     *
     */
    private UserDirectory() throws UserDirectoryException {

            java.io.InputStream i = null;
            p = null;
            i = this.getClass().getClassLoader().
                getResourceAsStream(UserDirectoryFile);


            if (null==i) {
                throw new UserDirectoryException();
            }

            else {

                try {
                    p = new Properties();
                    p.load(i);
                    i.close();
                }

                catch (java.io.IOException e) {
                    p = null;
                    System.out.println(e.getMessage());
                    throw new UserDirectoryException();
                }

                finally {
                    i = null;
                }

            } // end else

    } // end UserDirectory


    /**
     *
     */
    public static UserDirectory getInstance() throws
            UserDirectoryException {

        if (null==userDirectory) {

            userDirectory = new UserDirectory();

        }

        return userDirectory;

    }


    /**
     * Transform id so that it will match any conventions used by user
     * directory. The default implementation forces the id to
     * uppercase. Does <b>not</b> expect the userId to be null and
     * will throw a NPE if it is.
     *
     * @exception Throws Null Pointer Exception if userId is null.
     */
    public String fixId(String userId) {
       return userId.toUpperCase();
    }


    /**
     *
     */
    public boolean isValidPassword(String userId, String password) {

            // no null passwords
        if (null==password) return false;

            // conform userId to uppercase
        String _userId = fixId(userId);

            // no passwords for non-users
        if (!isUserExist(_userId)) return false;

            // does password match user's password
        return (password.equals(getPassword(_userId)));

    }


    /**
     *
     */
    public boolean isUserExist(String userId) {

            // no null users
        if (null==userId) return false;

            // if not null, it's a user
        return !(null==p.getProperty(userId));

    }


    /**
     *
     */
    public String getPassword(String userId)  {
          return p.getProperty(userId);
    }


    /**
     *
     */
    public Enumeration getUserIds()  {
       return p.propertyNames();
    }


    /**
     *
     */
    public void setUser(String userId, String password) throws
            UserDirectoryException {

            // no nulls
        if ((null==userId) || (null==password)) {
            throw new UserDirectoryException();
        }


        try {

                // conform userId to uppercase when stored
            p.put(fixId(userId), password);
            p.store(new FileOutputStream(UserDirectoryFile),
            UserDirectoryHeader);

        }

        catch (IOException e) {

            throw new UserDirectoryException();

       }
    }

} // end UserDirectory