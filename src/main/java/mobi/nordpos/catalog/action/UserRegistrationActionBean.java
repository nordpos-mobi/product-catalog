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

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import mobi.nordpos.catalog.ext.Public;
import mobi.nordpos.catalog.model.User;
import mobi.nordpos.catalog.util.Hashcypher;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.BooleanTypeConverter;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
@Public
public class UserRegistrationActionBean extends UserBaseActionBean {

    private static final String REG_FORM = "/WEB-INF/jsp/user_reg.jsp";

    @Validate(required = true, minlength = 5, maxlength = 20, expression = "${'this' == 'this'.user.password}")
    private String confirmPassword;

    @DefaultHandler
    @DontValidate
    public Resolution form() {
        return new ForwardResolution(REG_FORM);
    }

    public Resolution accept() throws UnsupportedEncodingException, NoSuchAlgorithmException {
        User user = getUser();
        try {
            user.setPassword(Hashcypher.hashString(user.getPassword()));
            getContext().getMessages().add(
                    new SimpleMessage(getLocalizationKey("message.User.registered"),
                            createUser(user).getName())
            );
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError("{2} {3}", ex.getErrorCode(), ex.getMessage()));
            return getContext().getSourcePageResolution();
        }
        return new ForwardResolution(PresentationActionBean.class);
    }

    @ValidateNestedProperties({
        @Validate(field = "name",
                required = true,
                minlength = 5,
                maxlength = 20),
        @Validate(field = "password",
                required = true,
                minlength = 5,
                maxlength = 20),
        @Validate(field = "visible",
                required = true,
                converter = BooleanTypeConverter.class),
        @Validate(field = "role",
                required = true)})
    @Override
    public void setUser(User user) {
        super.setUser(user);
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

}
