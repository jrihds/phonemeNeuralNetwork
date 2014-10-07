package rasmus.fft.spi;

import rasmus.fft.FFTTransformer;

public abstract class FFTProvider {
	
	public class Info {

		public static final int PRIORITY_NATIVE = 40;     // Hardware optimized FFT transform
		public static final int PRIORITY_OPTIMIZED = 30;  // Optimized FFT transformation
		public static final int PRIORITY_FFT = 20;        // Simple FFT transformation
		public static final int PRIORITY_DFT = 10;        // Slow direct DFT transfromation
		public static final int PRIORITY_OPTIONAL = 0;    // Optional provider
		
		public Info(String name, String vendor, String description, String version, int priority)
		{
			this.name = name;
			this.vendor = vendor;
			this.description = description;
			this.version = version;
			this.priority = priority;
		}
		
		private String name;
		private String vendor;
		private String description;
		private String version;
		private int priority;
		
		public String getName() { return name; }
		public String getVendor() { return vendor; }
		public String getDescription() { return description; }
		public String getVersion() { return version; }
		public int getPriority() { return priority; }

	}	
	
	public abstract Info getInfo();
	
	public FFTTransformer getTransformer(int fftsize, int direction, boolean inplace)
	{
		return null;
	}
	public FFTTransformer getTransformer(int[] dims, int direction, boolean inplace)
	{
		if(dims.length == 1) return getTransformer(dims[0], direction, inplace);
		return null;
	}
	
}
