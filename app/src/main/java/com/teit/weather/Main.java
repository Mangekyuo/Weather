package com.teit.weather;

import android.app.Activity;



import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;


import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;


import java.util.ArrayList;

import in.galaxyofandroid.spinerdialog.OnSpinerItemClick;
import in.galaxyofandroid.spinerdialog.SpinnerDialog;


public class Main extends Activity {
    DataBaseHelper dataBaseHelper;
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first);

        //work with database
        dataBaseHelper = new DataBaseHelper(Main.this);

        //Create ArrayList<String> where will contain all cities which stores in database:
        ArrayList<String> citieswhichstoreindatabase = new ArrayList<>();

        //firslty recreate screen with all cities
        recreateScreen(citieswhichstoreindatabase);

        //secondly create our work directory
        Button addcity =(Button)findViewById(R.id.ButtonAddCity);
        //create list of cities
        ArrayList<String> cities = new ArrayList<>();
        //fill list with city names
        fullCityList(cities);
        //thirdly we should destroy all cities in our list which was already created
        DestroyCities(cities,citieswhichstoreindatabase);
        //Authorize spinner dialog
         final SpinnerDialog spinnerDialog= new SpinnerDialog(Main.this, cities,"Select item");
         //get what user chosen:
        spinnerDialog.bindOnSpinerListener(new OnSpinerItemClick() {
            @Override
            public void onClick(String item, int position) {
                //replace all spaces to special encodes
                String citytransfer = item.replaceAll(" ","%20");
                createCityLayout(item,citytransfer);
                //add city to database
                dataBaseHelper.addCity(item);
            }
        });
        addcity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //show cities
                spinnerDialog.showSpinerDialog();
            }
        });
    }
    public void recreateScreen(final ArrayList<String> list){
        dataBaseHelper.getAllRecords(list);
        if(list.isEmpty()){
            //if list is empty show that list is empty:
        }
        else {
            //if list isn't empty recreate screen:
            //we should be able to delete city ==>
            final LinearLayout linearLayout =(LinearLayout)findViewById(R.id.Linearyyy);
            //create layoutparams to be able set margins after : LayoutParamsview.setMargins(30,10,30,0);

            //get device height and width
            Display display = getWindowManager().getDefaultDisplay();
            int width = display.getWidth();
            int height = display.getHeight();
            int width_text = width-width/4;

            for (int i=0;i<list.size();i++){
                LinearLayout.LayoutParams LL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height/7);
                LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(width_text,LinearLayout.LayoutParams.WRAP_CONTENT);
                LinearLayout.LayoutParams binparams = new LinearLayout.LayoutParams(128,128);

                //create linear layout
                final LinearLayout layout =new LinearLayout(Main.this);
                LL.setMargins(30,30,30,0);
                layout.setLayoutParams(LL);
                layout.setOrientation(LinearLayout.HORIZONTAL);
                layout.setGravity(Gravity.CENTER_VERTICAL);

                layout.setBackgroundResource(R.drawable.city);

                //create Text View
                final TextView textView = new TextView(Main.this);
                Typeface neue = ResourcesCompat.getFont(Main.this, R.font.neue);
                textView.setTypeface(neue);
                textView.setGravity(Gravity.CENTER);
                textView.setLayoutParams(textparams);
                textView.setTextSize(20);
                textView.setTextColor(Color.WHITE);
                textView.setText(list.get(i));

                //create bin
                ImageView bin = new ImageView(Main.this);
                bin.setBackgroundResource(R.drawable.bin_white);
                binparams.setMargins(0,0,0,0);
                bin.setLayoutParams(binparams);

                bin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String city_names = String.valueOf(textView.getText());
                        //destroy record from database:
                        dataBaseHelper.deleteCity(city_names);
                        //destroy view from linear
                        linearLayout.removeView(layout);
                    }
                });
                //add children to linear layout
                layout.addView(textView);
                layout.addView(bin);

                layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //start new intent ==>
                        String city_names = String.valueOf(textView.getText());
                        Intent intent = new Intent(com.teit.weather.Main.this,Weather.class);
                        String citytransfer = city_names.replaceAll(" ","%20");
                        //push to extras name of that city
                        intent.putExtra("cityname",citytransfer);
                        startActivity(intent);
                        finish();
                    }
                });

                //add linear layout to main layout
                linearLayout.addView(layout);
            }


        }
    }
    public void DestroyCities(ArrayList<String> cities_normally , ArrayList<String> createdcities){
        //destroy cities which already created
        for (int k=0;k<createdcities.size();k++){
            cities_normally.remove(createdcities.get(k));
        }
    }

    public void createCityLayout(final String cityname , final String citytransfer){
        //we should be able to delete city ==>
        final LinearLayout linearLayout =(LinearLayout)findViewById(R.id.Linearyyy);
        //create layoutparams to be able set margins after : LayoutParamsview.setMargins(30,10,30,0);

        //get device height and width
        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();
        int height = display.getHeight();
        int width_text = width-width/4;

        LinearLayout.LayoutParams LL = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,height/7);
        LinearLayout.LayoutParams textparams = new LinearLayout.LayoutParams(width_text,LinearLayout.LayoutParams.WRAP_CONTENT);
        LinearLayout.LayoutParams binparams = new LinearLayout.LayoutParams(128,128);

        //create linear layout
        final LinearLayout layout =new LinearLayout(Main.this);
        LL.setMargins(30,30,30,0);
        layout.setLayoutParams(LL);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER_VERTICAL);

        layout.setBackgroundResource(R.drawable.city);

        //create Text View
        TextView textView = new TextView(Main.this);
        Typeface neue = ResourcesCompat.getFont(Main.this, R.font.neue);
        textView.setTypeface(neue);
        textView.setGravity(Gravity.CENTER);
        textView.setLayoutParams(textparams);
        textView.setTextSize(20);
        textView.setTextColor(Color.WHITE);
        textView.setText(cityname);

        //create bin
        ImageView bin = new ImageView(Main.this);
        bin.setBackgroundResource(R.drawable.bin_white);
        binparams.setMargins(0,0,0,0);
        bin.setLayoutParams(binparams);
        bin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //destroy record from database:
                dataBaseHelper.deleteCity(cityname);
                //destroy view from linear
                linearLayout.removeView(layout);
            }
        });
        //add children to linear layout
        layout.addView(textView);
        layout.addView(bin);

        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //start new intent ==>
                Intent intent = new Intent(com.teit.weather.Main.this,Weather.class);
                //push to extras name of that city
                intent.putExtra("cityname",citytransfer);
                startActivity(intent);
                finish();
            }
        });

        //add linear layout to main layout
        linearLayout.addView(layout);
    }
    //work to full as much as possible cities
    public void fullCityList(ArrayList<String> cities){
        cities.add("Andorra la Vella");
        cities.add("Abu Dhabi");
        cities.add("Kabul");
        cities.add("The Valley");
        cities.add("Tirana");
        cities.add("Yerevan");
        cities.add("Willemstad");
        cities.add("Luanda");
        cities.add("Buenos Aires");
        cities.add("Pago Pago");
        cities.add("Vienna");
        cities.add("Canberra");
        cities.add("Oranjestad");
        cities.add("Mariehamn");
        cities.add("Baku");
        cities.add("Sarajevo");
        cities.add("Bridgetown");
        cities.add("Dhaka");
        cities.add("Brussels");
        cities.add("Ouagadougou");
        cities.add("Sofia");
        cities.add("Manama");
        cities.add("Gitega");
        cities.add("Porto-Novo");
        cities.add("Gustavia");
        cities.add("Hamilton");
        cities.add("Bandar Seri Begawan");
        cities.add("Sucre");
        cities.add("Brasilia");
        cities.add("Nassau");
        cities.add("Thimphu");
        cities.add("Gaborone");
        cities.add("Minsk");
        cities.add("Belmopan");
        cities.add("Ottawa");
        cities.add("Kinshasa");
        cities.add("Bangui");
        cities.add("Brazzaville");
        cities.add("Bern");
        cities.add("Avarua");
        cities.add("Santiago");
        cities.add("Yaounde");
        cities.add("Beijing");
        cities.add("Bogota");
        cities.add("San Jose");
        cities.add("Belgrade");
        cities.add("Havana");
        cities.add("Praia");
        cities.add("Willemstad");
        cities.add("Nicosia");
        cities.add("Prague");
        cities.add("Berlin");
        cities.add("Djibouti");
        cities.add("Copenhagen");
        cities.add("Roseau");
        cities.add("Santo Domingo");
        cities.add("Algiers");
        cities.add("Quito");
        cities.add("Tallinn");
        cities.add("Cairo");
        cities.add("Asmara");
        cities.add("Madrid");
        cities.add("Addis Ababa");
        cities.add("Helsinki");
        cities.add("Suva");
        cities.add("Stanley");
        cities.add("Palikir");
        cities.add("Torshavn");
        cities.add("Paris");
        cities.add("Libreville");
        cities.add("London");
        cities.add("Tbilisi");
        cities.add("Cayenne");
        cities.add("Accra");
        cities.add("Gibraltar");
        cities.add("Nuuk");
        cities.add("Banjul");
        cities.add("Conakry");
        cities.add("Basse-Terre");
        cities.add("Malabo");
        cities.add("Athens");
        cities.add("Grytviken");
        cities.add("Hagatna");
        cities.add("Bissau");
        cities.add("Georgetown");
        cities.add("Hong Kong");
        cities.add("Tegucigalpa");
        cities.add("Zagreb");
        cities.add("Port-au-Prince");
        cities.add("Budapest");
        cities.add("Jakarta");
        cities.add("Dublin");
        cities.add("Jerusalem");
        cities.add("Douglas");
        cities.add("New Delhi");
        cities.add("Baghdad");
        cities.add("Tehran");
        cities.add("Reykjavik");
        cities.add("Rome");
        cities.add("Saint Helier");
        cities.add("Kingston");
        cities.add("Amman");
        cities.add("Tokyo");
        cities.add("Nairobi");
        cities.add("Bishkek");
        cities.add("Phnom Penh");
        cities.add("Tarawa");
        cities.add("Moroni");
        cities.add("Basseterre");
        cities.add("Pyongyang");
        cities.add("Seoul");
        cities.add("Kuwait City");
        cities.add("George Town");
        cities.add("Nur-Sultan");
        cities.add("Vientiane");
        cities.add("Beirut");
        cities.add("Castries");
        cities.add("Vaduz");
        cities.add("Colombo");
        cities.add("Monrovia");
        cities.add("Maseru");
        cities.add("Vilnius");
        cities.add("Luxembourg");
        cities.add("Riga");
        cities.add("Tripoli");
        cities.add("Rabat");
        cities.add("Monaco");
        cities.add("Chisinau");
        cities.add("Podgorica");
        cities.add("Marigot");
        cities.add("Marigot");
        cities.add("Skopje");
        cities.add("Bamako");
        cities.add("Nay Pyi Taw");
        cities.add("Ulaanbaatar");
        cities.add("Macao");
        cities.add("Saipan");
        cities.add("Male");
        cities.add("Lilongwe");
        cities.add("Mexico City");
        cities.add("Kuala Lumpur");
        cities.add("Maputo");
        cities.add("Windhoek");
        cities.add("Niamey");
        cities.add("Amsterdam");
        cities.add("Oslo");
        cities.add("Kathmandu");
        cities.add("Yaren");
        cities.add("Wellington");
        cities.add("Muscat");
        cities.add("Lima");
        cities.add("Manila");
        cities.add("Islamabad");
        cities.add("Warsaw");
        cities.add("East Jerusalem");
        cities.add("Lisbon");
        cities.add("Asuncion");
        cities.add("Doha");
        cities.add("Belgrade");
        cities.add("Moscow");
        cities.add("Riyadh");
        cities.add("Victoria");
        cities.add("Khartoum");
        cities.add("Stockholm");
        cities.add("Singapore");
        cities.add("Ljubljana");
        cities.add("Bratislava");
        cities.add("Freetown");
        cities.add("Dakar");
        cities.add("Mogadishu");
        cities.add("Damascus");
        cities.add("N'Djamena");
        cities.add("Bangkok");
        cities.add("Dushanbe");
        cities.add("Ashgabat");
        cities.add("Tunis");
        cities.add("Ankara");
        cities.add("Taipei");
        cities.add("Washington");
        cities.add("Tashkent");
        cities.add("Hanoi");
        cities.add("Lusaka");
        cities.add("Harare");
    }
}