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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.SQLException;
import mobi.nordpos.dao.model.Product;
import mobi.nordpos.catalog.util.ImagePreview;
import mobi.nordpos.dao.ormlite.TaxPersist;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.BigDecimalTypeConverter;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.StringTypeConverter;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class ProductChangeActionBean extends ProductBaseActionBean {

    private static final String PRODUCT_EDIT = "/WEB-INF/jsp/product_edit.jsp";

    private Product currentProduct;
    private FileBean imageFile;
   
    @DefaultHandler
    public Resolution form() throws SQLException {
        return new ForwardResolution(PRODUCT_EDIT);
    }

    public Resolution update() {
        Product product = getProduct();
        BigDecimal taxRate = product.getTax().getRate();
        BigDecimal bdTaxRateMultiply = taxRate.add(BigDecimal.ONE);
        product.setPriceSell(product.getTaxPriceSell().divide(bdTaxRateMultiply, MathContext.DECIMAL64));

        try {
            productPersist.init(getDataBaseConnection());
            if (productPersist.change(product)) {
                getContext().getMessages().add(
                        new SimpleMessage(getLocalizationKey("message.Product.updated"),
                                product.getName()));
            }
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
            return getContext().getSourcePageResolution();
        }
        return new ForwardResolution(ProductViewActionBean.class);
    }

    public Resolution delete() throws SQLException {
        Product product = getProduct();
        try {
            productPersist.init(getDataBaseConnection());
            if (productPersist.delete(product.getId())) {
                getContext().getMessages().add(
                        new SimpleMessage(getLocalizationKey("message.Product.deleted"),
                                product.getName()));
            }
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
            return getContext().getSourcePageResolution();
        }
        return new ForwardResolution(CategoryListActionBean.class);
    }

    @ValidationMethod(on = "update")
    public void validateProductNameIsUnique(ValidationErrors errors) {
        String name = getProduct().getName();
        if (name != null && !name.isEmpty() && !name.equals(getCurrentProduct().getName())) {
            try {
                productPersist.init(getDataBaseConnection());
                if (productPersist.find(Product.NAME, name) != null) {
                    errors.add("product.name", new SimpleError(
                            getLocalizationKey("error.Product.AlreadyExists"), name));
                }
            } catch (SQLException ex) {
                getContext().getValidationErrors().addGlobalError(
                        new SimpleError(ex.getMessage()));
            }
        }
    }

    @ValidationMethod(on = "update")
    public void validateProductCodeIsUnique(ValidationErrors errors) {
        String code = getProduct().getCode();
        if (code != null && !code.isEmpty() && !code.equals(getCurrentProduct().getCode())) {
            try {
                productPersist.init(getDataBaseConnection());
                if (productPersist.find(Product.CODE, code) != null) {
                    errors.add("product.code", new SimpleError(
                            getLocalizationKey("error.Product.AlreadyExists"), code));
                }
            } catch (SQLException ex) {
                getContext().getValidationErrors().addGlobalError(
                        new SimpleError(ex.getMessage()));
            }
        }
    }

    @ValidationMethod(on = "update")
    public void validateProductReferenceIsUnique(ValidationErrors errors) {
        String reference = getProduct().getReference();
        if (reference != null && !reference.isEmpty() && !reference.equals(getCurrentProduct().getReference())) {
            try {
                productPersist.init(getDataBaseConnection());
                if (productPersist.find(Product.REFERENCE, reference) != null) {
                    errors.add("product.reference", new SimpleError(
                            getLocalizationKey("error.Product.AlreadyExists"), reference));
                }
            } catch (SQLException ex) {
                getContext().getValidationErrors().addGlobalError(
                        new SimpleError(ex.getMessage()));
            }
        }
    }

    @ValidationMethod(on = "update")
    public void validateProductImageUpload(ValidationErrors errors) {
        try {
            productPersist.init(getDataBaseConnection());
            if (imageFile != null) {
                if (imageFile.getContentType().startsWith("image")) {
                    try {
                        getProduct().setImage(ImagePreview.createThumbnail(imageFile.getInputStream(), 256));
                    } catch (IOException ex) {
                        errors.add("product.image", new SimpleError(
                                getLocalizationKey("error.FileNotUpload"), imageFile.getFileName()));
                    }
                } else {
                    errors.add("product.image", new SimpleError(
                            getLocalizationKey("error.FileNotImage"), imageFile.getFileName()));
                }
            } else {
                getProduct().setImage(productPersist.read(getProduct().getId()).getImage());
            }
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
        }
    }

    @ValidationMethod(on = "form")
    public void validateProductIdIsAvalaible(ValidationErrors errors) {
        try {
            productPersist.init(getDataBaseConnection());
            taxPersist.init(getDataBaseConnection());
            Product product = productPersist.read(getProduct().getId());
            if (product != null) {
                product.setTax(taxPersist.read(product.getTaxCategory().getId()));
                setProduct(product);
            } else {
                errors.add("product.id", new SimpleError(
                        getLocalizationKey("error.CatalogNotInclude")));
            }
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
        }
    }

    @ValidateNestedProperties({
        @Validate(on = {"form", "update", "delete"},
                field = "id",
                required = true,
                converter = StringTypeConverter.class),
        @Validate(on = {"update"},
                field = "name",
                required = true,
                trim = true,
                maxlength = 255),
        @Validate(on = {"update"},
                field = "code",
                required = true,
                trim = true,
                minlength = 13,
                maxlength = 13,
                mask = "[0-9]+"),
        @Validate(on = {"update"},
                field = "taxPriceSell",
                required = true,
                converter = BigDecimalTypeConverter.class),
        @Validate(on = {"update"},
                field = "priceBuy",
                required = true,
                converter = BigDecimalTypeConverter.class),
        @Validate(on = {"update"},
                field = "productCategory.id",
                required = true,
                converter = StringTypeConverter.class)
    })
    @Override
    public void setProduct(Product product) {
        super.setProduct(product);
    }

    public Product getCurrentProduct() {
        return currentProduct;
    }

    public void setCurrentProduct(Product currentProduct) {
        this.currentProduct = currentProduct;
    }

    public FileBean getImageFile() {
        return imageFile;
    }

    public void setImageFile(FileBean imageFile) {
        this.imageFile = imageFile;
    }

}
