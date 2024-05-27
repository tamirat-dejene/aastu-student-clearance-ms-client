package g3.scms.controllers;

import java.io.File;
import java.io.IOException;

import g3.scms.api.Api;
import g3.scms.model.Request;
import g3.scms.utils.ReqRes;
import g3.scms.utils.Util;
import g3.scms.utils.Views;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.SplitPane;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Author: Group 3
 * AASTU Student Clearance Management System
 */

public class App extends Application {
	private static Scene scene;

	/**
	 * Application entry point
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			launch();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}

	@Override
	public void start(Stage stage) throws IOException {
		AnchorPane homePane = (AnchorPane) Views.loadFXML("/views/landing_page");
		AnchorPane loginPane = (AnchorPane) Views.loadFXML("/views/login_page");
		AnchorPane homePicture = (AnchorPane) Views.loadFXML("/views/home");
		AnchorPane mainAppPane = (AnchorPane) homePane.getChildren()
				.filtered(node -> node.getId() != null && node.getId().equals("mainAppAnchorPane"))
				.get(0);
		AnchorPane adminMainPane = (AnchorPane) Views.loadFXML("/views/admin_pages/mainPage");

		// Check if the user has saved login token
		// If yes we don't wanna show the login page else we proceed to the login page
		File file = new File("src/main/resources/auth.bat");
		if (file.exists()) {
			// check if the token is not tampered with by sending the login request using
			// the saved token
			String token = ReqRes.getAuthenticationString(file);
			try {
				boolean isValidToken = App.checkSessionValidity(token);
				if (isValidToken) {
					// Load the main functionality page
					if (ReqRes.authStringIsAdmin(token)) {
						Views.paintPage(adminMainPane, mainAppPane, 0, 0, 0, 0);
					} else {
						AnchorPane mainPane = (AnchorPane) Views.loadFXML("/views/main_page");
						AnchorPane lap = getLeftAnchorPane(mainPane);
						Views.paintPage(homePicture, lap, 0, 0, 0, 0);
						Views.paintPage(mainPane, mainAppPane, 0, 0, 0, 0);
					}
				} else {
					// Remove the current invalid token file and show login pane
					file.delete();
					Views.paintPage(loginPane, mainAppPane, 0, 0, 0, 0);
				}
			} catch (Error e) {
				Views.paintPage(loginPane, mainAppPane, 0, 0, 0, 0);
			}
		} else
			Views.paintPage(loginPane, mainAppPane, 0, 0, 0, 0);

		scene = new Scene(homePane, 1080, 540);

		stage.setScene(scene);
		stage.centerOnScreen();
		stage.setResizable(false);
		stage.setTitle("Student Clearance");
		stage.getIcons().add(new Image(App.class.getResource("/image/aastu-logo.jpg").toString()));

		stage.show();
	}

	/**
	 * 
	 * @param token checks the token validity by sending the request to the server
	 *              by seting Authorization header in the header request
	 * @return true if the token is valid, false otherwise
	 */
	private static boolean checkSessionValidity(String token) {
		// Build the request adding the authorization header
		Request request = new Request();
		request.setBaseUrl(Util.getEnv().getProperty("API_BASE_URL"));
		request.setPath("api/auth/login");
		request.setHeaderMap("Authorization", token);

		// Send get request to the api
		Api api = new Api();
		var response = api.get(request, (err, res) -> {
			if (err != null)
				return null;
			return res;
		});

		if (response == null)
			throw new Error("The server is down!");
		if (response.statusCode() == 200)
			return true;
		if (response.statusCode() == 401)
			return false;
		return false;
	}

	private AnchorPane getLeftAnchorPane(AnchorPane ap) {
		SplitPane pane = (SplitPane) ap.getChildren()
				.filtered(node -> node.getId() != null && node.getId().equals("splitPane")).get(0);
		AnchorPane lap = (AnchorPane) pane.getItems()
				.filtered(node -> node.getId() != null && node.getId().equals("leftAnchorPane")).get(0);
		return lap;
	}
}
