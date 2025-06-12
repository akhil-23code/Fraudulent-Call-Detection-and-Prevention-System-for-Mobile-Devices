from flask import Flask, request, jsonify
import random
import string
import smtplib
from email.mime.text import MIMEText
from email.mime.multipart import MIMEMultipart

app = Flask(__name__)

otp_store = {}

EMAIL_ADDRESS = "ramesh.thakur482666@gmail.com"
EMAIL_PASSWORD = "knyi yepq immk bujs"

def generate_otp(length=6):
    return ''.join(random.choices(string.digits, k=length))

def send_email(to_email, otp):
    try:
        msg = MIMEMultipart()
        msg['From'] = EMAIL_ADDRESS
        msg['To'] = to_email
        msg['Subject'] = 'Your OTP Code for logging into ScamDetect Mobile Application'

        # body = f'Your OTP is: {otp}'
        body = f"""
        <html>
        <body style="font-family: Arial, sans-serif; background-color: #f4f6f8; padding: 20px; color: #333;">
            <div style="max-width: 480px; margin: auto; background: white; border-radius: 8px; box-shadow: 0 2px 8px rgba(0,0,0,0.1); padding: 30px;">
              <h2 style="color: #2E86C1; margin-bottom: 10px;">Your One-Time Password (OTP)</h2>
              <p style="font-size: 20px; letter-spacing: 2px; font-weight: bold; background-color: #e1ecf7; padding: 15px; text-align: center; border-radius: 6px; color: #1B4F72;">{otp}</p>
              <p style="margin-top: 25px; font-size: 14px; line-height: 1.5;">
            Please <strong>do not share</strong> this OTP with anyone.<br>
            This code is valid for a short time only and is required to verify your identity.
              </p>
              <hr style="border: none; border-top: 1px solid #ddd; margin: 30px 0;">
              <p style="font-size: 12px; color: #888; text-align: center;">
               If you did not request this OTP, please ignore this email.
              </p>
            </div>
        </body>
        </html>
        """

        msg.attach(MIMEText(body, 'html'))

        with smtplib.SMTP('smtp.gmail.com', 587) as server:
            server.starttls()
            server.login(EMAIL_ADDRESS, EMAIL_PASSWORD)
            server.send_message(msg)

        return True
    except Exception as e:
        print("Error sending email:", e)
        return False

@app.route('/send_otp', methods=['POST'])
def send_otp():
    data = request.get_json()
    email = data.get('email')

    if not email:
        return jsonify({'error': 'Email is required'}), 400

    otp = generate_otp()
    otp_store[email] = otp

    if send_email(email, otp):
        return jsonify({'message': 'OTP sent successfully'})
    else:
        return jsonify({'error': 'Failed to send OTP'}), 500

@app.route('/verify_otp', methods=['POST'])
def verify_otp():
    data = request.get_json()
    email = data.get('email')
    otp = data.get('otp')

    if otp_store.get(email) == otp:
        del otp_store[email]
        return jsonify({'message': 'OTP verified successfully'})
    else:
        return jsonify({'error': 'Invalid OTP'}), 400

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=5050, debug=True)
