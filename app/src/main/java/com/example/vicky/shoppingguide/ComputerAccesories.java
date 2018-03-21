package com.example.vicky.shoppingguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class ComputerAccesories extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    ActionBar actionBar;
    String flipkart = "https://www.flipkart.com/search?as=off&as-show=on&count=40&otracker=start";
    String amazon="https://www.amazon.in/s/ref=";
    String snapdeal="https://www.snapdeal.com/search?keyword=";
    String output = " ";
    int flag;
    Spinner dropdownMethod,dropdownitem,dropdownbrand, dropdownsort;
    EditText editText;
    String Item,AccesoriesBrand,sort;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.computer_accesories);

        actionBar= getSupportActionBar();
        actionBar.setTitle("Computer Accesories");
        actionBar.setDisplayHomeAsUpEnabled(true);

        editText=(EditText)findViewById(R.id.laptopAccessoriesProductDetail);

        dropdownitem=(Spinner)findViewById(R.id.LaptopAccesoriesItem);
        String item[]={"keyboard","mouse","charger","speakers","Other Item"};
        ArrayAdapter<String> arrayAdapteritem=new ArrayAdapter<String>(ComputerAccesories.this,R.layout.spinner_row,item);
        arrayAdapteritem.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownitem.setAdapter(arrayAdapteritem);
        dropdownitem.setOnItemSelectedListener(this);

        dropdownbrand=(Spinner)findViewById(R.id.LaptopAccesoriesBrand);
        String[] brand= {"Any brand","dell","hp","lenevo"};
        ArrayAdapter<String> arrayAdapterbrand=new ArrayAdapter<String>(ComputerAccesories.this,R.layout.spinner_row,brand);
        arrayAdapterbrand.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownbrand.setAdapter(arrayAdapterbrand);
        dropdownbrand.setOnItemSelectedListener(this);


        dropdownsort=(Spinner)findViewById(R.id.sortbyLaptopAccesories);
        String[] itemsSort = {"Relevance", "Popularity", "Price-High to low", "Price-Low to high", "Newest first"};
        ArrayAdapter<String> arrayAdaptersort = new ArrayAdapter<String>(ComputerAccesories.this, R.layout.spinner_row, itemsSort);
        arrayAdaptersort.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownsort.setAdapter(arrayAdaptersort);
        dropdownsort.setOnItemSelectedListener(this);




    }
    @Override
    public void onItemSelected(AdapterView<?> parentMA, View view, int i,long l){

         switch(parentMA.getId()){
             case R.id.LaptopAccesoriesItem:
                 Item=(String )parentMA.getItemAtPosition(i);
                 break;

             case R.id.LaptopAccesoriesBrand:
                 AccesoriesBrand=(String)parentMA.getItemAtPosition(i);
                 break;

             case R.id.sortbyLaptopAccesories:
                 sort=(String)parentMA.getItemAtPosition(i);
                 break;
         }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView)
    {

    }

    public void searchLaptopAccesories(View view)
    {
        if(Item.equals("Other Item")) {
            if (AccesoriesBrand.equals("Any Brand"))
                output = editText.getText().toString();
            else
                output= AccesoriesBrand +" " + editText.getText().toString();
        }
        else if(AccesoriesBrand.equals("Any brand") )
            output= editText.getText().toString()+" "+Item;
        else
        output= AccesoriesBrand +" "+editText.getText().toString()+" "+Item;

        if(sort.equals("Relevance")){
            flipkart += "&q=" + output;

            amazon+="sr_st_relevanceblender";
            amazon+="?keywords="+output;
            amazon+="&sort=relevanceblender";

            snapdeal+=output+"&sort=rlvncy";
        }

        else if (sort.equals("Popularity")) {
            flipkart += "&p%5B%5D=sort%3Dpopularity" + "&q=" + output;
            amazon+="ref=sr_st_review-rank";
            amazon+="?keywords="+output;
            amazon+="&sort=review-rank";

            snapdeal+=output+"&sort=plrty";
        }
        else if (sort.equals("Price-High to low")) {
            flipkart += "&p%5B%5D=sort%3Dprice_desc" + "&q=" + output;
            amazon+="ref=sr_st_price-desc-rank";
            amazon+="?keywords="+output;
            amazon+="&sort=price-desc-rank";

            snapdeal+=output+"&sort=phtl";
        }
        else if (sort.equals("Price-Low to high")) {
            flipkart += "&p%5B%5D=sort%3Dprice_asc" + "&q=" + output;
            amazon+="ref=sr_st_price-asc-rank";
            amazon+="?keywords="+output;
            amazon+="&sort=price-asc-rank";

            snapdeal+=output+"&sort=plth";
        }
        else if (sort.equals("Newest first")) {
            flipkart += "&p%5B%5D=sort%3Drecency_desc" + "&q=" + output;
            amazon+="ref=sr_st_date-desc-rank";
            amazon+="?keywords="+output;
            amazon+="&sort=date-desc-rank";
            snapdeal+=output+"&sort=rec";
        }

        Intent i=new Intent(this,ShoppingActivity.class);
        i.putExtra("flipkart",flipkart);
        i.putExtra("amazon",amazon);
        i.putExtra("snapdeal",snapdeal);

        flipkart="https://www.flipkart.com/search?as=off&as-show=on&count=40&otracker=start";
        amazon="https://www.amazon.in/s/ref=";
        snapdeal="https://www.snapdeal.com/search?keyword=";
        startActivity(i);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id=item.getItemId();

        if(id== R.id.action_settings)
        {
            return true;
        }
        else if(id==android.R.id.home)
        {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent i= new Intent(ComputerAccesories.this,Electronics.class);
        startActivity(i);
        finish();
    }
}