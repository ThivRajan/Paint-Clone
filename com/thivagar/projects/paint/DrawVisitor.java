package com.thivagar.projects.paint;
import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public class DrawVisitor implements Visitor {
	
	GraphicsContext g;
	
	public DrawVisitor(GraphicsContext g){
		this.g = g;
	}
	
	public void visit(CircleCommand circle){
		int x = circle.getCentre().x;
		int y = circle.getCentre().y;
		int radius = circle.getRadius();
		if(circle.isFill()){
		g.setFill(circle.getColor());
		g.fillOval(x-radius, y-radius, 2*radius, 2*radius);
		} else {
		g.setStroke(circle.getColor());
		g.strokeOval(x-radius, y-radius, 2*radius, 2*radius);
		}
	}
	
	public void visit(RectangleCommand rectangle){
		Point topLeft = rectangle.getTopLeft();
		Point dimensions = rectangle.getDimensions();
		if(rectangle.isFill()){
		g.setFill(rectangle.getColor());
		g.fillRect(topLeft.x, topLeft.y, dimensions.x, dimensions.y);
		} else {
		g.setStroke(rectangle.getColor());
		g.strokeRect(topLeft.x, topLeft.y, dimensions.x, dimensions.y);
		}
	}
	
	public void visit(SquiggleCommand squiggle){
		ArrayList<Point> points = squiggle.getPoints();
		g.setStroke(squiggle.getColor());
		for(int i=0;i<points.size()-1;i++){
		Point p1 = points.get(i);
		Point p2 = points.get(i+1);
		g.strokeLine(p1.x, p1.y, p2.x, p2.y);
		}
	}
}