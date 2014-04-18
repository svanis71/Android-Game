package com.ricjon.thegame.framework;

import com.ricjon.thegame.framework.Graphics.PixmapFormat;

public interface Pixmap {

	public int getWidth();
	public PixmapFormat getFormat();
	public void dispose();
}
