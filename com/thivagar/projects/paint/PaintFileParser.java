package com.thivagar.projects.paint;

import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaintFileParser {
	
	private int lineNumber = 0;
	private String errorMessage ="";
	private PaintModel paintModel;

	private Pattern pFileStart = Pattern.compile("^PaintSaveFileVersion1.0$");
	private Pattern pFileEnd = Pattern.compile("^EndPaintSaveFile$");

	private Pattern pCircleStart = Pattern.compile("^Circle$");
	private Pattern pCenter = Pattern.compile("^center:\\([0-9]+,[0-9]+\\)$");
	private Pattern pRadius = Pattern.compile("^radius:[0-9]+$");
	private Pattern pCircleEnd = Pattern.compile("^EndCircle$");

	private Pattern pRectStart = Pattern.compile("^Rectangle$");
	private Pattern pP1 = Pattern.compile("^p1:\\([0-9]+,[0-9]+\\)$");
	private Pattern pP2 = Pattern.compile("^p2:\\([0-9]+,[0-9]+\\)$");
	private Pattern pRectEnd = Pattern.compile("^EndRectangle$");

	private Pattern pSquiggleStart = Pattern.compile("^Squiggle$");
	private Pattern pPointStart = Pattern.compile("^points$");
	private Pattern pPoint = Pattern.compile("^point:\\([0-9]+,[0-9]+\\)$");
	private Pattern pPointEnd = Pattern.compile("^endpoints$");
	private Pattern pSquiggleEnd = Pattern.compile("^EndSquiggle$");

	private Pattern pColor = Pattern.compile("^color:[1,2]?[0-9]?[0-9],[1,2]?[0-9]?[0-9],[1,2]?[0-9]?[0-9]");
	private Pattern pFill = Pattern.compile("^filled:true$|^filled:false$");

	private void error(String mesg){
		this.errorMessage = "Error in line "+lineNumber+" "+mesg;
	}

	public String getErrorMessage(){
		return this.errorMessage;
	}

	public boolean parse(BufferedReader inputStream, PaintModel paintModel) {
		this.paintModel = paintModel;
		this.errorMessage="";

		CircleCommand circleCommand = null;
		RectangleCommand rectangleCommand = null;
		SquiggleCommand squiggleCommand = null;

		try {
			int state=0; int stateExtra=0;
			Matcher m; String l;
			Color color = null;
			boolean fill = false;

			this.lineNumber=0;
			while ((l = inputStream.readLine()) != null) {
				this.lineNumber++;
				l=l.replace(" ","");
				switch(state){

					case 0:
						m=pFileStart.matcher(l);
						if(m.matches()){
							state=1;
							break;
						}
						error("Expected Start of Paint Save File");
						return false;

					case 1: 
						m=pCircleStart.matcher(l);
						if(m.matches()){
							state=2;
							stateExtra=1;
							break;
						}
						m = pRectStart.matcher(l);
						if (m.matches()){
							state=2;
							stateExtra=2;
							break;
						}
						m = pSquiggleStart.matcher(l);
						if (m.matches()){
							state=2;
							stateExtra=3;
							break;
						}

						m = pFileEnd.matcher(l);
						if (m.matches()) return true;

						error("Expected End of Save File or Start of New Shape");
						return false;

					case 2:
						m = pColor.matcher(l);
						if (m.matches()){
							l = l.replace("color:","");
							String [] rgb = l.split(",");
							color = Color.rgb(Integer.parseInt(rgb[0]),Integer.parseInt(rgb[1]),Integer.parseInt(rgb[2]));

							if ((l = inputStream.readLine()) != null) {
								this.lineNumber++;
								l=l.replace(" ","");
							} else {
								error("Expected Whether Shape Was Filled or Not");
								return false;
							}
						} else {
							error("Expected Color of Shape");
							return false;
						}

						m = pFill.matcher(l);
						if (m.matches()){
							l = l.replace("filled:","");

							if (l.equals("true")) fill = true;
							else fill = false;

							state += stateExtra;
							break;
						} else {
						error("Expected Whether Shape Was Filled or Not");
						return false;
						}

					case 3:
						Point center = null;
						int radius = 0;

						m = pCenter.matcher(l);
						if (m.matches()){
							l = l.replace("center:","");
							l = (l.replace("(","")).replace(")","");
							String [] centerStr = l.split(",");
							center = new Point (Integer.parseInt(centerStr[0]),Integer.parseInt(centerStr[1]));

							if ((l = inputStream.readLine()) != null) {
								this.lineNumber++;
								l=l.replace(" ","");
							} else {
								error("Expected Radius of Circle");
								return false;
							}
						} else {
						error("Expected Center Point of Circle");
						return false;
						}

						m = pRadius.matcher(l);
						if (m.matches()){
							l = l.replace("radius:","");
							radius = Integer.parseInt(l);
							if ((l = inputStream.readLine()) != null) {
								this.lineNumber++;
								l=l.replace(" ","");
							} else {
								error("Expected End of Circle");
								return false;
							}
						} else {
							error("Expected Radius of Circle");
							return false;
						}

						m = pCircleEnd.matcher(l);
						if (m.matches()){
							circleCommand = new CircleCommand(center,radius);
							circleCommand.setColor(color);
							circleCommand.setFill(fill);
							paintModel.addCommand(circleCommand);
							circleCommand = null;
							state = 1;
							break;
						} else {
							error("Expected End of Circle");
							return false;
						}

					case 4:
						Point p1 = null;
						Point p2 = null;

						m = pP1.matcher(l);
						if (m.matches()){
							l = l.replace("p1:","");
							l = (l.replace("(","")).replace(")","");
							String [] pointStr = l.split(",");
							p1 = new Point (Integer.parseInt(pointStr[0]),Integer.parseInt(pointStr[1]));

							if ((l = inputStream.readLine()) != null) {
								this.lineNumber++;
								l=l.replace(" ","");
							} else {
								error("Expected P2 of Rectangle");
								return false;
							}
						} else {
							error("Expected P1 of Rectangle");
							return false;
						}

						m = pP2.matcher(l);
						if (m.matches()){
							l = l.replace("p2:","");
							l = (l.replace("(","")).replace(")","");
							String [] pointStr = l.split(",");
							p2 = new Point (Integer.parseInt(pointStr[0]),Integer.parseInt(pointStr[1]));

							if ((l = inputStream.readLine()) != null) {
								this.lineNumber++;
								l=l.replace(" ","");
							} else {
								error("Expected End of Rectangle");
								return false;
							}
						} else {
							error("Expected P2 of Rectangle");
							return false;
						}

						m = pRectEnd.matcher(l);
						if (m.matches()){
							rectangleCommand = new RectangleCommand(p1,p2);
							rectangleCommand.setColor(color);
							rectangleCommand.setFill(fill);
							paintModel.addCommand(rectangleCommand);
							rectangleCommand = null;
							state = 1;
							break;
						} else {
							error("Expected End of Rectangle");
							return false;
						}

					case 5:
						squiggleCommand = new SquiggleCommand();

						m = pPointStart.matcher(l);
						if (!m.matches()) {
							error("Expected Start of Points");
							return false;
						}

						if ((l = inputStream.readLine()) != null) {
							this.lineNumber++;
							l=l.replace(" ","");

							m = pPoint.matcher(l);
							if (m.matches()){
								l = l.replace("point:","");
								l = (l.replace("(","")).replace(")","");
								String [] pointStr = l.split(",");
								Point point = new Point (Integer.parseInt(pointStr[0]),Integer.parseInt(pointStr[1]));
								squiggleCommand.add(point);
							} else {
								error("Expected Point From Squiggle");
								return false;
							}
						} else {
							error("Expected Point From Squiggle");
							return false;
						}

						while ((l = inputStream.readLine()) != null) {
							this.lineNumber++;
							l=l.replace(" ","");
							m = pPointEnd.matcher(l);
							if (m.matches()) break;

							m = pPoint.matcher(l);
							if (m.matches()){
								l = l.replace("point:","");
								l = (l.replace("(","")).replace(")","");
								String [] pointStr = l.split(",");
								Point point = new Point (Integer.parseInt(pointStr[0]),Integer.parseInt(pointStr[1]));
								squiggleCommand.add(point);
							} else {
								error("Expected Point From Squiggle");
								return false;
							}
						}

						m = pPointEnd.matcher(l);
						if (m.matches()){
							if ((l = inputStream.readLine()) != null) {
								this.lineNumber++;
								l=l.replace(" ","");
							} else {
								error("Expected End of Squiggle");
								return false;
							}
						} else {
							error("Expected End of Points");
							return false;
						}

						m = pSquiggleEnd.matcher(l);
						if (m.matches()){
							state = 1;
							squiggleCommand.setColor(color);
							squiggleCommand.setFill(fill);
							paintModel.addCommand(squiggleCommand);
							break;
						} else {
							error("Expected End of Squiggle");
							return false;
						}
				}
			}
		} catch (Exception e){

		}
		return true;
	}
}
