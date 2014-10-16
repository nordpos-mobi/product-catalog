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
import java.sql.SQLException;
import mobi.nordpos.catalog.dao.ormlite.ApplicationPersist;
import mobi.nordpos.catalog.model.Application;

/**
 * @author Andrey Svininykh <svininykh@gmail.com>
 */
public abstract class ApplicationBaseActionBean extends BaseActionBean {

    private Application application;

    public Application getApplication() {
        return application;
    }

    public void setApplication(Application application) {
        this.application = application;
    }

    protected Application readApplication(String id) throws SQLException {
        try {
            connection = new JdbcConnectionSource(getDataBaseURL(), getDataBaseUser(), getDataBasePassword());
            ApplicationPersist applicationDao = new ApplicationPersist(connection);
            return applicationDao.queryForId(id);
        } finally {
            if (connection != null) {
                connection.close();
            }
        }
    }

}
