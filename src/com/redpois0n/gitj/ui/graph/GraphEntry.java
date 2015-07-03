package com.redpois0n.gitj.ui.graph;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.redpois0n.git.Commit;

public class GraphEntry {
	
	public static final int BALL_DIAMETER = 8;
	public static final int SPACE = 10;
	
	private String graphData;
	private List<String> additional = new ArrayList<String>();
	private Commit commit;
	
	public GraphEntry(String graphData, Commit c) {
		this.graphData = graphData;
		this.commit = c;
	}
	
	public BufferedImage render(int height) {
		BufferedImage image = new BufferedImage(100, height, BufferedImage.TYPE_INT_ARGB);
		Graphics g = image.createGraphics();
		
		int location = SPACE;
		
		List<String> list = new ArrayList<String>();
		list.addAll(additional);
		list.add(0, graphData);
		
		int depth = 0;
		
		for (String s : list) {
			if (s.length() > depth) {
				depth = s.length();
			}
		}
		
		boolean[] b = new boolean[depth];
		
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
			
			for (int s = 0; s < str.length(); s++) {
				char c = str.charAt(s);
				
				g.setColor(Color.red);
				
				if (c == '*') {
					g.fillOval(location - BALL_DIAMETER / 2, height / 2 - BALL_DIAMETER / 2, BALL_DIAMETER, BALL_DIAMETER);
				}
				
				boolean drawn = c == '*' || c == '|' || c == '/' || c == '\\';

				if (!b[s]) {
					if (c == '*' || c == '|') {
						g.drawLine(location, 0, location, height);
					} else if (c == '/') {
						g.drawLine(location, height / 2, location - SPACE, height);
					} else if (c == '\\') {
						g.drawLine(location - SPACE, height / 2, location, height);
					}

					b[s] = true;
				}
				
				if (drawn) {
					location += SPACE;
				}
			}
			
			location = SPACE;
		}
		
		for (int i = 0; i < list.size(); i++) {
			String str = list.get(i);
			
			for (int s = 0; s < str.length(); s++) {
				char c = str.charAt(s);
				
				g.setColor(Color.red);
				
				boolean drawn = c == '*' || c == '|' || c == '/' || c == '\\';

				if (c == '*') {
					g.fillOval(location - BALL_DIAMETER / 2, height / 2 - BALL_DIAMETER / 2, BALL_DIAMETER, BALL_DIAMETER);
				}
				
				if (drawn) {
					location += SPACE;
				}
			}
			
			location = SPACE;
		}
		
		return image;
	}
	
	public ImageIcon renderIcon(int height) {
		return new ImageIcon(render(height));
	}
	
	public Commit getCommit() {
		return this.commit;
	}

	public void addData(String graphData) {
		additional.add(graphData);
	}
	
}
