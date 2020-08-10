
import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class Squash extends Simulation {

	val httpProtocol = http
		.baseUrl("http://192.168.0.37:8080")
		.inferHtmlResources()
		.acceptHeader("image/webp,*/*")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("en-GB,en;q=0.5")
		.userAgentHeader("Mozilla/5.0 (X11; Linux x86_64; rv:68.0) Gecko/20100101 Firefox/68.0")

	val headers_0 = Map(
		"Accept" -> "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8",
		"Upgrade-Insecure-Requests" -> "1")

	val headers_3 = Map(
		"Accept" -> "*/*",
		"X-CSRF-TOKEN" -> "${csrf_token}",
		"X-Requested-With" -> "XMLHttpRequest")

	val headers_5 = Map(
		"Accept" -> "application/json, text/javascript, */*; q=0.01",
		"X-CSRF-TOKEN" -> "${csrf_token}",
		"X-Requested-With" -> "XMLHttpRequest")

	val headers_10 = Map(
		"Accept" -> "application/json, text/javascript, */*; q=0.01",
		"Content-Type" -> "application/json;charset=UTF-8",
		"X-CSRF-TOKEN" -> "${csrf_token}",
		"X-Requested-With" -> "XMLHttpRequest")
		
	val scn = scenario("toto")
		.exec(http("openSquashTM")
                .get("/squash/")
                .headers(headers_0)
                .check(css("meta[name='_csrf']","content").saveAs("csrf_token"))).exitHereIfFailed
            .pause(8)
            .exec(http("loginToTM")
                .post("/squash/login")
                .headers(headers_0)
                .formParam("_csrf", "${csrf_token}")
                .formParam("username", "admin")
                .formParam("password", "admin")).exitHereIfFailed
            .pause(5)
		.exec(http("request_2")
			.get("/squash/requirement-workspace/")
			.headers(headers_0)
			.check(css("meta[name='_csrf']","content").saveAs("csrf_token"))
			.resources(http("request_3")
			.get("/squash/requirement-libraries/1")
			.headers(headers_3)))
		.pause(3)
		.exec(http("request_4")
			.get("/squash/images/throbber-7b9776076d5fceef4993b55c9383dedd.gif")
			.resources(http("request_5")
			.get("/squash/requirement-browser/drives/1/content")
			.headers(headers_5)))
		.pause(1)
		.exec(http("request_6")
			.get("/squash/styles/images/ui-bg_flat_75_aaaaaa_40x100-2a44fbdb7360c60122bcf6dcef0387d8.png")
			.resources(http("request_7")
			.get("/squash/styles/images/ui-bg_highlight-soft_45_2b2b2b_1x100-09c2e12d9cb77c606f3dcf92f4d08b87.png"),
            http("request_8")
			.get("/squash/styles/images/ui-icons_ffffff_256x240-342bc03f6264c75d3f1d7f99e34295b9.png"),
            http("request_9")
			.get("/squash/scripts/ckeditor/skins/moonocolor/icons-e987e4a1de2e53deab4d04745dc07a54.png?t=95e5d83")))
		.pause(4)
		.exec(http("request_10")
			.post("/squash/requirement-browser/drives/1/content/new-requirement")
			.headers(headers_10)
			.body(RawFileBody("Squash/0010_request.json")))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
