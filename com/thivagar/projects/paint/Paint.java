package com.thivagar.projects.paint;

import javafx.application.Application;
import javafx.stage.Stage;

public class Paint extends Application {
	
	PaintModel model;
	View view;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.model = new PaintModel();
		this.view = new View(model, stage);
	}
}
