package com.simats.orthoflexhip;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
//import com.github.barteksc.pdfviewer.PDFView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.simats.orthoflexhip.api.ApiService;
import com.simats.orthoflexhip.api.RetrofitClient;
import com.simats.orthoflexhip.apiresponse.Constant;
import com.simats.orthoflexhip.dataClass.PatientViewDischargeData;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;




public class DischargeSummaryActivity extends AppCompatActivity {

    String patientId;
//    PDFView pdfView;

    FragmentActivity activity;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.discharge_summary);
        try {
            activity = this;
            context  = this;
        } catch (Exception e) {
            e.printStackTrace();
        }
        ImageButton imageButton8 = findViewById(R.id.imageButton8);
        imageButton8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Define your back button behavior here
                finish(); // Close the current activity
            }
        });

//        pdfView = findViewById(R.id.pdfView);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        patientId = sharedPreferences.getString(Constant.VIEW_PATIENT_ID_FOR_MEDICAL_DETAILS, null);

        ApiService apiService = RetrofitClient.getInstance();
        apiService.patientViewDischargeSummary(Integer.parseInt(patientId)).enqueue(new Callback<PatientViewDischargeData>() {
            @Override
            public void onResponse(Call<PatientViewDischargeData> call, Response<PatientViewDischargeData> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String pdfUrl = RetrofitClient.BASE_URL + response.body().getData();
//                    new RetrievePDFfromUrl().execute(pdfUrl);
                }
            }

            @Override
            public void onFailure(Call<PatientViewDischargeData> call, Throwable t) {
                Toast.makeText(DischargeSummaryActivity.this, t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File pdfDir = new File(activity.getExternalFilesDir(null), "MyApp");
            if (!pdfDir.exists()) {
                pdfDir.mkdirs();
            }

            File pdfFile = new File(pdfDir, "downloaded_pdf.pdf");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[10096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(pdfFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);
                    fileSizeDownloaded += read;

                    Log.d("DownloadPdf", "Downloaded " + fileSizeDownloaded + " of " + fileSize);
                }

                outputStream.flush();
                return true;
            } catch (IOException e) {
                Log.e("DownloadPdf", "Error writing PDF to disk", e);
                return false;
            } finally {
                if (inputStream != null) inputStream.close();
                if (outputStream != null) outputStream.close();
            }
        } catch (IOException e) {
            Log.e("DownloadPdf", "Error writing PDF to disk", e);
            return false;
        }
    }

//    class RetrievePDFfromUrl extends AsyncTask<String, Void, InputStream> {
//        @Override
//        protected InputStream doInBackground(String... strings) {
//            // we are using inputstream
//            // for getting out PDF.
//            InputStream inputStream = null;
//            try {
//                URL url = new URL(strings[0]);
//                // below is the step where we are
//                // creating our connection.
//                HttpURLConnection urlConnection = (HttpsURLConnection) url.openConnection();
//                if (urlConnection.getResponseCode() == 200) {
//                    // response is success.
//                    // we are getting input stream from url
//                    // and storing it in our variable.
//                    inputStream = new BufferedInputStream(urlConnection.getInputStream());
//                }
//
//            } catch (IOException e) {
//                // this is the method
//                // to handle errors.
//                e.printStackTrace();
//                return null;
//            }
//            return inputStream;
//        }
//
//        @Override
//        protected void onPostExecute(InputStream inputStream) {
//            // after the execution of our async
//            // task we are loading our pdf in our pdf view.
//            pdfView.fromStream(inputStream).load();
//        }
//    }

}