package com.thivagar.projects.paint;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.paint.Color;

public class SaveVisitor implements Visitor{

	private PrintWriter writer;
	
	public SaveVisitor (PrintWriter writer) {
		this.writer = writer;
	}
	
	public void visit(CircleCommand circle) {
		Color color = circle.getColor();
		writer.println("Circle");
		
		writer.println("color:"+(int)(color.getRed()*256)+","+(int)(color.getGreen()*256)+","+(int)(color.getBlue()*256));
		writer.println("filled:"+circle.isFill());
		
		writer.println("center:"+"("+circle.getCentre().getX()+","+circle.getCentre().getY()+")");
		writer.println("radius:"+circle.getRadius());
		
		writer.println("EndCircle");
	}
	
	public void visit(RectangleCommand rectangle) {
		Color color = rectangle.getColor();
		writer.println("Rectangle");
		
		writer.println("color:"+(int)(color.getRed()*256)+","+(int)(color.getGreen()*256)+","+(int)(color.getBlue()*256));
		writer.println("filled:"+rectangle.isFill());
		
		writer.println("p1:"+"("+rectangle.getP1().getX()+","+rectangle.getP1().getY()+")");
		writer.println("p2:"+"("+rectangle.getP2().getX()+","+rectangle.getP2().getY()+")");
		
		writer.println("EndRectangle");
	}
	
	public void visit(SquiggleCommand squiggle) {
		Color color = squiggle.getColor();
		writer.println("Squiggle");
		
		writer.println("color:"+(int)(color.getRed()*256)+","+(int)(color.getGreen()*256)+","+(int)(color.getBlue()*256));
		writer.println("filled:"+squiggle.isFill());
		
		writer.println("points");
		for (Point p: squiggle.getPoints()){
			writer.println("point:"+"("+p.getX()+","+p.getY()+")");
		}
		writer.println("endpoints");
		
		writer.println("EndSquiggle");
	}
}