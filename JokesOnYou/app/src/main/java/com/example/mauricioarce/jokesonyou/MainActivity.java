package com.example.mauricioarce.jokesonyou;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.query.Select;


public class MainActivity extends AppCompatActivity {

    private TextView showJoke;
    private EditText introduceJoke;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showJoke = (TextView) findViewById(R.id.text_joke);
        introduceJoke = (EditText) findViewById(R.id.text_introduce_joke);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public void showJoke(View view) {
        Joke joke = getRandom();
        if (joke != null) {
            showJoke.setText(joke.toString());
            MainApplication.speakOut(showJoke.getText().toString());
        } else {
            Toast.makeText(getApplicationContext(), "Guarda alguna nota", Toast.LENGTH_SHORT).show();
        }
    }

    public void insertJoke(View view) {
        String jokeText = introduceJoke.getText().toString();
        if (jokeText.replaceAll(" ", "").equals("")) {
            Toast.makeText(getApplicationContext(), "Introduce una nota", Toast.LENGTH_SHORT).show();
        } else {
            Joke joke = new Joke();
            joke.setJoke(jokeText);
            joke.save();

            Toast.makeText(getApplicationContext(), "Nota guardada", Toast.LENGTH_SHORT).show();
        }
    }

    private static Joke getRandom() {
        return new Select().from(Joke.class).orderBy("RANDOM()").executeSingle();
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
        }

        return super.onOptionsItemSelected(item);
    }
}
