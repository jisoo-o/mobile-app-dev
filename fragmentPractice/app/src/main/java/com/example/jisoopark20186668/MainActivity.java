package com.example.jisoopark20186668;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.example.jisoopark20186668.databinding.ActivityMainBinding;

public class MainActivity extends FragmentActivity implements FragmentOne.btnListener {

    private ActivityMainBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

    }

    public void onButtonClick(String text){
        FragmentTwo fragmentTwo = (FragmentTwo) getSupportFragmentManager().findFragmentById(R.id.fragment2);
        fragmentTwo.changeText(text);
    }


}






