package com.dwp.lbus.service.impl;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dwp.lbus.data.BaseUser;
import com.dwp.lbus.data.User;
import com.dwp.lbus.service.LBUSAppService;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;

public class LBUSAppServiceImpl implements LBUSAppService {
	private static final Logger LOGGER = LoggerFactory.getLogger(LBUSAppServiceImpl.class);
	Vertx vertx;
	String serviceHost;

	public LBUSAppServiceImpl(Vertx vertx) {
		this.vertx = vertx;
	}

	@Override
	public void findUsersByCity(String city, Handler<AsyncResult<List<BaseUser>>> resultHandler) {
		setServiceHost();
		WebClient client = WebClient.create(vertx);
		client.getAbs(serviceHost + "/city/" + city + "/users").putHeader("accept", "application/json").send(ar -> {
			if (ar.succeeded()) {
				Stream<JsonArray> stream = Stream.of(ar.result().bodyAsJsonArray());
				List<BaseUser> users = (List<BaseUser>) ar.result().bodyAsJsonArray().getList().stream()
						.map(data -> JsonObject.mapFrom(data)).map(data -> new BaseUser((JsonObject) data))
						.collect(Collectors.toCollection(LinkedList::new));
				LOGGER.info("Successfully retrieved findUsersByCity ::" + ar.result().bodyAsString());
				resultHandler.handle(Future.succeededFuture(users));

			} else {
				resultHandler.handle(Future.failedFuture("Failed to fetch users based on City from the herokuapp.."));
			}
		});
	}

	@Override
	public void getUserById(String id, Handler<AsyncResult<User>> resultHandler) {
		setServiceHost();
		WebClient client = WebClient.create(vertx);
		client.getAbs(serviceHost + "/user/" + id).putHeader("accept", "application/json").send(ar -> {
			if (ar.succeeded()) {
				if (ar.result().statusCode() == 200) {
					User user = ar.result().bodyAsJson(User.class);
					LOGGER.info("Successfully retrieved getUserById ::" + ar.result().bodyAsString());
					resultHandler.handle(Future.succeededFuture(user));
				} else if (ar.result().statusCode() == 404) {
					LOGGER.error("Received error in getUserById ::" + ar.result().bodyAsString());
					resultHandler.handle(Future.failedFuture(ar.result().bodyAsString()));
				}
			} else {
				resultHandler.handle(Future.failedFuture("Failed to return User based on ID from the herokuapp.."));
			}
		});
	}

	@Override
	public void getUsers(Handler<AsyncResult<List<BaseUser>>> resultHandler) {
		setServiceHost();
		WebClient client = WebClient.create(vertx);
		client.getAbs(serviceHost + "/users").putHeader("accept", "application/json").send(ar -> {
			if (ar.succeeded()) {
				List<BaseUser> users = (List<BaseUser>) ar.result().bodyAsJsonArray().getList().stream()
						.map(data -> JsonObject.mapFrom(data)).map(data -> new BaseUser((JsonObject) data))
						.collect(Collectors.toCollection(LinkedList::new));
				LOGGER.info("Successfully retrieved getUsers ::" + ar.result().bodyAsString());
				resultHandler.handle(Future.succeededFuture(users));
			} else {
				resultHandler.handle(Future.failedFuture("Failed to fetch all the users from the herokuapp.."));
			}
		});
	}

	/**
	 * This method used to get the bottom line service host details from config json
	 * and set it to the serviceHost variable
	 * 
	 */
	private void setServiceHost() {
		serviceHost = vertx.getOrCreateContext().config().getJsonObject("config").getString("service_host");
		LOGGER.info("Service Host :: " + serviceHost);
	}

}
