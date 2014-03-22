/*
tollevex - a tetravex like game

Copyright 2014 Jon Tollefson <jon@tollefoto.com>

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.tollefoto.tetravex;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/*
 * A Tile is divided into 4 equal size triangles.  Each triangle has a number and color.
 * The color and number are always paired the same.  The number is drawn centered in it's triangle.
 * 
 */
public class TileView extends View {

	final static int NUMBERTEXTSIZE = 18;

	/* Number */
	Paint mNumberPaint;
	float mNumberWidth, mNumberHeight;
	int mNumberColor = Color.BLACK;
	float mLeftNumberX, mLeftNumberY;
	float mRightNumberX, mRightNumberY;
	float mTopNumberX, mTopNumberY;
	float mBottomNumberX, mBottomNumberY;
	String mLeftNumberLabel, mRightNumberLabel, mTopNumberLabel, mBottomNumberLabel;

	/* Quadrant */
	Paint mQuadPaint;
	int mBorderColor = Color.DKGRAY;
	int mLeftColor, mRightColor, mTopColor, mBottomColor;
	float mCenterX, mCenterY;
	int mTrianglePathStrokeWidth = 3;
	Path mLeftTriangle, mRightTriangle, mTopTriangle, mBottomTriangle;
	float mTopLeftX, mTopLeftY;
	float mBottomLeftX, mBottomLeftY;
	float mTopRightX, mTopRightY;
	float mBottomRightX, mBottomRightY;

	/* Map numbers, 1 to 9, to unique color */
	int mNumberColorMap[] = {Color.BLACK, Color.rgb(11,124,196), Color.rgb(29,217,224), Color.rgb(186,186,186), Color.rgb(51,213,59),
			Color.rgb(251,23,68), Color.rgb(205,43,207), Color.WHITE, Color.rgb(222, 224, 29), Color.rgb(251,121,6)};


	public TileView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	 public TileView(Context context, AttributeSet attrs, TileData td) {
	        super(context, attrs);

	        if(td != null) {
		        mLeftColor = td.mLeft;
		        mLeftNumberLabel = Integer.toString(mLeftColor);
		        mRightColor = td.mRight;
		        mRightNumberLabel = Integer.toString(mRightColor);
		        mTopColor = td.mTop;
		        mTopNumberLabel = Integer.toString(mTopColor);
		        mBottomColor = td.mBottom;
		        mBottomNumberLabel = Integer.toString(mBottomColor);
	        }
	        init();
	    }

	 protected void onDraw(Canvas canvas) {
		 super.onDraw(canvas);

		 // Draw triangles
		 //Left
		 mQuadPaint.setStrokeWidth(mTrianglePathStrokeWidth);
		 mQuadPaint.setColor(mNumberColorMap[mLeftColor]);
		 canvas.drawPath(mLeftTriangle, mQuadPaint);

		 //Right
		 mQuadPaint.setColor(mNumberColorMap[mRightColor]);
		 canvas.drawPath(mRightTriangle, mQuadPaint);

		 //Top
		 mQuadPaint.setColor(mNumberColorMap[mTopColor]);
		 canvas.drawPath(mTopTriangle, mQuadPaint);

		 //Bottom
		 mQuadPaint.setColor(mNumberColorMap[mBottomColor]);
		 canvas.drawPath(mBottomTriangle, mQuadPaint);

		 mQuadPaint.setColor(mBorderColor);
		 canvas.drawLine(mCenterX, mCenterY, mTopLeftX, mTopLeftY, mQuadPaint);
		 canvas.drawLine(mCenterX, mCenterY, mBottomLeftX, mBottomLeftY, mQuadPaint);
		 canvas.drawLine(mTopLeftX, mTopLeftY, mBottomLeftX, mBottomLeftY, mQuadPaint);
		 mQuadPaint.setColor(mBorderColor);
		 canvas.drawLine(mCenterX, mCenterY, mTopRightX, mTopRightY, mQuadPaint);
		 canvas.drawLine(mCenterX, mCenterY, mBottomRightX, mBottomRightY, mQuadPaint);
		 canvas.drawLine(mBottomRightX, mBottomRightY, mTopRightX, mTopRightY, mQuadPaint);
		 mQuadPaint.setColor(mBorderColor);
		 canvas.drawLine(mCenterX, mCenterY, mTopLeftX, mTopLeftY, mQuadPaint);
		 canvas.drawLine(mCenterX, mCenterY, mBottomRightX, mBottomRightY, mQuadPaint);
		 canvas.drawLine(mTopLeftX, mTopLeftY, mTopRightX, mTopRightY, mQuadPaint);
		 mQuadPaint.setColor(mBorderColor);
		 canvas.drawLine(mCenterX, mCenterY, mBottomRightX, mBottomRightY, mQuadPaint);
		 canvas.drawLine(mCenterX, mCenterY, mBottomLeftX, mBottomLeftY, mQuadPaint);
		 canvas.drawLine(mBottomLeftX, mBottomLeftY, mBottomRightX, mBottomRightY, mQuadPaint);

		 
		 //Draw the number in each triangle
		 canvas.drawText(mLeftNumberLabel, mLeftNumberX, mLeftNumberY, mNumberPaint);
		 canvas.drawText(mRightNumberLabel, mRightNumberX, mRightNumberY, mNumberPaint);
		 canvas.drawText(mTopNumberLabel, mTopNumberX, mTopNumberY, mNumberPaint);
		 canvas.drawText(mBottomNumberLabel, mBottomNumberX, mBottomNumberY, mNumberPaint);
	 }

