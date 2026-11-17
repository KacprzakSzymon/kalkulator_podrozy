package com.example.paliwa_tusk;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText dystansEdit;
    private EditText spalanieEdit;
    private RadioButton benzynaRadio;
    private RadioButton dieselRadio;
    private RadioButton lpgRadio;
    private EditText pasazerowieEdit;
    private Button obliczButton;
    private TextView wynikText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        konfigurujWstrzepkiOkna();
        inicjalizujWidoki();
        ustawListenerow();
    }

    private void konfigurujWstrzepkiOkna() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void inicjalizujWidoki() {
        dystansEdit = findViewById(R.id.dystansEdit);
        spalanieEdit = findViewById(R.id.spalanieEdit);
        benzynaRadio = findViewById(R.id.benzynaRadio);
        dieselRadio = findViewById(R.id.dieselRadio);
        lpgRadio = findViewById(R.id.lpgRadio);
        pasazerowieEdit = findViewById(R.id.pasazerowieEdit);
        obliczButton = findViewById(R.id.obliczButton);
        wynikText = findViewById(R.id.wynikText);

        pasazerowieEdit.setText("1");
    }

    private void ustawListenerow() {
        obliczButton.setOnClickListener(v -> wykonajObliczenia());
    }

    private void wykonajObliczenia() {
        try {
            float dystans = pobierzDodatniaWartoscFloat(dystansEdit);
            float spalanie = pobierzDodatniaWartoscFloat(spalanieEdit);
            int pasazerowie = pobierzDodatniaWartoscInt(pasazerowieEdit);
            float przelicznik = pobierzCenePaliwa();

            // Wzory obliczeniowe
            float ileSpali = zaokraglij(spalanie * (dystans / 100f) * przelicznik);
            float naOsobe = zaokraglij(ileSpali / pasazerowie);

            wyswietlWynikSukcesu(ileSpali, naOsobe);

        } catch (Exception e) {
            wyswietlBlad();
        }
    }

    private float pobierzDodatniaWartoscFloat(EditText editText) {
        float wartosc = Float.parseFloat(editText.getText().toString());
        if (wartosc <= 0) {
            throw new IllegalArgumentException("Wartość musi być większa od zera");
        }
        return wartosc;
    }

    private int pobierzDodatniaWartoscInt(EditText editText) {
        int wartosc = Integer.parseInt(editText.getText().toString());
        if (wartosc <= 0) {
            throw new IllegalArgumentException("Wartość musi być większa od zera");
        }
        return wartosc;
    }

    private float pobierzCenePaliwa() {
        if (benzynaRadio.isChecked()) {
            return 6.50f;
        } else if (dieselRadio.isChecked()) {
            return 6.70f;
        } else if (lpgRadio.isChecked()) {
            return 3.10f;
        } else {
            throw new IllegalArgumentException("Nie wybrano rodzaju paliwa");
        }
    }

    private float zaokraglij(float wartosc) {
        return Math.round(wartosc * 100.0f) / 100.0f;
    }

    private void wyswietlWynikSukcesu(float calkowityKoszt, float kosztNaOsobe) {
        wynikText.setTextColor(0xFF00BB00);
        wynikText.setText("Całkowity koszt: " + calkowityKoszt + " zł \n Na osobę: " + kosztNaOsobe + " zł");
    }

    private void wyswietlBlad() {
        wynikText.setTextColor(0xFFBB0000);
        wynikText.setText("Niepoprawne dane wejściowe!");
    }
}