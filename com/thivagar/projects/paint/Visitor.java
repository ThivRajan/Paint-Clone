package com.thivagar.projects.paint;

public interface Visitor {
	public void visit(CircleCommand circle);
	public void visit(RectangleCommand rectangle);
	public void visit(SquiggleCommand squiggle);
}