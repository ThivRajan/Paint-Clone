package com.thivagar.projects.paint;
import javafx.scene.canvas.GraphicsContext;

public class CircleCommand extends PaintCommand implements Visitable {
	
	private Point centre;
	private int radius;

	public CircleCommand(Point centre, int radius){
		this.centre = centre;
		this.radius = radius;
	}
	public Point getCentre() { return centre; }
	public void setCentre(Point centre) {
		this.centre = centre;
		this.setChanged();
		this.notifyObservers();
	}
	public int getRadius() { return radius; }
	public void setRadius(int radius) {
		this.radius = radius;
		this.setChanged();
		this.notifyObservers();
	}

	@Override
  	public void execute(GraphicsContext g) {
    	DrawVisitor dVisitor = new DrawVisitor(g);
    	this.accept(dVisitor);
  	}

	public void accept (Visitor visitor) {
   		visitor.visit(this);
 	}
}
