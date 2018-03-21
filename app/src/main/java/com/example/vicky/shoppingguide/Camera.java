package com.example.vicky.shoppingguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

/**
 * Created by vicky on 21/3/18.
 */

public class Camera extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    String flipkart = "https://www.flipkart.com/search?as=off&as-show=on&count=40&otracker=start";
    String amazon = "https://www.amazon.in/s/ref=";
    String snapdeal = "https://www.snapdeal.com/search?keyword=";
    String output = "";
    int flag;
    String Brand = "", Pixels = "", Type = "", sort = "";

    ActionBar actionBarLaptop;

    Spinner dropdownSearchMethod, dropdownBrand, dropdowntype, dropdownPixel, dropdownsort;
    EditText editText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_cat);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Camera Specification");
        actionBar.setDisplayHomeAsUpEnabled(true);


        editText= (EditText) findViewById(R.id.cameraKeyword);

        dropdownSearchMethod=(Spinner) findViewById(R.id.searchmethodcamera);

        String[] searchMethod={"Search by keyword","Search by Specification"};

        ArrayAdapter<String>arrayAdaptersearch=new ArrayAdapter<>(Camera.this,R.layout.spinner_row,searchMethod);
        arrayAdaptersearch.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownSearchMethod.setAdapter(arrayAdaptersearch);
        dropdownSearchMethod.setOnItemSelectedListener(this);


        dropdownBrand=(Spinner)findViewById(R.id.camerabrand);

        String[] brand={"Brand independent","nikon","canon","sony","fujifilm","panasonic","other brand"};

        ArrayAdapter<String>arrayAdapter=new ArrayAdapter<>(Camera.this,R.layout.spinner_row,brand);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownBrand.setAdapter(arrayAdapter);
        dropdownBrand.setOnItemSelectedListener(this);



        dropdownPixel=(Spinner)findViewById(R.id.cameramegapixels);

        String[] pixels={"10 to 13.99","14 to 15.99 MP","16 to 17.99 MP","18 to 23.99","24 and above"};

        ArrayAdapter<String>arrayAdapterpixel=new ArrayAdapter<>(Camera.this,R.layout.spinner_row,pixels);
        arrayAdapterpixel.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownPixel.setAdapter(arrayAdapterpixel);
        dropdownPixel.setOnItemSelectedListener(this);




        dropdowntype=(Spinner)findViewById(R.id.cameratype);

        String[] type={"Any type","camcoders","point shoot","sports action","dslr","instant camera"};

        ArrayAdapter<String> arrayAdaptertype=new ArrayAdapter<>(Camera.this,R.layout.spinner_row,type);
        arrayAdaptertype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdowntype.setAdapter(arrayAdaptertype);
        dropdowntype.setOnItemSelectedListener(this);



        dropdownsort=(Spinner)findViewById(R.id.camerasort);

        String[] sort={"Relevance","Popularity","Price-High to low","Price-Low to high","Newest first"};

        ArrayAdapter<String>arrayAdaptersort=new ArrayAdapter<>(Camera.this,R.layout.spinner_row,sort);
        arrayAdaptersort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        dropdownsort.setAdapter(arrayAdaptersort);
        dropdownsort.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
        switch (parent.getId()) {

            case R.id.searchmethodcamera: {
                if (((String) parent.getItemAtPosition(i)).equals("Search by keyword")) {

                    editText.setVisibility(View.VISIBLE);
                    dropdownBrand.setVisibility(View.GONE);
                    dropdowntype.setVisibility(View.GONE);
                    dropdownPixel.setVisibility(View.GONE);
                    dropdownsort.setVisibility(View.GONE);

                    flag = 0;
                } else {
                    flag = 1;
                    editText.setVisibility(View.GONE);
                    dropdownBrand.setVisibility(View.VISIBLE);
                    dropdowntype.setVisibility(View.VISIBLE);
                    dropdownPixel.setVisibility(View.VISIBLE);
                    dropdownsort.setVisibility(View.VISIBLE);
                    flag=1;
                }
                break;
            }

            case R.id.camerabrand:
                Brand = (String) parent.getItemAtPosition(i);
                break;

            case R.id.cameratype:
                if (((String) parent.getItemAtPosition(i)).equals("Any type")) {
                    Type=" ";
                }
                else
                    Type = (String) parent.getItemAtPosition(i);

                break;

            case R.id.cameramegapixels:
                Pixels = (String) parent.getItemAtPosition(i);
                break;



            case R.id.camerasort:
                sort = (String) parent.getItemAtPosition(i);
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void searchCamera (View view)
    {
        if (flag == 0) {
            output = editText.getText().toString();
        } else if (flag == 1 && (Brand.equals("Brand independent") ||Brand.equals("other brand"))) {
            output = Type+ " camera " + Pixels;

        } else {
            output = Brand +" "+Type+ " camera " + Pixels;
        }

        if (sort.equals("Relevance")) {
            flipkart += "&q=" + output;

            amazon += "sr_st_relevanceblender";
            amazon += "?keywords=" + output;
            amazon += "&sort=relevanceblender";

            snapdeal += output + "&sort=rlvncy";
        } else if (sort.equals("Popularity")) {
            flipkart += "&p%5B%5D=sort%3Dpopularity" + "&q=" + output;
            amazon += "ref=sr_st_review-rank";
            amazon += "?keywords=" + output;
            amazon += "&sort=review-rank";

            snapdeal += output + "&sort=plrty";
        } else if (sort.equals("Price-High to low")) {
            flipkart += "&p%5B%5D=sort%3Dprice_desc" + "&q=" + output;
            amazon += "ref=sr_st_price-desc-rank";
            amazon += "?keywords=" + output;
            amazon += "&sort=price-desc-rank";

            snapdeal += output + "&sort=phtl";
        } else if (sort.equals("Price-Low to high")) {
            flipkart += "&p%5B%5D=sort%3Dprice_asc" + "&q=" + output;
            amazon += "ref=sr_st_price-asc-rank";
            amazon += "?keywords=" + output;
            amazon += "&sort=price-asc-rank";

            snapdeal += output + "&sort=plth";
        } else if (sort.equals("Newest first")) {
            flipkart += "&p%5B%5D=sort%3Drecency_desc" + "&q=" + output;
            amazon += "ref=sr_st_date-desc-rank";
            amazon += "?keywords=" + output;
            amazon += "&sort=date-desc-rank";
            snapdeal += output + "&sort=rec";
        }


        Intent i = new Intent(this, ShoppingActivity.class);
        i.putExtra("flipkart", flipkart);
        i.putExtra("amazon", amazon);
        i.putExtra("snapdeal", snapdeal);
        flipkart = "https://www.flipkart.com/search?as=off&as-show=on&count=40&otracker=start";
        amazon = "https://www.amazon.in/s/ref=";
        snapdeal = "https://www.snapdeal.com/search?keyword=";
        startActivity(i);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id == android.R.id.home)
            onBackPressed();

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(Camera.this,MainActivity.class);
        startActivity(i);
        finish();
    }
}
