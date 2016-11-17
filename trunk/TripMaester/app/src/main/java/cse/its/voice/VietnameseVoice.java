package cse.its.voice;

import java.io.IOException;
import java.util.ArrayList;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

/**
 * @author SinhHuynh
 * @Tag Simple Vietnamese Text-to-Speech 
 */
public class VietnameseVoice {

	private ArrayList<MediaPlayer> mpList;
	ArrayList<String> textList;
	ArrayList<AssetFileDescriptor> afdList;
	ArrayList<CountDownTimer> cdTimerList;

	public static final int LEFT = 1;
	public static final int RIGHT = 2;
	public static final int AHEAD = 3;

	public VietnameseVoice(Context context, int direction, int number,
			String desStreet) {
		mpList = new ArrayList<MediaPlayer>();
		textList = new ArrayList<String>();

		textList.add("gostraight");
		// add distance
		if (number > 999)
			// if((number/1000)%10 == 1)
			textList.add((number / 1000) % 10 + "t");
		if (number > 99) {
			if (number > 1000 && (number / 100) % 10 == 0)
				textList.add("0h");
			else
				textList.add((number / 100) % 10 + "h");
		}
		if (number > 9)
			textList.add((number / 10) % 10 + "t");
		textList.add("met");
		// add direction
		switch (direction) {
		case LEFT:
			textList.add("turnleft");
			break;
		case AHEAD:
			textList.add("to");
			break;
		case RIGHT:
			textList.add("turnright");
			break;
		}
		if (desStreet != null) {

		}

		for (int i = 0; i < textList.size(); ++i) {
			try {
				AssetFileDescriptor afd = context.getAssets().openFd(
						"audio/" + textList.get(i) + ".mp3");
				MediaPlayer mp = new MediaPlayer();
				mp.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),
						afd.getLength());
				mp.prepare();

				mpList.add(mp);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public void speak() {
		cdTimerList = new ArrayList<CountDownTimer>();
		for (int i = mpList.size() - 1; i >= 0; --i) {
			final int j = i;
			//
			CountDownTimer cdTimer = new CountDownTimer(mpList.get(j)
					.getDuration(), 10) {

				@Override
				public void onTick(long millisUntilFinished) {
					mpList.get(j).start();
					// Log.wtf("Media file", "mpList index: " + j +
					// "  cdTimer index : " + (mpList.size() - 1 -j));
				}

				@Override
				public void onFinish() {
					mpList.get(j).stop();
					if (j < (mpList.size() - 1))
						cdTimerList.get(mpList.size() - 2 - j).start();
				}
			};
			// size-1, size-2,.., 2, 1, 0
			// 0, 1, 2,... size-2, size-1

			cdTimerList.add(cdTimer);

		}
		cdTimerList.get(cdTimerList.size() - 1).start();
	}

}
