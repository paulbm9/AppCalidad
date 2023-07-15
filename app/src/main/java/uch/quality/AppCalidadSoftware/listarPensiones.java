package uch.quality.AppCalidadSoftware;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class listarPensiones extends AppCompatActivity {

    private static final String API_URL = "https://jsonplaceholder.typicode.com/comments?postId=1"; // Reemplaza con la URL de tu API

    private TableLayout tableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_pensiones);
        tableLayout = findViewById(R.id.tableLayout);

        // Consulta la API en segundo plano
        new listarPensiones.FetchDataAsyncTask().execute(API_URL);
    }
    private class FetchDataAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }

                bufferedReader.close();
                inputStream.close();

                return stringBuilder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String response) {
            if (response != null) {
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String postId = jsonObject.getString("id_pension");
                        String id = jsonObject.getString("fecha_vencimiento");
                        String name = jsonObject.getString("fecha_operacion"); // Corregir el nombre de la clave
                        String email = jsonObject.getString("monto"); // Corregir el nombre de la clave
                        String body = jsonObject.getString("estado");
                        String body2 = jsonObject.getString("nombreEstudiante");// Corregir el nombre de la clave

                        // Crea una nueva fila de la tabla
                        TableRow tableRow = new TableRow(listarPensiones.this);

                        // Crea los text views para cada dato
                        TextView postIdTextView = createTextView(postId);
                        TextView idTextView = createTextView(id);
                        TextView nameTextView = createTextView(name);
                        TextView emailTextView = createTextView(email);
                        TextView bodyTextView = createTextView(body);
                        TextView bodyTextView2 = createTextView(body2);

                        // Añade los text views a la fila
                        tableRow.addView(postIdTextView);
                        tableRow.addView(idTextView);
                        tableRow.addView(nameTextView);
                        tableRow.addView(emailTextView);
                        tableRow.addView(bodyTextView);
                        tableRow.addView(bodyTextView2);

                        // Añade la fila a la tabla
                        tableLayout.addView(tableRow);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        private TextView createTextView(String text) {
            TextView textView = new TextView(listarPensiones.this);
            textView.setText(text);
            textView.setPadding(8, 8, 8, 8);
            textView.setGravity(Gravity.CENTER);
            return textView;
        }
    }
    public void matriculas(View view) {
        // Redirigir a la actividad listarPensiones cuando se hace clic en el botón
        Intent intent = new Intent(this, listarActivity.class);
        startActivity(intent);
    }
}