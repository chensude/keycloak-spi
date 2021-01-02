package com.example.demo.provider;

import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.storage.UserStorageProviderFactory;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author <a href="mailto:bbalasub@redhat.com">Bala B</a>
 * @version $Revision: 1 $
 */
@MapperScan("com.example.demo")
@SpringBootApplication
public class CustomUserStorageProviderFactory
		implements UserStorageProviderFactory<ILocalCustomUserStorageProvider> {
	private static final Logger logger = Logger.getLogger(CustomUserStorageProviderFactory.class);

	@Override
	public ILocalCustomUserStorageProvider create(KeycloakSession session, ComponentModel model) {
		try {
			ILocalCustomUserStorageProvider provider=new CustomUserStorageProvider();
			provider.setModel(model);
			provider.setSession(session);
			return provider;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getId() {
		return "mysqldb-test";
	}

	@Override
	public String getHelpText() {
		return "MySQL DB User Storage Provider";
	}

	@Override
	public void close() {
		logger.info("<<<<<< Closing factory");

	}
}
