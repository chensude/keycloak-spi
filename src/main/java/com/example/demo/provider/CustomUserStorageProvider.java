package com.example.demo.provider;

import java.util.*;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.demo.entity.UserEntity;
import com.example.demo.mapper.UserMapper;
import org.jboss.logging.Logger;
import org.keycloak.component.ComponentModel;
import org.keycloak.credential.CredentialInput;
import org.keycloak.credential.CredentialModel;
import org.keycloak.models.GroupModel;
import org.keycloak.models.KeycloakSession;
import org.keycloak.models.RealmModel;
import org.keycloak.models.RoleModel;
import org.keycloak.models.UserCredentialModel;
import org.keycloak.models.UserModel;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="mailto:bbalasub@redhat.com">Bala B</a>
 * @version $Revision: 1 $
 */

public class CustomUserStorageProvider implements ILocalCustomUserStorageProvider {
	private static final Logger logger = Logger.getLogger(CustomUserStorageProvider.class);

	@Autowired
	private UserMapper userMapper;

	protected ComponentModel model;

	protected KeycloakSession kcSession;

	@Override
	public void setModel(ComponentModel model) {
		this.model = model;
	}

	@Override
	public void setSession(KeycloakSession session) {
		this.kcSession = session;
	}

	@Override
	public void preRemove(RealmModel realm) {

	}

	@Override
	public void preRemove(RealmModel realm, GroupModel group) {

	}

	@Override
	public void preRemove(RealmModel realm, RoleModel role) {

	}

	@Override
	public void close() {
	}



	@Override
	public UserModel getUserById(String id, RealmModel realm) {
		logger.info("getUserById: " + id);

		UserEntity userEntity = userMapper.selectById(Integer.valueOf(id));

		if (userEntity==null) {
			logger.info("Could not find user by id: " + id);
			return null;
		}
		return new UserAdapter(kcSession, realm, model,userEntity);
	}

	@Override
	public UserModel getUserByUsername(String username, RealmModel realm) {
		logger.info("getUserByUsername: " + username);
		LambdaQueryWrapper<UserEntity> eq = new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUserName, username);
		List<UserEntity> entities = userMapper.selectList(eq);
		if (entities==null) {
			logger.info("Could not find user by username: " + username);
			return null;
		}

		return new UserAdapter(kcSession, realm, model,entities.get(0));
	}

	/**
	 * Not implemented.
	 */
	@Override
	public UserModel getUserByEmail(String email, RealmModel realm) {
		return null;
	}

	@Override
	public boolean supportsCredentialType(String credentialType) {
		logger.info("supportsCredentialType: " + CredentialModel.PASSWORD.equals(credentialType));
		return CredentialModel.PASSWORD.equals(credentialType);
	}

	@Override
	public boolean isConfiguredFor(RealmModel realm, UserModel user, String credentialType) {
		logger.info("isConfiguredFor: " + (supportsCredentialType(credentialType)));
		return supportsCredentialType(credentialType);
	}

	@Override
	public boolean isValid(RealmModel realm, UserModel user, CredentialInput input) {
		if (!supportsCredentialType(input.getType()) || !(input instanceof UserCredentialModel))
			return false;
		UserCredentialModel cred = (UserCredentialModel) input;

		List<UserEntity> users = userMapper.findByUserNameAndPass(user.getUsername(), cred.getValue());
		if (users == null || users.isEmpty()) {
			logger.info("Authentication failed for the user: " + user.getUsername());
			return false;
		} else {
			return true;
		}
	}

	@Override
	public int getUsersCount(RealmModel realm) {
		long count = userMapper.selectCount(null);
		return (int) count;
	}

	@Override
	public List<UserModel> getUsers(RealmModel realm) {
		return getUsers(realm, -1, -1);
	}

	@Override
	public List<UserModel> getUsers(RealmModel realm, int firstResult, int maxResults) {

		List<UserEntity> all = userMapper.selectList(null);

		List<UserModel> users = new LinkedList<>();
		for (UserEntity entity :all)
			users.add(new UserAdapter(kcSession, realm, model, entity));
		return users;
	}

	@Override
	public List<UserModel> searchForUser(String search, RealmModel realm) {
		return searchForUser(search, realm, -1, -1);
	}

	@Override
	public List<UserModel> searchForUser(String userName, RealmModel realm, int firstResult,
			int maxResults) {
		logger.info("seach user by user name: " + userName);

		LambdaQueryWrapper<UserEntity> eq = new LambdaQueryWrapper<UserEntity>().eq(UserEntity::getUserName, userName);
		List<UserEntity> entities = userMapper.selectList(eq);

		if (entities  == null ) {
			logger.info("No matching username for: " + userName);
			return Collections.emptyList();
		}

		List<UserModel> userList = new ArrayList<UserModel>();

		userList.add(new UserAdapter(kcSession, realm, model,entities.get(0)));

		return userList;
	}

	@Override
	public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm) {
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> searchForUser(Map<String, String> params, RealmModel realm,
			int firstResult, int maxResults) {
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group, int firstResult,
			int maxResults) {
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> getGroupMembers(RealmModel realm, GroupModel group) {
		return Collections.emptyList();
	}

	@Override
	public List<UserModel> searchForUserByUserAttribute(String attrName, String attrValue,
			RealmModel realm) {
		return Collections.emptyList();
	}
}
