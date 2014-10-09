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
import java.util.List;
import mobi.nordpos.catalog.ext.UUIDTypeConverter;
import mobi.nordpos.catalog.model.Product;
import mobi.nordpos.catalog.model.ProductCategory;
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
import org.apache.commons.validator.routines.checkdigit.CheckDigitException;
import org.apache.commons.validator.routines.checkdigit.EAN13CheckDigit;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class ProductCreateActionBean extends ProductBaseActionBean {

    private static final String PRODUCT_CREATE = "/WEB-INF/jsp/product_create.jsp";

    @DefaultHandler
    public Resolution form() {
        return new ForwardResolution(PRODUCT_CREATE);
    }

    public Resolution add() {
        Product product = getProduct();
        try {
            getContext().getMessages().add(
                    new SimpleMessage(getLocalizationKey("message.Product.added"),
                            createProduct(product).getName(), product.getProductCategory().getName())
            );
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError("{2} {3}", ex.getErrorCode(), ex.getMessage()));
            return getContext().getSourcePageResolution();
        }
        return new ForwardResolution(CategoryProductListActionBean.class)
                .addParameter("category.id", product.getProductCategory().getId());
    }

    @ValidateNestedProperties({
        @Validate(on = {"add"},
                field = "name",
                required = true,
                trim = true,
                maxlength = 255),
        @Validate(on = {"add"},
                field = "code",
                required = true,
                trim = true,
                minlength = 13,
                maxlength = 13,
                mask = "[0-9]+"),
        @Validate(on = {"add"},
                field = "priceSell",
                required = true,
                converter = BigDecimalTypeConverter.class),
        @Validate(on = {"add"},
                field = "priceBuy",
                required = true,
                converter = BigDecimalTypeConverter.class),
        @Validate(field = "productCategory.id",
                required = true,
                converter = UUIDTypeConverter.class)
    })
    @Override
    public void setProduct(Product product) {
        super.setProduct(product);
    }

    @ValidationMethod
    public void validateProductCategoryIdIsAvalaible(ValidationErrors errors) {
        try {
            ProductCategory category = readProductCategory(getProduct().getProductCategory().getId());
            if (category != null) {
                getProduct().setProductCategory(category);
            } else {
                errors.add("product.category.id", new SimpleError(
                        getLocalizationKey("error.CatalogNotInclude")));
            }
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
        }
    }

    @ValidationMethod
    public void validateProductCodeIsUnique(ValidationErrors errors) {
        String codeCreate = getProduct().getCode();
        if (codeCreate != null && !codeCreate.isEmpty()) {
            try {
                if (readProduct(codeCreate) != null) {
                    errors.addGlobalError(new SimpleError(
                            getLocalizationKey("error.Product.AlreadyExists"), codeCreate
                    ));
                }
            } catch (SQLException ex) {
                getContext().getValidationErrors().addGlobalError(
                        new SimpleError(ex.getMessage()));
            }
        }
    }

    public String getGenerateCode() throws SQLException, CheckDigitException {
        String prefix = getBarcodePrefix();

        if (!prefix.matches("\\d\\d\\d")) {
            prefix = "200";
        }

        String plu = getProduct().getProductCategory().getCode();
        while (plu.length() < 4) {
            plu = "0".concat(plu);
        }
        if (!plu.matches("\\d\\d\\d\\d")) {
            plu = "0000";
        }
        List<Product> list = listProductByCodePrefix(prefix.concat(plu));
        String code = Integer.toString(list.size() + 1);
        while (code.length() < 5) {
            code = "0".concat(code);
        }
        String barcode = prefix.concat(plu).concat(code);        
        barcode = barcode.concat(new EAN13CheckDigit().calculate(barcode));
        return barcode;
    }

}
