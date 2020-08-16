package com.thivagar.projects.paint;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import javafx.scene.paint.Color;

import javafx.scene.canvas.GraphicsContext;

public class PaintModel extends Observable implements Observer {
    public void save(PrintWriter writer) {
		writer.println("Paint Save File Version 1.0");

		SaveVisitor sVisitor = new SaveVisitor(writer);
		for(PaintCommand c: this.commands){
			if (c instanceof CircleCommand)((CircleCommand)c).accept(sVisitor);
			else if (c instanceof RectangleCommand) ((RectangleCommand)c).accept(sVisitor);
			else ((SquiggleCommand)c).accept(sVisitor);
		}

		writer.println("EndPaintSaveFile");
		writer.close();
    }

	public void reset(){
		for(PaintCommand c: this.commands){
			c.deleteObserver(this);
		}
		this.commands.clear();
		this.setChanged();
		this.notifyObservers();
	}

	public void addCommand(PaintCommand command){
		this.commands.add(command);
		command.addObserver(this);
		this.setChanged();
		this.notifyObservers();
	}

	private ArrayList<PaintCommand> commands = new ArrayList<PaintCommand>();

	public void executeAll(GraphicsContext g) {
		for(PaintCommand c: this.commands){
			c.execute(g);
		}
	}

	@Override
	public void update(Observable o, Object arg) {
		this.setChanged();
		this.notifyObservers();
	}
}
