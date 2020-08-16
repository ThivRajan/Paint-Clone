package com.thivagar.projects.paint;

import javafx.scene.canvas.GraphicsContext;
import java.util.ArrayList;

public class SquiggleCommand extends PaintCommand implements Visitable {

	private ArrayList<Point> points=new ArrayList<Point>();

	public void add(Point p){
		this.points.add(p);
		this.setChanged();
		this.notifyObservers();
	}

	public ArrayList<Point> getPoints(){ return this.points; }

	@Override
	public void execute(GraphicsContext g) {
    	DrawVisitor dVisitor = new DrawVisitor(g);
    	this.accept(dVisitor);
  	}

	public void accept (Visitor visitor) {
   		visitor.visit(this);
 	}
}
