package com.dwp.lbus.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.dwp.lbus.LBUSAppVerticle;
import com.dwp.lbus.data.BaseUser;
import com.dwp.lbus.data.User;

import io.vertx.core.Vertx;
import io.vertx.ext.web.client.WebClient;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;

@ExtendWith(VertxExtension.class)
public class LBUSAppTest {

	@Test
	@DisplayName("Deploy a LBUS service verticle and make request to getUsersByCity")
	void testGetUsersByCity(Vertx vertx, VertxTestContext testContext) {
		WebClient webClient = WebClient.create(vertx);
		Checkpoint deploymentCheckpoint = testContext.checkpoint();
		Checkpoint requestCheckpoint = testContext.checkpoint(1);

		vertx.deployVerticle(new LBUSAppVerticle(), testContext.succeeding(id -> {
			deploymentCheckpoint.flag();
			webClient.get(8080, "localhost", "/city/London/users").send(testContext.succeeding(resp -> {
				testContext.verify(() -> {
					assertThat(resp.statusCode()).isEqualTo(200);
					BaseUser user = new BaseUser();
					user.setId(135);
					user.setFirst_name("Mechelle");
					user.setLast_name("Boam");
					user.setEmail("mboam3q@thetimes.co.uk");
					user.setIp_address("113.71.242.187");
					user.setLatitude(-6.5115909);
					user.setLongitude(105.652983);
					assertThat(resp.statusMessage()).isEqualTo("Success");
					assertThat(resp.bodyAsString()).contains(user.toJson().encodePrettily());
					requestCheckpoint.flag();
				});
			}));
		}));
	}

	@Test
	@DisplayName("Deploy a LBUS service verticle and make request to getUsers")
	void testGetUsers(Vertx vertx, VertxTestContext testContext) {
		WebClient webClient = WebClient.create(vertx);
		Checkpoint deploymentCheckpoint = testContext.checkpoint();
		Checkpoint requestCheckpoint = testContext.checkpoint(1);

		vertx.deployVerticle(new LBUSAppVerticle(), testContext.succeeding(id -> {
			deploymentCheckpoint.flag();
			webClient.get(8080, "localhost", "/users").send(testContext.succeeding(resp -> {
				testContext.verify(() -> {
					assertThat(resp.statusCode()).isEqualTo(200);
					BaseUser user = new BaseUser();
					user.setId(1);
					user.setFirst_name("Maurise");
					user.setLast_name("Shieldon");
					user.setEmail("mshieldon0@squidoo.com");
					user.setIp_address("192.57.232.111");
					user.setLatitude(34.003135);
					user.setLongitude(-117.7228641);
					assertThat(resp.statusMessage()).isEqualTo("Success");
					assertThat(resp.bodyAsString()).contains("Maurise");
					assertThat(resp.bodyAsString()).contains(user.toJson().encodePrettily());
					requestCheckpoint.flag();
				});
			}));
		}));
	}

	@Test
	@DisplayName("Deploy a LBUS service verticle and make request to getUserById")
	void testGetUserById(Vertx vertx, VertxTestContext testContext) {
		WebClient webClient = WebClient.create(vertx);
		Checkpoint deploymentCheckpoint = testContext.checkpoint();
		Checkpoint requestCheckpoint = testContext.checkpoint(1);

		vertx.deployVerticle(new LBUSAppVerticle(), testContext.succeeding(id -> {
			deploymentCheckpoint.flag();
			webClient.get(8080, "localhost", "/user/123").send(testContext.succeeding(resp -> {
				testContext.verify(() -> {
					assertThat(resp.statusCode()).isEqualTo(200);
					User user = new User();
					user.setId(123);
					user.setFirst_name("Anderea");
					user.setLast_name("Highnam");
					user.setEmail("ahighnam3e@wix.com");
					user.setIp_address("194.34.249.139");
					user.setLatitude(40.8321934);
					user.setLongitude(40.0092505);
					user.setCity("Merkezköy");
					assertThat(resp.statusMessage()).isEqualTo("Success");
					assertThat(resp.bodyAsString()).contains(user.toJson().encodePrettily());
					requestCheckpoint.flag();
				});
			}));
		}));
	}

	@Test
	@DisplayName("Deploy a LBUS service verticle and make request to getUserById with invalid user id")
	void testGetUserById_Failure(Vertx vertx, VertxTestContext testContext) {
		WebClient webClient = WebClient.create(vertx);
		Checkpoint deploymentCheckpoint = testContext.checkpoint();
		Checkpoint requestCheckpoint = testContext.checkpoint(1);

		vertx.deployVerticle(new LBUSAppVerticle(), testContext.succeeding(id -> {
			deploymentCheckpoint.flag();
			webClient.get(8080, "localhost", "/user/x").send(testContext.succeeding(resp -> {
				testContext.verify(() -> {
					assertThat(resp.statusCode()).isEqualTo(404);
					assertThat(resp.statusMessage()).isEqualTo("Not Found");
					assertThat(resp.bodyAsString()).contains(
							"{\"message\": \"Id x doesn't exist. You have requested this URI [/user/x] but did you mean /user/<string:id> or /users ?\"}");
					requestCheckpoint.flag();
				});
			}));
		}));
	}

}
