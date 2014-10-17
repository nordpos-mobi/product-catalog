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

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.openbravo.pos.sales.TaxesLogic;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import mobi.nordpos.catalog.dao.ormlite.ProductCategoryPersist;
import mobi.nordpos.catalog.dao.ormlite.TaxCategoryPersist;
import mobi.nordpos.catalog.dao.ormlite.TaxPersist;
import mobi.nordpos.catalog.ext.MobileActionBeanContext;
import mobi.nordpos.catalog.model.ProductCategory;
import mobi.nordpos.catalog.model.Tax;
import mobi.nordpos.catalog.model.TaxCategory;
import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.controller.StripesFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public abstract class BaseActionBean implements ActionBean {

    private static final String DB_URL = "db.URL";
    private static final String DB_USER = "db.user";
    private static final String DB_PASSWORD = "db.password";
    private static final String DB_APP = "db.application.id";
    private static final String BARCODE_PREFIX = "barcode.prefix";

    private MobileActionBeanContext context;

    Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    ConnectionSource connection;

    @Override
    public MobileActionBeanContext getContext() {
        return this.context;
    }

    @Override
    public void setContext(ActionBeanContext actionBeanContext) {
        this.context = (MobileActionBeanContext) actionBeanContext;
    }

    public String getDataBaseURL() {
        return getContext().getServletContext().getInitParameter(DB_URL);
    }

    public String getDataBaseUser() {
        return getContext().getServletContext().getInitParameter(DB_USER);
    }

    public String getDataBasePassword() {
        return getContext().getServletContext().getInitParameter(DB_PASSWORD);
    }

    public String getDataBaseApplication() {
        return getContext().getServletContext().getInitParameter(DB_APP);
    }

    public String getBarcodePrefix() {
        return getContext().getServletContext().getInitParameter(BARCODE_PREFIX);
    }

    public String getLocalizationKey(String key) {
        return StripesFilter.getConfiguration().getLocalizationBundleFactory()
                .getFormFieldBundle(getContext().getLocale()).getString(key);
    }

    protected ProductCategory readProductCategory(UUID uuid) throws SQLException {
        try {
            connection = new JdbcConnectionSource(getDataBaseURL(), getDataBaseUser(), getDataBasePassword());
            ProductCategoryPersist productCategoryDao = new ProductCategoryPersist(connection);
            return productCategoryDao.queryForId(uuid);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    protected Tax readTax(String taxCategoryId) throws SQLException {
        try {
            connection = new JdbcConnectionSource(getDataBaseURL(), getDataBaseUser(), getDataBasePassword());
            TaxPersist taxDao = new TaxPersist(connection);
            TaxesLogic taxLogic = new TaxesLogic(taxDao.queryForAll());
            return taxLogic.getTax(taxCategoryId, new Date());
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

    protected List<TaxCategory> readTaxCategoryList() throws SQLException {
        try {
            connection = new JdbcConnectionSource(getDataBaseURL(), getDataBaseUser(), getDataBasePassword());
            TaxCategoryPersist taxCategoryDao = new TaxCategoryPersist(connection);
            return taxCategoryDao.queryForAll();
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}