	 protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		 super.onSizeChanged(w, h, oldw, oldh);
		 // Calculate location of number labels
		 mLeftNumberX = (w / 4) - (mNumberWidth / 2);
		 mLeftNumberY = h / 2  + (mNumberHeight / 4);

		 mRightNumberX = (w - (w / 4)) - (mNumberWidth / 2);
		 mRightNumberY = (h / 2) + (mNumberHeight / 4);

		 mTopNumberX = (w / 2) - (mNumberWidth / 2);
		 mTopNumberY = (h / 4) + (mNumberHeight / 4);

		 mBottomNumberX = (w / 2) - (mNumberWidth / 2);
		 mBottomNumberY = (h  - (h / 4)) + (mNumberHeight / 2);
		 
		 mCenterX = w / 2;
		 mCenterY = h / 2;
		 
		 // Calculate key points needed to draw triangles
		 mTopLeftX = 0;
		 mTopLeftY = 0;
		 mBottomLeftX = 0;
		 mBottomLeftY = h;
		 mTopRightX = w;
		 mTopRightY = 0;
		 mBottomRightX = w;
		 mBottomRightY = h;

		 mLeftTriangle = new Path();
		 mLeftTriangle.moveTo(mCenterX, mCenterY);
		 mLeftTriangle.lineTo(mTopLeftX, mTopLeftY);
		 mLeftTriangle.lineTo(mBottomLeftX, mBottomLeftY);
		 mLeftTriangle.close();
		 
		 mRightTriangle = new Path();
		 mRightTriangle.moveTo(mCenterX, mCenterY);
		 mRightTriangle.lineTo(mTopRightX, mTopRightY);
		 mRightTriangle.lineTo(mBottomRightX, mBottomRightY);
		 mRightTriangle.close();

		 mTopTriangle = new Path();
		 mTopTriangle.moveTo(mCenterX, mCenterY);
		 mTopTriangle.lineTo(mTopLeftX, mTopLeftY);
		 mTopTriangle.lineTo(mTopRightX, mTopRightY);
		 mTopTriangle.close();

		 mBottomTriangle = new Path();
		 mBottomTriangle.moveTo(mCenterX, mCenterY);
		 mBottomTriangle.lineTo(mBottomLeftX, mBottomLeftY);
		 mBottomTriangle.lineTo(mBottomRightX, mBottomRightY);
		 mBottomTriangle.close();
	 }

	 private void init() {
		   mNumberPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		   mNumberPaint.setColor(mNumberColor);
		   DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
		   float pixelSize = NUMBERTEXTSIZE * dm.scaledDensity; 
		   mNumberPaint.setTextSize(pixelSize);
		   if (mNumberHeight == 0) {
		       mNumberHeight = mNumberPaint.getTextSize();
		       mNumberWidth = mNumberPaint.measureText(mLeftNumberLabel);
		   } else {
		       mNumberPaint.setTextSize(mNumberHeight);
		   }

		   mQuadPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		   mQuadPaint.setColor(mBorderColor);
	 }

}
