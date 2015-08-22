package com.example.mauricioarce.jokesonyou;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * Created by Mauricio Arce on 20/08/2015.
 */
@Table(name = "joke")
public class Joke extends Model {

    @Column(name = "joke", index = true)
    public String joke;

    public Joke() {
        super();
    }

    public void setJoke(String joke) {
        this.joke = joke;
    }

    @Override
    public String toString() {
        return joke;
    }
}
