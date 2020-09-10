package com.example.clarosms;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class FirstFragment extends Fragment {
    private String directorioRaiz;
    Workbook workbook;
    AsyncHttpClient asyncHttpClient;
    List<String> storyTitle,storyContent,thumbImages,storySms;
    RecyclerView recyclerView;
    Button button3;
    Adapter adapter;
    ProgressBar progressBar;
    TextView wait;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        wait = view.findViewById(R.id.wait);
        button3 = view.findViewById(R.id.button3);

        storyTitle = new ArrayList<>();
        storyContent = new ArrayList<>();
        thumbImages = new ArrayList<>();
        storySms = new ArrayList<>();

        loadExcel(false);

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadExcel(true);
            }
        });
    }

    private void loadExcel(boolean api){
        wait.setVisibility(View.VISIBLE);

        directorioRaiz = Environment.getExternalStorageDirectory().getPath() + "/Download/smstoday.xls";

        File f = new File(directorioRaiz);

        if(f.exists() && !f.isDirectory()) {

            Toast.makeText(getContext(), "Archivos de mensaje existe", Toast.LENGTH_SHORT).show();
            WorkbookSettings ws = new WorkbookSettings();
            ws.setGCDisabled(true);

            String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(permissions,1);

            try {
                workbook = Workbook.getWorkbook(f);
                Sheet sheet = workbook.getSheet(0);

                button3.setText("Enviar mensajes("+ sheet.getRows() + ")" );

                for(int i = 0;i< sheet.getRows();i++){
                    final Cell[] row = sheet.getRow(i);
                    storyTitle.add(row[0].getContents());
                    storyContent.add(row[1].getContents());
                    storySms.add(row[2].getContents());
                    thumbImages.add("hola");

                    if (api){

                        asyncHttpClient = new AsyncHttpClient();

                        asyncHttpClient.get("http://192.168.1.19:9090/sendsms?phone="+row[0].getContents()+"&text="+row[2].getContents()+"&password=1234", new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                Toast.makeText(getContext(), "Mensaje enviado a " + row[1].getContents(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                                Toast.makeText(getContext(), "Falla http a " + row[1].getContents(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }


                }

                showData();

                wait.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);


            } catch (IOException e) {
                e.printStackTrace();
            } catch (BiffException e) {
                e.printStackTrace();
            }
        }else{
            Toast.makeText(getContext(), "Archivos de mensaje no exsite", Toast.LENGTH_SHORT).show();
        }
    }

    private void showData() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new Adapter(getContext(),storyTitle,storyContent,storySms,thumbImages);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
    }


}
