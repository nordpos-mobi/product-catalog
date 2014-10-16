/**
 * Copyright (c) 2012-2014 Nord Trading Network.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package mobi.nordpos.catalog.action;

import java.sql.SQLException;
import mobi.nordpos.catalog.model.Application;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class ApplicationPresentActionBean extends ApplicationBaseActionBean {

    private static final String PRESENT = "/WEB-INF/jsp/present.jsp";
    private static final String INFO = "/WEB-INF/jsp/info.jsp";

    @DefaultHandler
    public Resolution title() {
        return new ForwardResolution(PRESENT);
    }

    public Resolution info() {
        return new ForwardResolution(INFO);
    }

    public String getCountry() {
        return getContext().getLocale().getDisplayCountry();
    }

    public String getLanguage() {
        return getContext().getLocale().getDisplayLanguage();
    }

    public String getServerInfo() {
        return getContext().getServletContext().getServerInfo();
    }

    public String getJavaVersion() {
        return System.getProperty("java.vendor") + " " + System.getProperty("java.version");
    }

    public String getOperationSystem() {
        return System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch");
    }

    @Override
    public void setApplication(Application application) {
        super.setApplication(application);
    }
    
    @ValidationMethod
    public void validateProductCodeIsAvalaible(ValidationErrors errors) {
        try {
            Application application = readApplication(getDataBaseApplication());
            if (application != null) {
                setApplication(application);
            } else {
                errors.add("application.id", new SimpleError(
                        getLocalizationKey("error.DatabaseNotSupportApplication"), getDataBaseApplication()));
            }
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
        }
    }

}
