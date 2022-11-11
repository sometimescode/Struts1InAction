/*
 * $Header: /cvsroot/struts/logon/WEB-INF/src/java/app/LogonAction.java,v 1.1.1.1 2002/08/15 15:50:55 thusted Exp $
 * $Revision: 1.1.1.1 $
 * $Date: 2002/08/15 15:50:55 $
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


package app;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionServlet;


/**
 * Implementation of <strong>Action</strong> that validates a user logon.
 *
 * @author Craig R. McClanahan
 * @author Ted Husted
 * @version $Revision: 1.1.1.1 $ $Date: 2002/08/15 15:50:55 $
 */
public final class LogonAction extends Action {


    /**
     * Validate credentials with business tier.
     *
     * @param username The username credential
     * @param password The password credential
     * @returns true if credentials can be validated
     * @exception UserDirectoryException if cannot access directory
     */
    public boolean isUserLogon(String username,
        String password) throws UserDirectoryException {

        return (UserDirectory.getInstance().isValidPassword(username,password));
        // return true;

    }


    /**
     * Login the user.
     * The event is logged if the debug level is >= Constants.DEBUG.
     *
     * @param mapping The ActionMapping used to select this instance
     * @param actionForm The ActionForm bean for this request (if any)
     * @param request The HTTP request we are processing
     * @param response The HTTP response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet exception occurs
     */
    public ActionForward perform(ActionMapping mapping,
            ActionForm form,
            HttpServletRequest request,
            HttpServletResponse response)
        throws IOException, ServletException {

        // Obtain username and password from web tier
        String username = ((LogonForm) form).getUsername();
        String password = ((LogonForm) form).getPassword();

        // Validate credentials with business tier
        boolean validated = false;
        try {

            validated = isUserLogon(username,password);
        }

        catch (UserDirectoryException ude) {
            // couldn't connect to user directory
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.logon.connect"));
            saveErrors(request,errors);
            // return to input page
            return (new ActionForward(mapping.getInput()));
        }

        if (!validated) {
            // credentials don't match
            ActionErrors errors = new ActionErrors();
            errors.add(ActionErrors.GLOBAL_ERROR,
            new ActionError("error.logon.invalid"));
            saveErrors(request,errors);
            // return to input page
            return (new ActionForward(mapping.getInput()));
        }

        // Save our logged-in user in the session,
        // because we use it again later.
        HttpSession session = request.getSession();
        session.setAttribute(Constants.USER_KEY, form);

        // Log this event, if appropriate
        if (servlet.getDebug() >= Constants.DEBUG) {
            StringBuffer message =
                new StringBuffer("LogonAction: User '");
            message.append(username);
            message.append("' logged on in session ");
            message.append(session.getId());
            servlet.log(message.toString());
        }

        // Return success
        return (mapping.findForward(Constants.SUCCESS));

    }

} // End LogonAction
