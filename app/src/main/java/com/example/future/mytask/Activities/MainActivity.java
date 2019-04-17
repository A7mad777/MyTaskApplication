package com.example.future.mytask.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.future.mytask.Adapter.RecyclerAdapter;

import com.example.future.mytask.Model.DataModel;
import com.example.future.mytask.R;
import com.jcminarro.roundkornerlayout.RoundKornerFrameLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    public static String URL_Data = "https://api.github.com/orgs/google/repos?fbclid=IwAR2T7FSGm1p1x2ZrI7Hyih4ErjVXOpCATnXfilsEOUGnoU6lxpG3oUIfOjw";
    private Context context;
    List<DataModel> dataModels = new ArrayList<>();
    RecyclerAdapter adapter;
    List<DataModel>models;
    final int Image_Gallery_Request = 20;
    private ImageView profilePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        profilePicture = (ImageView) findViewById(R.id.profilex);
        dataModels = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
       JasonConn jasonConn = new JasonConn();
        jasonConn.execute(URL_Data);
}
    public class JasonConn extends AsyncTask<String, Void, String > {
    HttpURLConnection connection = null;
    BufferedReader reader = null;
    String name = "";
    String descreption = "";
    String note_id = "";
    String full_name = "";
    String result = "";

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL(params[0]);
            connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return "Error from server";
            }
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            if ((line = reader.readLine()) != null) {
                result += line;
            }
            JSONArray jArray = new JSONArray(result);
            for (int i = 0; i < jArray.length(); i++) {
                JSONObject jRealObj = jArray.getJSONObject(i);
                name += jRealObj.getString("name");
                descreption += jRealObj.getString("description");
                note_id += jRealObj.getString("note_id");
                full_name += jRealObj.getString("full_name");
                DataModel model = new DataModel(note_id, name, full_name, descreption);
                models.add(model);
                model.setDescription(descreption);
                model.setFull_name(full_name);
                model.setName(name);
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return name;
    }

    @Override
    protected void onPostExecute(String s) {
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);

    }
}

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 20) {
                Uri imageUri = data.getData();
                InputStream inputStream;
                try {
                    inputStream = getContentResolver().openInputStream(imageUri);
                    Bitmap image = BitmapFactory.decodeStream(inputStream);
                    profilePicture.setImageBitmap(image);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Unable to ", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }
        public void onTakePhotoClicked(View view) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    Intent photoIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    File PictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
                    String path = PictureDirectory.getPath();
                    Uri data = Uri.parse(path);
                    photoIntent.setDataAndType(data, "image/*");
                    startActivityForResult(photoIntent, Image_Gallery_Request);
                } else {
                    String[] permissionRequest = {Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
                    requestPermissions(permissionRequest, Image_Gallery_Request);
                }
            }
        }
    }
