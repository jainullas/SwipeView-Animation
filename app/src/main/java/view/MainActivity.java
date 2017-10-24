package view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ullas.recyclerswipeview.R;

import java.util.ArrayList;

import adapter.TranslateAnimationAdapter;
import data.Data;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Data> countries = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    private void initViews() {
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        countries.addAll(getCountries());
        TranslateAnimationAdapter adapter = new TranslateAnimationAdapter(this, countries);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    public ArrayList<Data> getCountries() {
        ArrayList<Data> countryList = new ArrayList<>();
        countryList.add(new Data("United states of America"));
        countryList.add(new Data("Germany"));
        countryList.add(new Data("India"));
        countryList.add(new Data("Pakisthan"));
        final Data aus = new Data("Australia");
        aus.isBothNotPresent = true;
        countryList.add(aus);
        countryList.add(new Data("Antartica"));
        countryList.add(new Data("United Kingdom"));
        countryList.add(new Data("Dubai"));
        countryList.add(new Data("Japan"));
        countryList.add(new Data("Saudi Arabia"));
        countryList.add(new Data("Sri lanka"));
        countryList.add(new Data("Burma"));
        final Data bla = new Data("bla bla bla");
        bla.isBothNotPresent = true;
        countryList.add(bla);
        countryList.add(new Data("London"));
        countryList.add(new Data("Bhuthan"));
        return countryList;
    }
}
