package com.dwp.lbus.service;

import java.util.List;

import com.dwp.lbus.data.BaseUser;
import com.dwp.lbus.data.User;
import com.dwp.lbus.service.impl.LBUSAppServiceImpl;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;

/**
 * This interface provides various methods to invoke bottom line service to
 * fetch user details
 * 
 * @author Vani Abarna A
 *
 */
public interface LBUSAppService {

	static LBUSAppService create(Vertx vertx) {
		return new LBUSAppServiceImpl(vertx);
	}

	/**
	 * This method used to fetch the users detail based on City from the bottom line
	 * API
	 * 
	 * @param city
	 * @param resultHandler
	 */
	void findUsersByCity(String city, Handler<AsyncResult<List<BaseUser>>> resultHandler);

	/**
	 * This method used to fetch the User details based on the Id from the bottom
	 * line API
	 * 
	 * @param id
	 * @param resultHandler
	 */
	void getUserById(String id, Handler<AsyncResult<User>> resultHandler);

	/**
	 * This method used to fetch all the users from the bottom line API
	 * 
	 * @param resultHandler
	 */
	void getUsers(Handler<AsyncResult<List<BaseUser>>> resultHandler);

}
