# Fraudulent Call Detection and Prevention System for Mobile Devices

## Server 1 - Speech-to-Text engine (Conversion from high to mono frequency)

1. After opening this server1 folder, create a new venv ```python -m venv myenv```
2. Activate the venv mac/linux - ```source myenv/bin/activate``` windows - ```myenv\Scripts\activate```
3. Install dependencies ```pip install -r requirements.txt```
4. Run the server mac/linux - ```python3 app.py``` windows - ```python app.py```
5. To unistall the dependencies ```pip freeze | xargs pip uninstall -y```


## Server 2 - Automated OTP generation SMTP server

1. After opening this server2 folder, create a new venv ```python -m venv myenv```
2. Activate the venv mac/linux - ```source myenv/bin/activate``` windows - ```myenv\Scripts\activate```
3. Install dependencies ```pip install -r requirements.txt```
4. Run the server mac/linux - ```python3 main.py``` windows - ```python main.py```
5. Make sure you change the Google Mail Credentials in ```main.py```


## ScamDetect - Kotlin Application folder

1. Open project in Android Studio
2. Hardcode the laptop's IP address (make sure that external phone & laptop is connected to the same network)
3. Run the app in your external mobile device

## MLService Link : [Model & Dataset](https://drive.google.com/drive/folders/1eaMqw-ghzdF7lr39Ck1zcIvxjCuwjHN9?usp=sharing)



## About the project

### ScamDetect: AI-powered Fraudulent Call Detection and Prevention System

ScamDetect is an interdisciplinary mobile application designed to detect and prevent fraudulent calls and messages in real time using on-device Artificial Intelligence (AI). By combining speech recognition, natural language processing (NLP), voice pattern analysis, and caller metadata, ScamDetect empowers users to stay safe from telecommunication fraud without compromising privacy.

---

### Key Features

- **Offline AI Processing**
  - All detection is performed on-device, ensuring low latency and user privacy.

- **Voice + Text Analysis**
  - Utilizes MFCC for voice feature extraction and VOSK for real-time transcription.
  - Applies NLP models (TF-IDF, Naive Bayes, Logistic Regression) for scam text analysis.

- **Fraud Database Integration**
  - Matches caller ID against external fraud number databases via HTTP/HTTPS and MQTT.

- **User Reporting System**
  - Allows crowdsourced reporting of scam incidents to improve system learning.

- **Unified Mobile App**
  - All-in-one platform for call initiation, scam detection, alerts, and reporting.

---
### Tech Stack & Tools:
ML Models: Logistic Regression, Random Forest, SVM, LSTM, BERT.
AI Pipelines: Voice + Text + Metadata analysis for hybrid decision making.
APIs: Secure protocols like HTTP/HTTPS, MQTT for external data sync.
---

### Architecture Overview

### A. Input Layer: 
- Captures voice input during calls
- Converts speech to text via VOSK engine

### B. Data Layer:
- Collects call metadata (duration, timestamp, caller ID)
- Preprocesses speech/text and detects anomalies

### C. Processing Layer:
- Applies AI algorithms to classify scam patterns
- Uses voice+text+metadata for multi-modal analysis
- Dynamic programming for pattern history matching

---

### 📈 Evaluation & Results

### Test Environment
- Tested on 5 Android devices (Samsung, Redmi, OnePlus)
- Dataset: Open-source + user simulations

### Detection Accuracy

| Input Type | Accuracy | Scam Score Range | Avg. Response Time |
|------------|----------|------------------|---------------------|
| Text/SMS   | 92%      | 0.54 - 0.91      | ~1.2 seconds         |
| Audio      | 89%      | 0.52 - 0.87      | ~2.5 seconds         |

### User Feedback
- 90% of users rated scam tips as useful & understandable
- Positive response to animated educational videos

---

### Prerequisites
- Android Studio
- Kotlin-compatible Android device
- Internet (for fraud database sync only)
