package com.example.vicky.shoppingguide;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;


public class Categories extends Fragment {
    GridView gridView;
    String[] gridViewString ={"Mobiles","Mobile Accesories","Smart Wearables","Laptops","Computer Accesories","Cameras"
            };
    int[] gridViewImageId={
            R.drawable.mobiles,R.drawable.mobile_accesories,R.drawable.smartwearable,R.drawable.laptops,R.drawable.computer_accesories,
            R.drawable.cameras
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.categories,null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CustomAdaptorGridCategories customAdaptorGridCategories=new CustomAdaptorGridCategories(getActivity(),gridViewString,gridViewImageId);
        gridView=(GridView)view.findViewById(R.id.gridViewCategories);
        gridView.setAdapter(customAdaptorGridCategories);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(gridViewString[position]=="Mobiles") {
                    startActivity(new Intent(getContext(), Mobiles.class));
                    getActivity().finish();
                }
                if(gridViewString[position]=="Mobile Accesories") {
                    startActivity(new Intent(getContext(), MobileAccesories.class));
                    getActivity().finish();
                }
                if(gridViewString[position]=="Smart Wearables") {
                    startActivity(new Intent(getContext(), SmartWearables.class));
                    getActivity().finish();
                }
                if(gridViewString[position]=="Laptops") {
                    startActivity(new Intent(getContext(), Laptops.class));
                    getActivity().finish();
                }
                if(gridViewString[position]=="Computer Accesories") {
                    startActivity(new Intent(getContext(), ComputerAccesories.class));
                    getActivity().finish();
                }
                if(gridViewString[position]=="Cameras") {
                    startActivity(new Intent(getContext(), Camera.class));
                    getActivity().finish();
                }
            }
        });

    }
}
