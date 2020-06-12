package com.dwp.lbus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.dwp.lbus.service.LBUSAppService;

import io.vertx.config.ConfigRetriever;
import io.vertx.config.ConfigRetrieverOptions;
import io.vertx.config.ConfigStoreOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Launcher;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.RequestParameters;
import io.vertx.ext.web.api.contract.RouterFactoryOptions;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;

/**
 * Location Based User Service Application
 * 
 * This class loads API specification from json and acts a controller to create
 * handler for each operationId and loads configuration details from config json
 * and starts a HTTP server at specified port and host details
 * 
 * 
 * @author Vani Abarna A
 *
 */
public class LBUSAppVerticle extends AbstractVerticle {

	private static final Logger LOGGER = LoggerFactory.getLogger(LBUSAppVerticle.class);

	private LBUSAppService service;
	private HttpServer server;

	/**
	 * This method used to read the API specification and create a handler for each
	 * operationId and call the respective service methods and set the response in
	 * the routingContext object also start the HTTP Server on the given port and
	 * host details which are retrieved from config JSON
	 * 
	 */
	@Override
	public void start(Promise<Void> promise) {
		LOGGER.info("Reading api definition from app.json..");
		OpenAPI3RouterFactory.create(vertx, "app.json", ar -> {
			this.service = LBUSAppService.create(vertx);
			if (ar.succeeded()) {
				// API Specification loaded with success
				OpenAPI3RouterFactory routerFactory = ar.result();

				// Enable automatic response when ValidationException is thrown
				routerFactory.setOptions(new RouterFactoryOptions().setMountResponseContentTypeHandler(true));

				// Adding handler for each operationId read from API Specification
				routerFactory.addHandlerByOperationId("findUsersByCity", routingContext -> {
					RequestParameters params = routingContext.get("parsedParameters");
					String city = params.pathParameter("city").getString();
					// findUsersByCity business logic
					service.findUsersByCity(city, resultHandler -> {
						if (resultHandler.succeeded()) {
							routingContext.response().setStatusCode(200).setStatusMessage("Success")
									.putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
									.end(new JsonArray(resultHandler.result()).encodePrettily());
						} else {
							routingContext.response().setStatusCode(503).setStatusMessage("Service Unavailable")
									.end(resultHandler.cause().getMessage());
						}
					});
				});
				routerFactory.addHandlerByOperationId("getUserById", routingContext -> {
					RequestParameters params = routingContext.get("parsedParameters");
					String id = params.pathParameter("id").getString();
					// getUserById business logic
					service.getUserById(id, resultHandler -> {
						if (resultHandler.succeeded()) {
							routingContext.response().setStatusCode(200).setStatusMessage("Success")
									.putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
									.end(resultHandler.result().toJson().encodePrettily());
						} else {
							routingContext.response().setStatusCode(404).setStatusMessage("Not Found")
									.end(resultHandler.cause().getMessage());
						}
					});
				});
				routerFactory.addHandlerByOperationId("getUsers", routingContext -> {
					// getUsers business logic
					service.getUsers(resultHandler -> {
						if (resultHandler.succeeded()) {
							routingContext.response().setStatusCode(200).setStatusMessage("Success")
									.putHeader(HttpHeaders.CONTENT_TYPE, "application/json")
									.end(new JsonArray(resultHandler.result()).encodePrettily());
						} else {
							routingContext.response().setStatusCode(503).setStatusMessage("Service Unavailable")
									.end(resultHandler.cause().getMessage());
						}
					});
				});
				Router router = routerFactory.getRouter();
				loadConfiguration(handler -> {
					vertx.getOrCreateContext().config().put("config", handler.result());
					server = vertx.createHttpServer(new HttpServerOptions().setPort(handler.result().getInteger("port"))
							.setHost(handler.result().getString("host")));
					server.requestHandler(router).listen(httpServerAsyncResult -> {
						if (httpServerAsyncResult.succeeded())
							LOGGER.info("LBUSAppController started! Listening on port "
									+ httpServerAsyncResult.result().actualPort());
						else
							LOGGER.error(httpServerAsyncResult.cause().getMessage());
					});
				});
				LOGGER.info(
						"API definition read successfully from app.json and handler are created based on the operation ID..");
				promise.complete();
			} else {
				// Something went wrong during router factory initialization
				Throwable exception = ar.cause();
				// Log exception, fail verticle deployment ... etc
				LOGGER.error(exception.getMessage());
				promise.fail(exception);
			}
		});
	}

	/**
	 * This method used to load the configuration details from config json file and
	 * return the JSON Object
	 * 
	 * @param resultHandler
	 */
	private void loadConfiguration(Handler<AsyncResult<JsonObject>> resultHandler) {
		LOGGER.info("Loading configuration from config.json.");
		ConfigStoreOptions fileStore = new ConfigStoreOptions().setType("file")
				.setConfig(new JsonObject().put("path", "config/config.json"));
		ConfigRetrieverOptions options = new ConfigRetrieverOptions().addStore(fileStore);

		ConfigRetriever retriever = ConfigRetriever.create(vertx, options);

		retriever.getConfig(ar -> {
			if (ar.failed()) {
				LOGGER.error("Failed to read the configuration from config.json.");
			} else {
				JsonObject config = ar.result();
				resultHandler.handle(Future.succeededFuture(config));
			}
		});
	}

	public static void main(String[] args) {
		new Launcher().execute("run", LBUSAppVerticle.class.getName());
	}

}
