package com.example.vicky.shoppingguide;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.microsoft.projectoxford.vision.VisionServiceClient;
import com.microsoft.projectoxford.vision.VisionServiceRestClient;
import com.microsoft.projectoxford.vision.contract.AnalysisResult;
import com.microsoft.projectoxford.vision.contract.Caption;
import com.microsoft.projectoxford.vision.rest.VisionServiceException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by vicky on 4/10/17.
 */

public class CaptureAndFind extends Fragment{
    public VisionServiceClient visionServiceClient= new VisionServiceRestClient("7b40922bda1a4635b9697dfb4dbf5daa","https://westcentralus.api.cognitive.microsoft.com/vision/v1.0");
    Button clickImage,proceed;
    RoundedImageView preview;
    File file;
    Uri uriSavedImage;
    Bitmap bitmap;
    Gson gson;
    EditText mEditText;
    ByteArrayInputStream inputStream;
    String output="";
    String flipkart = "https://www.flipkart.com/search?as=off&as-show=on&count=40&otracker=start";
    String amazon="https://www.amazon.in/s/ref=";
    String snapdeal="https://www.snapdeal.com/search?keyword=";
    ProgressDialog progressDialog;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.camera_activity,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //to fix some URI error
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
                //preview.setImageURI(uriSavedImage);
               /* try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uriSavedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
                bitmap=ImageHelper.loadSizeLimitedBitmapFromUri(uriSavedImage,getContext().getContentResolver());
               preview.setImageBitmap(bitmap);



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

    public class DataTask extends AsyncTask<InputStream,String,String>{
        private Exception e=null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // mEditText.setText("Describing...");
            progressDialog=new ProgressDialog(getContext());
            progressDialog.setTitle("Please wait...");
            progressDialog.show();
        }

        @Override
        protected String doInBackground(InputStream... inputStreams) {
            try {
                ByteArrayOutputStream output=new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,output);
                inputStream=new ByteArrayInputStream(output.toByteArray());
                gson=new Gson();
                publishProgress("Recognising...");
                AnalysisResult v=visionServiceClient.describe(inputStream,1);
                String result=gson.toJson(v);
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
            progressDialog.dismiss();
            if(e!=null){
                Toast.makeText(getContext(),"Error: "+e.getMessage(),Toast.LENGTH_SHORT).show();
                this.e=null;
            } else {
                Gson gson=new Gson();
                AnalysisResult result=gson.fromJson(s,AnalysisResult.class);
                String cap="";
                ArrayList<String> tags=new ArrayList<>();
              /*  mEditText.append("\n");
                mEditText.append("Image format: " + result.metadata.format + "\n");
                mEditText.append("Image width: " + result.metadata.width + ", height:" + result.metadata.height + "\n");
                mEditText.append("\n");*/

                for (Caption caption: result.description.captions) {
                    cap=caption.text;
                    //mEditText.append("Caption: " + caption.text + ", confidence: " + caption.confidence + "\n");
                }
                /*mEditText.append(cap);
                mEditText.append("\n");*/

                for (String tag: result.description.tags) {
                    tags.add(tag);
                    //mEditText.append("Tag: " + tag + "\n");
                }
              /* mEditText.append("\n");
                mEditText.append("\n--- Raw Data ---\n\n");
               mEditText.append(s);
               mEditText.setSelection(0);*/
              try {
                  afterPost(cap,tags);
              }catch (Exception e){
                  Log.i("Errorr:",e.getMessage());
              }


            }

        }
    }

    private void afterPost(String cap, final ArrayList<String> tags) {
        new AlertDialog.Builder(getContext())
                .setTitle("Please Confirm")
                .setMessage("Does the following description match the clicked image?"+"\n"+"Description:"+"\n"+cap)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            onSuccess(tags);
                        }catch ( Exception e){
                            Log.i("Errorr:",e.getMessage());
                        }
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(),"Please try to click the image again for better accuracy!!",Toast.LENGTH_SHORT).show();
                        clickImage.setVisibility(View.VISIBLE);
                        preview.setVisibility(View.GONE);
                        proceed.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                }).show();
    }

    private void onSuccess(ArrayList<String> tags) {
        String[] f_tags=tags.toArray(new String[tags.size()]);
        new AlertDialog.Builder(getContext())
                .setTitle("Choose an Object")
                .setSingleChoiceItems(f_tags, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ListView listView=((AlertDialog)dialog).getListView();
                        output = (String) listView.getAdapter().getItem(listView.getCheckedItemPosition());
                        Toast.makeText(getContext(),output,Toast.LENGTH_SHORT).show();
                    }
                }).setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                    && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            }

    }
}
