from flask import Flask, request, jsonify
from vosk import Model, KaldiRecognizer
from pydub import AudioSegment
import wave
import json
import os

app = Flask(__name__)

try:
    model = Model("vosk-model-small-en-us-0.15")
except Exception as e:
    print(f"Error loading Vosk model: {e}")
    model = None

def convert_to_wav(file_path, output_path="converted.wav"):
    if not os.path.exists(file_path):
        raise FileNotFoundError(f"File not found: {file_path}")
    try:
        sound = AudioSegment.from_file(file_path)
        sound = sound.set_channels(1).set_frame_rate(16000).set_sample_width(2)
        sound.export(output_path, format="wav")
        print(f"✅ Conversion from high to mono freq is successful!")# Saved to: {output_path}")
        return output_path
    except Exception as e:
        print(f"❌ Conversion failed: {e}")
        return None

def transcribe_wav(wav_file_path, vosk_model):
    if vosk_model is None:
        return "Vosk model not loaded."

    try:
        wf = wave.open(wav_file_path, "rb")
        if wf.getnchannels() != 1 or wf.getsampwidth() != 2 or wf.getframerate() != 16000:
            return "Audio format not compatible with Vosk (must be 16kHz, 16-bit mono)."

        rec = KaldiRecognizer(vosk_model, wf.getframerate())
        results = []
        while True:
            data = wf.readframes(4000)
            if len(data) == 0:
                break
            if rec.AcceptWaveform(data):
                results.append(json.loads(rec.Result()))
        results.append(json.loads(rec.FinalResult()))

        full_text = "\n".join([r.get('text', '') for r in results])
        return full_text
    except Exception as e:
        return f"Transcription error: {e}"
    finally:
        if 'wf' in locals():
            wf.close()

@app.route('/transcribe', methods=['POST'])
def transcribe_audio():
    if 'audio' not in request.files:
        return jsonify({'error': 'No audio file provided'}), 400

    audio_file = request.files['audio']
    temp_input_file = "uploaded_audio"
    audio_file.save(temp_input_file)

    converted_wav_path = convert_to_wav(temp_input_file)
    os.remove(temp_input_file)

    if converted_wav_path:
        transcription = transcribe_wav(converted_wav_path, model)
        os.remove(converted_wav_path)
        return jsonify({'transcription': transcription})
    else:
        return jsonify({'error': 'Audio conversion failed'}), 500

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0', port=5001)