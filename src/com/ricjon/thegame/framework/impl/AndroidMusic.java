package com.ricjon.thegame.framework.impl;

import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.ricjon.thegame.framework.Music;

public class AndroidMusic implements Music, OnCompletionListener {

	MediaPlayer mediaPlayer;
	boolean isPrepared;
	private final Lock lock;

	public AndroidMusic(AssetFileDescriptor assetDescriptor) {
		mediaPlayer = new MediaPlayer();
        lock = new ReentrantLock();
		try {
			mediaPlayer.setDataSource(assetDescriptor.getFileDescriptor(),
					assetDescriptor.getStartOffset(),
					assetDescriptor.getLength());
			mediaPlayer.prepare();
			isPrepared = true;
			mediaPlayer.setOnCompletionListener(this);
		} catch (Exception e) {
			throw new RuntimeException("Couldn't load music");
		}

	}

	@Override
	public void play() {
        if(mediaPlayer.isPlaying()) {
        	return;
        }
        
        try{
        	lock.lock(); {
        		if(!isPrepared) {
        			mediaPlayer.prepare();
        			mediaPlayer.start();
        		}
				
			}
        } catch (IllegalStateException e) {
        	e.printStackTrace();
        } catch (IOException e) {
        	e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void setLooping(boolean looping) {
		mediaPlayer.setLooping(looping);

	}

	@Override
	public void setVolume(float volume) {
		mediaPlayer.setVolume(volume, volume);

	}

	@Override
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	@Override
	public boolean isStopped() {
		return !isPrepared;
	}

	@Override
	public boolean isLooping() {
		return mediaPlayer.isLooping();
	}

	@Override
	public void dispose() {
		if (mediaPlayer.isPlaying()) {
			mediaPlayer.stop();
		}
		mediaPlayer.release();
	}
	
	public void stop() {
		mediaPlayer.stop();
		lock.lock();
		isPrepared = false;
		lock.unlock();
	}

	@Override
	public void onCompletion(MediaPlayer mp) {
		lock.lock();
		isPrepared = false;
		lock.unlock();
	}

}
