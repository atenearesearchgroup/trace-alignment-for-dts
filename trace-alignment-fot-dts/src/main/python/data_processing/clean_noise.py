from scipy.signal import butter, filtfilt

def filter_data(data):
    # Filter requirements.
    period = 5.0  # Sample Period
    fs = 10.0  # sample rate, Hz
    cutoff = 2  # desired cutoff frequency of the filter, Hz ,      slightly higher than actual 1.2 Hz
    nyq = 0.5 * fs  # Nyquist Frequency
    order = 2  # sin wave can be approx represented as quadratic
    n = int(period * fs)  # total number of samples

    normal_cutoff = cutoff / nyq
    # Get the filter coefficients
    b, a = butter(order, normal_cutoff, btype='low', analog=False)
    result = filtfilt(b, a, data)
    return result



