package com.redpois0n.gitj.ui.graph;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import com.redpois0n.git.Commit;
import com.redpois0n.gitj.utils.RenderUtils;

public class GraphEntry {
	
	public static final int BALL_DIAMETER = 12;
	public static final int SPACE = 10;
	
	private GitGraph parent;
	private String graphData;
	private List<String> additional = new ArrayList<String>();
	private Commit commit;
	
	public GraphEntry(GitGraph parent, String graphData, Commit c) {
		this.parent = parent;
		this.graphData = graphData;
		this.commit = c;
	}
	
	public BufferedImage render(int height) {
		BufferedImage image = new BufferedImage(100, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g = image.createGraphics();
		
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
		
		boolean[] occupied = new boolean[depth];
		
		boolean[] dot = new boolean[depth];
		
		for (int i = list.size() - 1; i >= 0; i--) {
			String str = list.get(i);
			
			for (int s = 0; s < str.length(); s++) {
				char c = str.charAt(s);
				
				if (c == '*') {
					dot[s] = true;
				}
			}
		}
		
        g.setStroke(new BasicStroke(2));

		for (int i = list.size() - 1; i >= 0; i--) {
			String str = list.get(i);
			
			for (int s = 0; s < str.length(); s++) {
				char c = str.charAt(s);
				
				g.setColor(Color.red);
				
				if (c == '*') {
					RenderUtils.drawCircle(g, g.getColor(), location - BALL_DIAMETER / 2 + 1, height / 2 - BALL_DIAMETER / 2 + 1, BALL_DIAMETER , BALL_DIAMETER);
				}
				
				boolean drawn = c == '*' || c == '|' || c == '/' || c == '\\';
				
				if (!occupied[s] && drawn) {
					if (c == '*' || c == '|') {	
						GraphEntry next = parent.getNext(this);
						boolean draw = c == '*';
						
						if (!draw && next != null) {
							String parentData = next.graphData;
							
							if (parentData.length() > s && drawn) {
								draw = true;
							}
						} else {
							draw = true;
						}

						if (draw) {
							g.drawLine(location, 0, location, height);
						}
					} else if (c == '/') {
						if (dot[s]) {
							g.drawLine(location, 0, location, height / 2);
							g.drawLine(location, height / 2, location - SPACE, height);
						} else {
							g.drawLine(location, 0, location - SPACE, height / 2);
							g.drawLine(location - SPACE, height / 2, location - SPACE, height);
						}
						
						GraphEntry next = parent.getNext(this);
						
						if (next != null) {
							String parentData = next.graphData;
							
							if (parentData.length() > s && parentData.charAt(s) == '|') {
								g.drawLine(location, 0, location, height);
							}
						}
					} else if (c == '\\') {
						g.drawLine(location - SPACE, height / 2, location, height);
						g.drawLine(location - SPACE, 0, location - SPACE, height / 2);
					}

					occupied[s] = true;
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
