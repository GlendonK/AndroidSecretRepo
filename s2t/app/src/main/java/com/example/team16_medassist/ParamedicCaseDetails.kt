package com.example.team16_medassist

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.SpeechRecognizer
import android.widget.Button
import java.util.*
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class ParamedicCaseDetails : AppCompatActivity() {
    val TAG: String = "ParamedicCaseDetails"
    private lateinit var mPreviewText: TextView
    private lateinit var speechRecognizer: SpeechRecognizer
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_paramedic_case_details)

        val mSpeechToTextButton: Button = findViewById(R.id.speechToTextButton)
        mPreviewText = findViewById(R.id.previewText)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        val mMlText: TextView = findViewById(R.id.mlText)
        val mMlButton: Button = findViewById(R.id.mlButton)
        val mStopSpeechToTextButton: Button = findViewById(R.id.stopSpeechToTextButton)

        // SpeechToText class takes in a current Context and TextView(to display) somehow the sequnces gets messed up if i don setText in the STT class
        val speechToText = SpeechToText(this, mPreviewText)

        // button to listen and predict
        mSpeechToTextButton.setOnClickListener {

            speechToText.checkAudioPermission()
            // this is a async task code flow will not stop while this run
            speechToText.startSpeechToText()

            // @TODO fix this BS...
            /* There fore the ML code to predict is in another onclick
            * the speech to text have not finish yet and the ML code
            * will run, predicting with default zero inputs */


        }

        mMlButton.setOnClickListener {
            val symptoms = speechToText.getSpeechText()

            Toast.makeText(applicationContext, "$symptoms", Toast.LENGTH_SHORT).show()
            Log.d(TAG, "$symptoms")
            // Initialise the PredictDiagnosis class @Params(Context)
            val predictDiagnosis = PredictDiagnosis(this)
            // Classify and get a diagnosis
            var res = predictDiagnosis.getDiagnosis(symptoms)
            mMlText.text = res

            // Clear speechText so that it does not keep appending
            speechToText.clearSpeechText()
        }

        mStopSpeechToTextButton.setOnClickListener {

            // @TODO fix this BS logic... it does not stop immediately or after 5sec for some instances.
            speechToText.stopSpeechToText()
        }

    }


}

