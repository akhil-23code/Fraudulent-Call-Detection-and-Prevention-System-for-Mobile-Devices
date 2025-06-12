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
