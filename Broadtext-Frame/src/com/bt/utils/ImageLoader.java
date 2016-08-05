package com.bt.utils;

import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

public class ImageLoader {

	public Map<String, SoftReference<Drawable>> imageCache = new HashMap<String, SoftReference<Drawable>>();

	private ExecutorService executorService = Executors.newFixedThreadPool(5);
	private final Handler handler = new Handler();
	private boolean isActive = true;

	public Drawable loadDrawable(final String imageUrl, final ImageCallback callback) {
		if (imageCache.containsKey(imageUrl)) {
			SoftReference<Drawable> softReference = imageCache.get(imageUrl);
			if (softReference.get() != null) {
				return softReference.get();
			}
		}
		executorService.submit(new Runnable() {
			public void run() {
				try {
					final Drawable drawable = loadImageFromUrl(imageUrl);

					imageCache.put(imageUrl, new SoftReference<Drawable>(drawable));

					handler.post(new Runnable() {
						public void run() {
							callback.imageLoaded(drawable);
						}
					});
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		});
		return null;
	}

	protected Drawable loadImageFromUrl(String imageUrl) {
		try {
			URL url = new URL(imageUrl);
			InputStream is = url.openStream();
			Drawable drawable = Drawable.createFromStream(is, "drawable.png");
			// SystemClock.sleep(2000);
			return drawable;

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public interface ImageCallback {
		public void imageLoaded(Drawable imageDrawable);
	}

	public void loadImage(final String url, final ProgressBar progressBar, final ImageView imageView) {
		Drawable cacheImage = loadDrawable(url, new ImageLoader.ImageCallback() {
			public void imageLoaded(Drawable imageDrawable) {
				if (isActive) {
					imageView.setImageDrawable(imageDrawable);
					progressBar.setVisibility(View.GONE);
					imageView.setVisibility(View.VISIBLE);
				}
			}
		});
		if (cacheImage != null) {
			if (isActive) {
				imageView.setImageDrawable(cacheImage);
				progressBar.setVisibility(View.GONE);
				imageView.setVisibility(View.VISIBLE);
			}
		}
	}

	public void loadImage(final String url, final ImageView imageView) {
		Drawable cacheImage = loadDrawable(url, new ImageLoader.ImageCallback() {
			public void imageLoaded(Drawable imageDrawable) {
				if (isActive) {
					imageView.setImageDrawable(imageDrawable);
					imageView.setVisibility(View.VISIBLE);
				}
			}
		});
		if (cacheImage != null) {
			if (isActive) {
				imageView.setImageDrawable(cacheImage);
				imageView.setVisibility(View.VISIBLE);
			}
		}
	}

	public void loadImage(final String url, final ProgressBar progressBar, final ImageSwitcher imageSwitcher) {
		Drawable cacheImage = loadDrawable(url, new ImageLoader.ImageCallback() {
			public void imageLoaded(Drawable imageDrawable) {
				if (isActive) {
					imageSwitcher.setImageDrawable(imageDrawable);
					progressBar.setVisibility(View.GONE);
					imageSwitcher.setVisibility(View.VISIBLE);
				}
			}
		});
		if (cacheImage != null) {
			if (isActive) {
				imageSwitcher.setImageDrawable(cacheImage);
				progressBar.setVisibility(View.GONE);
				imageSwitcher.setVisibility(View.VISIBLE);
			}
		}
	}

	public void loadImage(final String url, final LinearLayout progressBarLayout, final ImageView imageView,
			final LinearLayout imageViewLayout) {
		Drawable cacheImage = loadDrawable(url, new ImageLoader.ImageCallback() {
			public void imageLoaded(Drawable imageDrawable) {
				if (isActive) {
					imageView.setImageDrawable(imageDrawable);
					progressBarLayout.setVisibility(View.GONE);
					imageViewLayout.setVisibility(View.VISIBLE);
				}
			}
		});
		if (cacheImage != null) {
			if (isActive) {
				imageView.setImageDrawable(cacheImage);
				progressBarLayout.setVisibility(View.GONE);
				imageViewLayout.setVisibility(View.VISIBLE);
			}
		}
	}

	public void setIfActive(boolean isActive) {
		this.isActive = isActive;
	}
}
