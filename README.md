# Cancer Detection App

A mobile application for detecting cancer using Machine Learning, developed as a submission for Dicoding. This app leverages TensorFlow Lite for real-time image classification and integrates additional features to enhance user experience and functionality.

---

## 🚀 **Features**
1. **Image Selection and Preview**
   - Users can upload images from their gallery, which are previewed within the app.

2. **Cancer Detection with TensorFlow Lite**
   - Utilizes a pre-trained TensorFlow Lite model to predict whether an image indicates cancer.
   - Provides a confidence score for the prediction.

3. **Prediction Results**
   - Displays:
     - Diagnosis: Indicates whether the image suggests cancer or not.
     - Confidence Score: A numeric value representing the model's prediction accuracy.
   - Handles errors gracefully with appropriate messages.

4. **Health News Integration**
   - Fetches and displays relevant health news articles, including those about cancer, using the [NewsAPI](https://newsapi.org/).

5. **Enhanced Usability**
   - Features for cropping and rotating images (powered by uCrop) to ensure better results during analysis.
   - Saves prediction history locally, including images, results, and confidence scores, for user reference.

---

## ⚙️ **Getting Started**
### Prerequisites
- Android Studio
- A valid API key for [NewsAPI](https://newsapi.org/)

### Installation
1. Clone the repository:
   ```bash
   git clone https://github.com/whdhdyt21/Cancer-Detection.git
   ```
2. Configure the `local.properties` file in your project root to include the API base URL and your API key:
   ```properties
   BASE_URL=https://newsapi.org/
   NEWS_API_KEY=[YOUR-API-KEY]
   ```
3. Open the project in Android Studio and build the app.

4. Run the app on your emulator or physical device.

---

## 🛠️ **Technologies Used**
- **Programming Language**: Kotlin
- **Machine Learning**: TensorFlow Lite
- **API Integration**: NewsAPI
- **Local Database**: Room/Realm
- **Libraries**:
  - uCrop: For image cropping and rotation
  - Retrofit: For API calls
  - Glide: For image loading and display

---

## 📜 **License**
This project is licensed under the MIT License. See the LICENSE file for details.

---

Thank you for exploring the Cancer Detection App! For any issues or suggestions, feel free to open an issue or submit a pull request.
```
