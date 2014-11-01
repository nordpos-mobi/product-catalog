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
import java.sql.SQLException;
import mobi.nordpos.catalog.model.ProductCategory;
import mobi.nordpos.catalog.util.ImagePreview;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.StringTypeConverter;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public class CategoryChangeActionBean extends CategoryBaseActionBean {

    private static final String CATEGORY_EDIT = "/WEB-INF/jsp/category_edit.jsp";

    private String currentCode;
    private String currentName;

    private FileBean imageFile;

    @DefaultHandler
    public Resolution form() throws SQLException {
        return new ForwardResolution(CATEGORY_EDIT);
    }

    public Resolution update() {
        ProductCategory category = getCategory();
        try {
            if (updateProductCategory(category)) {
                getContext().getMessages().add(
                        new SimpleMessage(getLocalizationKey("message.ProductCategory.updated"),
                                category.getName()));
            }
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
            return getContext().getSourcePageResolution();
        }
        return new ForwardResolution(CategoryListActionBean.class);
    }

    public Resolution delete() throws SQLException {
        ProductCategory category = getCategory();
        try {
            if (deleteProductCategory(category.getId())) {
                getContext().getMessages().add(
                        new SimpleMessage(getLocalizationKey("message.ProductCategory.deleted"),
                                category.getName()));
            }
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
            return getContext().getSourcePageResolution();
        }
        return new ForwardResolution(CategoryListActionBean.class);
    }

    @ValidationMethod(on = "delete")
    public void validateProductListIsEmpty(ValidationErrors errors) throws SQLException {
        setCategory(readProductCategory(getCategory().getId()));
        if (!getCategory().getProductCollection().isEmpty()) {
            errors.addGlobalError(new SimpleError(
                    getLocalizationKey("error.ProductCategory.IncludeProducts"), getCategory().getName(), getCategory().getProductCollection().size()
            ));
        }
    }

    @ValidationMethod(on = "update")
    public void validateCategoryNameIsUnique(ValidationErrors errors) {
        String name = getCategory().getName();
        if (name != null && !name.isEmpty() && !name.equals(getCurrentName())) {
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

    @ValidationMethod(on = "update")
    public void validateCategoryCodeIsUnique(ValidationErrors errors) {
        String code = getCategory().getCode();
        if (code != null && !code.isEmpty() && !code.equals(getCurrentCode())) {
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

    @ValidationMethod(on = "update")
    public void validateCategoryImageUpload(ValidationErrors errors) {
        try {
            if (imageFile != null) {
                if (imageFile.getContentType().startsWith("image")) {
                    try {
                        getCategory().setImage(ImagePreview.createThumbnail(imageFile.getInputStream(), 256));
                    } catch (IOException ex) {
                        errors.add("category.image", new SimpleError(
                            getLocalizationKey("error.ProductCategory.FileNotUpload"), imageFile.getFileName()));
                    }
                } else {
                    errors.add("category.image", new SimpleError(
                            getLocalizationKey("error.ProductCategory.FileNotImage"), imageFile.getFileName()));
                }
            } else {
                getCategory().setImage(readProductCategory(getCategory().getId()).getImage());
            }
        } catch (SQLException ex) {
            getContext().getValidationErrors().addGlobalError(
                    new SimpleError(ex.getMessage()));
        }
    }

    @ValidationMethod(on = "form")
    public void validateCategoryIdIsAvalaible(ValidationErrors errors) {
        try {
            ProductCategory category = readProductCategory(getCategory().getId());
            if (category != null) {
                setCategory(category);
            } else {
                errors.add("category.id", new SimpleError(
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
                trim = true,
                maxlength = 4)
    })
    @Override
    public void setCategory(ProductCategory category) {
        super.setCategory(category);
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public String getCurrentCode() {
        return currentCode;
    }

    public void setCurrentCode(String currentCode) {
        this.currentCode = currentCode;
    }

    public FileBean getImageFile() {
        return imageFile;
    }

    public void setImageFile(FileBean imageFile) {
        this.imageFile = imageFile;
    }
}
