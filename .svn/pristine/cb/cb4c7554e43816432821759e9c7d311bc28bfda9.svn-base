package speech;

import java.util.List;

import uk.ac.bath.audio.AudioPeakMonitor;
import uk.ac.bath.audio.FFTWorker;
import uk.org.toot.audio.core.AudioBuffer;
import uk.org.toot.audio.server.AudioClient;
import uk.org.toot.audio.server.IOAudioProcess;
import uk.org.toot.audio.server.JavaSoundAudioServer;
import uk.org.toot.audio.server.MultiIOJavaSoundAudioServer;

// PJL add channel to constructor
// now removing it

public class JR_AudioProcess {

	private static JavaSoundAudioServer audioServer;
	private static AudioBuffer chunk;
	private static AudioClient audioClient;
	private static AudioPeakMonitor peakIn = new AudioPeakMonitor();
	public static double spectrum[];
	public static double values[][] = new double[1024][2];
	public PJL_NNClient client;

	AudioBuffer myChunk;
	final FFTWorker fft;
	final boolean doHanning = true;

	final double pre_fft[];
	final double post_fft[];
	final double magn[];
	final float Fs;
	final int fftsize;
	int ptr = 0;

	public JR_AudioProcess(int fftsize_in, float Fs_in) {
		Fs = Fs_in;
		fftsize = fftsize_in;
		fft = new FFTWorker(Fs, doHanning);
		fft.resize(fftsize);
		pre_fft = new double[fftsize];
		post_fft = new double[2 * fftsize];
		magn = new double[fftsize];
		ptr = 0;
	}

	public double[][] getValues() {

		for (int i = 0; i < spectrum.length; i++) {
			values[i][0] = spectrum[i];
		}
		values[0][1] = 2 * peakIn.getPeak();
		return values;
	}

	public void startAudio(String inName1, String outName1,
			final int onscreen_bins, final PJL_NNClient client)
			throws Exception {

		final FFTWorker fft = new FFTWorker(Fs, true);
		fft.resize(fftsize);
		audioServer = new MultiIOJavaSoundAudioServer();

		List<String> outputs = audioServer.getAvailableOutputNames();
		System.out.println("Outputs:");
		for (String name : outputs) {
			System.out.println(name);
		}

		List<String> inputs = audioServer.getAvailableInputNames();
		System.out.println("Inputs:");
		for (String name : inputs) {
			System.out.println(name);
		}

		String inName = outputs.get(0);
		String outName = outputs.get(0);

		if (inName1 != null) {
			for (String name : outputs) {
				if (name.equals(outName1)) {
					outName = outName1;
					break;
				}
			}
		}

		if (outName1 != null) {
			for (String name : inputs) {
				if (name.equals(inName1)) {
					inName = inName1;
					break;
				}
			}
		}

		System.out.println(" Outputs " + outName);
		System.out.println(" Inputs " + inName);

		final IOAudioProcess output = audioServer.openAudioOutput(outName,
				"output");
		final IOAudioProcess input = audioServer
				.openAudioInput(inName, "input");
		chunk = audioServer.createAudioBuffer("default");
		chunk.setRealTime(true);

		audioClient = new AudioClient() {
			public void work(int arg0) {
				chunk.makeSilence();
				input.processAudio(chunk);
				output.processAudio(chunk);
				peakIn.processAudio(chunk);
				spectrum = processAudio(chunk, fftsize, (float) Fs);
				if (client != null)
					client.process();
			}

			public void setEnabled(boolean arg0) {
			}
		};
		audioServer.setClient(audioClient);
		audioServer.start();
		
		// shutdown hook to close the audio devices.
		Runtime.getRuntime().addShutdownHook(new Thread() {

			@Override
			public void run() {

				System.out.println("Stop...");
				audioServer.stop();
				try {
					audioServer.closeAudioInput(input);
					audioServer.closeAudioOutput(output);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	public double[] processAudio(AudioBuffer chunk, int fftsize, float Fs) {

		// PJL: Mix left and right so we don't need to worry about which channel
		// is
		// being used.

		float chn0[] = chunk.getChannel(0);
		float chn1[] = chunk.getChannel(1);

		for (int i = 0; i < chn0.length; i++) {
			pre_fft[ptr] = chn0[i] + chn1[i];
			ptr++;
			if (ptr == pre_fft.length) {
				fft.process(pre_fft, post_fft);
				for (int j = 0; j < pre_fft.length; j++) {
					double real = post_fft[2 * j];
					double imag = post_fft[2 * j + 1];
					magn[j] = (float) Math.sqrt((real * real) + (imag * imag));

				}
				ptr = 0;
			}
		}
		return magn;
	}

}