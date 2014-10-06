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
import mobi.nordpos.catalog.ext.UUIDTypeConverter;
import mobi.nordpos.catalog.model.Product;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.BigDecimalTypeConverter;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class ProductChangeActionBean extends ProductBaseActionBean {

    private static final String PRODUCT_EDIT = "/WEB-INF/jsp/product_edit.jsp";

    private String codeCurrent;

    @DefaultHandler
    public Resolution form() throws SQLException {
        return new ForwardResolution(PRODUCT_EDIT);
    }

    public Resolution update() {
        Product product = getProduct();
        try {
            if (updateProduct(product)) {
                getContext().getMessages().add(
                        new SimpleMessage(getLocalizationKey("label.message.Product.updated"),
                                product.getName()));
            }
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError("{2} {3}", ex.getErrorCode(), ex.getMessage()));
            return getContext().getSourcePageResolution();
        }
        return new ForwardResolution(CategoryListActionBean.class);
    }

    public Resolution delete() throws SQLException {
        Product product = getProduct();
        try {
            if (deleteProduct(product.getId())) {
                getContext().getMessages().add(
                        new SimpleMessage(getLocalizationKey("label.message.Product.deleted"),
                                product.getName()));
            }
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError("{2} {3}", ex.getErrorCode(), ex.getMessage()));
            return getContext().getSourcePageResolution();
        }
        return new ForwardResolution(CategoryListActionBean.class);
    }

    @ValidationMethod(on = "update")
    public void validateProductCodeIsUnique(ValidationErrors errors) {
        String codeUpdate = getProduct().getCode();
        if (codeUpdate != null && !codeUpdate.isEmpty() && !codeUpdate.equals(getCodeCurrent())) {
            try {
                if (readProduct(codeUpdate) != null) {
                    errors.addGlobalError(new SimpleError(
                            getLocalizationKey("label.error.Product.AlreadyExists"), codeUpdate
                    ));
                }
            } catch (SQLException ex) {
                getContext().getValidationErrors().addGlobalError(
                        new SimpleError(ex.getMessage()));
            }
        }
    }

    @ValidationMethod(on = "form")
    public void validateProductIdIsAvalaible(ValidationErrors errors) {
        try {
            Product product = readProduct(getProduct().getId());
            if (product != null) {
                setProduct(product);
            } else {
                errors.add("product.id", new SimpleError(
                        getLocalizationKey("label.error.CatalogNotInclude")));
            }
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
        }
    }

    @ValidateNestedProperties({
        @Validate(on = {"update", "delete"},
                field = "id",
                required = true,
                converter = UUIDTypeConverter.class),
        @Validate(on = {"update"},
                field = "name",
                required = true,
                trim = true,
                maxlength = 255),
        @Validate(on = {"form", "update"},
                field = "code",
                required = true,
                trim = true,
                maxlength = 13),
        @Validate(on = {"update"},
                field = "priceSell",
                required = true,
                converter = BigDecimalTypeConverter.class),
        @Validate(on = {"update"},
                field = "priceBuy",
                required = true,
                converter = BigDecimalTypeConverter.class),
        @Validate(on = {"update"},
                field = "productCategory.id",
                required = true,
                converter = UUIDTypeConverter.class)
    })
    @Override
    public void setProduct(Product product) {
        super.setProduct(product);
    }

    public String getCodeCurrent() {
        return codeCurrent;
    }

    public void setCodeCurrent(String codeCurrent) {
        this.codeCurrent = codeCurrent;
    }
}
