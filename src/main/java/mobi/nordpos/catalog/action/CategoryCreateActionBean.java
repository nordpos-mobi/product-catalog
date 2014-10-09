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
import mobi.nordpos.catalog.model.ProductCategory;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class CategoryCreateActionBean extends CategoryBaseActionBean {

    private static final String CATEGORY_CREATE = "/WEB-INF/jsp/category_create.jsp";

    @DefaultHandler
    public Resolution form() {
        return new ForwardResolution(CATEGORY_CREATE);
    }

    public Resolution add() {
        ProductCategory category = getCategory();
        try {
            getContext().getMessages().add(
                    new SimpleMessage(getLocalizationKey("message.ProductCategory.added"),
                            createProductCategory(category).getName())
            );
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
            return getContext().getSourcePageResolution();
        }
        return new ForwardResolution(CategoryListActionBean.class);
    }

    @ValidateNestedProperties({
        @Validate(on = "add",
                field = "name",
                required = true,
                trim = true,
                maxlength = 255),
        @Validate(field = "code",
                required = true,
                trim = true,
                maxlength = 4)
    })
    @Override
    public void setCategory(ProductCategory category) {
        super.setCategory(category);
    }

    @ValidationMethod
    public void validateCategoryNameIsUnique(ValidationErrors errors) {
        String name = getCategory().getName();
        if (name != null && !name.isEmpty()) {
            try {
                if (readProductCategory(ProductCategory.NAME, name) != null) {
                    errors.add("category.name", new SimpleError(
                            getLocalizationKey("error.ProductCategory.AlreadyExists"), name));
                }
            } catch (SQLException ex) {
                getContext().getValidationErrors().addGlobalError(
                        new SimpleError(ex.getMessage()));
            }
        }
    }

    @ValidationMethod
    public void validateCategoryCodeIsUnique(ValidationErrors errors) {
        String code = getCategory().getCode();
        if (code != null && !code.isEmpty()) {
            try {
                if (readProductCategory(ProductCategory.CODE, code) != null) {
                    errors.add("category.code", new SimpleError(
                            getLocalizationKey("error.ProductCategory.AlreadyExists"), code));
                }
            } catch (SQLException ex) {
                getContext().getValidationErrors().addGlobalError(
                        new SimpleError(ex.getMessage()));
            }
        }
    }

    public String getGenerateCode() {
        String code = getCategory().getCode();
        while (code.length() < 4) {
            code = "0".concat(code);
        }
        if (code.matches("\\d\\d\\d\\d")) {
            return code;
        } else {
            return "0000";
        }
    }
}
