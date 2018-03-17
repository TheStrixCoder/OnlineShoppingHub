package com.example.vicky.shoppingguide;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Caption;
import com.microsoft.projectoxford.vision.contract.LanguageCodes;
import com.microsoft.projectoxford.vision.contract.Line;
import com.microsoft.projectoxford.vision.contract.OCR;
import com.microsoft.projectoxford.vision.contract.Region;
import com.microsoft.projectoxford.vision.contract.Word;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by vicky on 15/3/18.
 */

@SuppressLint("ValidFragment")
class CaptureAndFindText extends Fragment {
    public VisionServiceClient visionServiceClient= new VisionServiceRestClient("7b40922bda1a4635b9697dfb4dbf5daa","https://westcentralus.api.cognitive.microsoft.com/vision/v1.0");
    Button clickImage,proceed;
    RoundedImageView preview;
    File file;
    Uri uriSavedImage;
    Bitmap bitmap;
    Gson gson;
    EditText mEditText;
    ByteArrayInputStream inputStream;
    ArrayList<String> items;
    String output="";
    String flipkart = "https://www.flipkart.com/search?as=off&as-show=on&count=40&otracker=start";
    String amazon="https://www.amazon.in/s/ref=";
    String snapdeal="https://www.snapdeal.com/search?keyword=";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.camera_activity,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //to fix uri error
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        preview= (RoundedImageView) view.findViewById(R.id.imageView4);
        proceed= (Button) view.findViewById(R.id.proceedImage);
        clickImage= (Button) view.findViewById(R.id.captureImage);

        clickImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(),new String[] { Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE }, 0);
                }
                else {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    file = new File(Environment.getExternalStorageDirectory(), "MyImages");
                    file.mkdirs();
                    File image = new File(file, "sample.jpg");
                    uriSavedImage = Uri.fromFile(image);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uriSavedImage);

                    startActivityForResult(intent, 100);
                }


            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {
                clickImage.setVisibility(View.GONE);
                preview.setVisibility(View.VISIBLE);
                proceed.setVisibility(View.VISIBLE);
                preview.setImageURI(uriSavedImage);
               /* try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uriSavedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                //preview.setImageBitmap(bitmap);

                bitmap=ImageHelper.loadSizeLimitedBitmapFromUri(uriSavedImage,getContext().getContentResolver());

                proceed.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataTask dataTask=new DataTask();
                        dataTask.execute();
                    }
                });

            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
        }

    }

    public class DataTask extends AsyncTask<InputStream,String,String> {
        private Exception e=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // mEditText.setText("Describing...");
        }

        @Override
        protected String doInBackground(InputStream... inputStreams) {
            try {
                ByteArrayOutputStream output=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,output);
                inputStream=new ByteArrayInputStream(output.toByteArray());
                gson=new Gson();

                OCR ocr;
                ocr=visionServiceClient.recognizeText(inputStream, LanguageCodes.AutoDetect,true);
                String result=gson.toJson(ocr);
                Log.i("Message:","received");
                return result;
            } catch (IOException e) {
                e.printStackTrace();
                this.e=e;

            } catch (VisionServiceException e) {
                e.printStackTrace();
                this.e=e;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(e!=null){
                Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                this.e=null;
            } else {
                Gson gson=new Gson();
                OCR ocr=gson.fromJson(s,OCR.class);
                String res="";
                items=new ArrayList<>();
                for (Region reg : ocr.regions) {
                    for (Line line : reg.lines) {
                        for (Word word : line.words) {
                            res +=word.text+ " ";
                        }
                        items.add(res);
                        res="";
                    }
                }
            }

                try {
                    afterPost(items);
                }catch (Exception e){
                    Log.i("Errorr:",e.getMessage());
                }


            }

        }

    private void afterPost(ArrayList<String> items) {
        String[] f_items=items.toArray(new String[items.size()]);
        new AlertDialog.Builder(getContext())
                .setTitle("Choose an Object")
                .setSingleChoiceItems(f_items, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView listView=((AlertDialog)dialog).getListView();
                        output = (String) listView.getAdapter().getItem(listView.getCheckedItemPosition());
                        Toast.makeText(getContext(),output,Toast.LENGTH_SHORT).show();
                    }
                })
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        flipkart += "&q=" + output;

                        amazon+="sr_st_relevanceblender";
                        amazon+="?keywords="+output;
                        amazon+="&sort=relevanceblender";

                        snapdeal+=output+"&sort=rlvncy";
                        Intent i=new Intent(getContext(),ShoppingActivity.class);
                        i.putExtra("flipkart",flipkart);
                        i.putExtra("amazon",amazon);
                        i.putExtra("snapdeal",snapdeal);
                        flipkart="https://www.flipkart.com/search?as=off&as-show=on&count=40&otracker=start";
                        amazon="https://www.amazon.in/s/ref=";
                        snapdeal="https://www.snapdeal.com/search?keyword=";
                        startActivity(i);
                    }
                }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clickImage.setVisibility(View.VISIBLE);
                preview.setVisibility(View.GONE);
                proceed.setVisibility(View.GONE);
                dialog.dismiss();
            }
        }).show();

    }
}

