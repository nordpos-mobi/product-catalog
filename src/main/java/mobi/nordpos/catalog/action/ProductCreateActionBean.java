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

import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import mobi.nordpos.catalog.ext.UUIDTypeConverter;
import mobi.nordpos.catalog.model.Product;
import mobi.nordpos.catalog.model.ProductCategory;
import mobi.nordpos.catalog.model.TaxCategory;
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

    private static final String DEFAULT_BARCODE_PREFIX = "200";

    Boolean isTaxInclude;

    @DefaultHandler
    public Resolution form() {
        return new ForwardResolution(PRODUCT_CREATE);
    }

    public Resolution add() {
        Product product = getProduct();
        try {
            product.setTax(readTax(product.getTaxCategory().getId()));
            BigDecimal taxRate = product.getTax().getRate();

            if (getIsTaxInclude() && taxRate != BigDecimal.ZERO) {
                BigDecimal bdTaxRateMultiply = taxRate.add(BigDecimal.ONE);
                product.setPriceSell(product.getPriceSell().divide(bdTaxRateMultiply, MathContext.DECIMAL64));
            }

            getContext().getMessages().add(
                    new SimpleMessage(getLocalizationKey("message.Product.added"),
                            createProduct(product).getName(), product.getProductCategory().getName())
            );
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
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
                field = "reference",
                required = true,
                trim = true),
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

    @ValidationMethod(on = {"add"})
    public void validateProductNameIsUnique(ValidationErrors errors) {
        String name = getProduct().getName();
        if (name != null && !name.isEmpty()) {
            try {
                if (readProduct(Product.NAME, name) != null) {
                    errors.add("product.name", new SimpleError(
                            getLocalizationKey("error.Product.AlreadyExists"), name));
                }
            } catch (SQLException ex) {
                getContext().getValidationErrors().addGlobalError(
                        new SimpleError(ex.getMessage()));
            }
        }
    }

    @ValidationMethod(on = {"add"})
    public void validateProductCodeIsUnique(ValidationErrors errors) {
        String code = getProduct().getCode();
        if (code != null && !code.isEmpty()) {
            try {
                if (readProduct(Product.CODE, code) != null) {
                    errors.add("product.code", new SimpleError(
                            getLocalizationKey("error.Product.AlreadyExists"), code));
                }
            } catch (SQLException ex) {
                getContext().getValidationErrors().addGlobalError(
                        new SimpleError(ex.getMessage()));
            }
        }
    }

    @ValidationMethod(priority = 1)
    public void validateProductBarcode(ValidationErrors errors) {

        String prefix = getBarcodePrefix();

        if (!prefix.matches("\\d\\d\\d")) {
            prefix = DEFAULT_BARCODE_PREFIX;
        }

        try {
            String plu = getPLU(getProduct().getProductCategory().getId());
            String code = getShortCode();
            String barcode = prefix.concat(plu).concat(code);
            getProduct().setCode(barcode.concat(new EAN13CheckDigit().calculate(barcode)));
        } catch (CheckDigitException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
        }
    }

    @ValidationMethod(priority = 1)
    public void validateProductReferency(ValidationErrors errors) {
        try {
            String plu = getPLU(getProduct().getProductCategory().getId());
            String code = getShortCode();
            getProduct().setReference(plu.concat("-").concat(code));
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
        }
    }

    private String getPLU(UUID categoryId) throws SQLException {
        String plu = readProductCategory(categoryId).getCode();
        if (plu != null && !plu.matches("\\d\\d\\d\\d")) {
            while (plu.length() < 4) {
                plu = "0".concat(plu);
            }
            return plu;
        } else {
            return "0000";
        }
    }

    private String getShortCode() {
        Integer listSize = getProduct().getProductCategory().getProductList().size();
        String code = Integer.toString(listSize + 1);
        while (code.length() < 5) {
            code = "0".concat(code);
        }
        return code;
    }

    public List<TaxCategory> getTaxCategoryList() throws SQLException {
        return readTaxCategoryList();
    }

    public Boolean getIsTaxInclude() {
        return isTaxInclude;
    }

    public void setIsTaxInclude(Boolean isTaxInclude) {
        this.isTaxInclude = isTaxInclude;
    }
}
