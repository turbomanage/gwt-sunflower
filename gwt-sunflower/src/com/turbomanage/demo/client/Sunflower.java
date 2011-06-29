package com.turbomanage.demo.client;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.gen2.client.IntegerSlider;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Copyright 2011 David M. Chandler
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author David M. Chandler (dchandler@turbomanage.com)
 */
public class Sunflower extends Composite implements EntryPoint {

  private static final int SEED_RADIUS = 2;
  private static final int SCALE_FACTOR = 4;
  public static final double PI2 = Math.PI * 2;
  public static final double PHI = (Math.sqrt(5)+1) / 2;
  private Canvas canvas;
  private Context2d front;
  private int numSeeds;
  private int maxSeeds = 1000;
  // max diameter in coordinate space
  private int maxD = (int) (Math.sqrt(maxSeeds) * SCALE_FACTOR * 2 + SEED_RADIUS * 2);
  private VerticalPanel vPanel = new VerticalPanel();
  private IntegerSlider numSeedsSlider = new IntegerSlider(0,maxSeeds);
  private int yc = maxD/2;
  private int xc = maxD/2;

  public void onModuleLoad() {
    RootPanel.get("gwt").add(new Sunflower());
  }
  
  public Sunflower() {
    canvas = Canvas.createIfSupported();
    canvas.setPixelSize(2*maxD, 2*maxD);
    // coordinate space = 1/2 pixel space for better performance
    canvas.setCoordinateSpaceHeight(maxD);
    canvas.setCoordinateSpaceWidth(maxD);
    front = canvas.getContext2d();
    
    initSlider();
    initPanel();
    
    initWidget(vPanel);
  }

  private void initSlider() {
    numSeedsSlider.setNumTicks(0);
    numSeedsSlider.setStepSize(1);
    numSeedsSlider.setNumLabels(10);
    numSeedsSlider.setWidth("100%");
    numSeedsSlider.addValueChangeHandler(new ValueChangeHandler<Double>() {
      @Override
      public void onValueChange(ValueChangeEvent<Double> event) {
        numSeeds = (int) Math.round(event.getValue());
        drawFrame();
      }
    });
  }

  private void initPanel() {
    Grid grid = new Grid(1,3);
    grid.getElement().setId("grid");
    grid.setWidth("100%");
    grid.setWidget(0, 0, new Image("r.png"));
    grid.setWidget(0, 1, new Image("theta.png"));
    grid.setWidget(0, 2, new Image("phi.png"));
    vPanel.add(numSeedsSlider);
    vPanel.add(grid);
    vPanel.add(canvas);
    vPanel.add(new Anchor("Source code", "http://code.google.com/p/gwt-sunflower/"));
  }

  /**
   * Draw the complete figure for the current
   * number of seeds
   */
  private void drawFrame() {
    front.clearRect(0, 0, maxD, maxD);
    for (int i=0; i<numSeeds; i++)
    {
      double theta = i * PI2 / PHI;
      double r = Math.sqrt(i) * SCALE_FACTOR;
      double x = xc + r * Math.cos(theta);
      double y = yc - r * Math.sin(theta);
      drawSeed(x,y);
    }
  }
  
  /**
   * Draw a small circle representing a seed centered at (x,y)
   * 
   * @param x Center of the seed head
   * @param y Center of the seed head
   */
  private void drawSeed(double x, double y) {
    front.beginPath();
    front.setLineWidth(2);
    front.setFillStyle("orange");
    front.setStrokeStyle("orange");
    front.arc(x, y, SEED_RADIUS, 0, PI2);
    front.fill();
    front.closePath();
    front.stroke();
  }
  
}