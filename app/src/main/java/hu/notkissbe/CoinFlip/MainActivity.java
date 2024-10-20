package hu.notkissbe.CoinFlip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private ImageView Kep;
    private Button Fej;
    private Button Iras;
    private TextView Stat;
    private String GepDobas;
    private String FelhasznaloTipp;
    private int Dobasok = 0;
    private int Gyoztes = 0;
    private int Vesztes = 0;
    private boolean isHead = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        init();
        Fej.setOnClickListener(v -> {
            FelhasznaloTipp = "Fej";
            coinFlip();
        });
        Iras.setOnClickListener(v -> {
            FelhasznaloTipp = "Iras";
            coinFlip();
        });
    }

    private void init(){
        Fej = findViewById(R.id.Fej);
        Iras = findViewById(R.id.Iras);
        Stat = findViewById(R.id.stat);
        Kep = findViewById(R.id.kep);
    }

    private void eredmenykozles(){
        Stat.setText(String.format("Dobások: %s \nGyőzelem: %s \nVeszteség: %s\n", Dobasok, Gyoztes, Vesztes));
    }

    private void coinFlip(){
        Random r = new Random();
        Dobasok++;
        if (r.nextBoolean()){
            GepDobas = "Fej";
        }
        else{
            GepDobas = "Iras";
        }
        coiflipanim();
        if (GepDobas.equals(FelhasznaloTipp)){
            Gyoztes++;
            Toast.makeText(this, "Nyertél!", Toast.LENGTH_SHORT).show();
        }
        else {
            Vesztes++;
            Toast.makeText(this, "Vesztettél!", Toast.LENGTH_SHORT).show();
        }
        eredmenykozles();
        if (Dobasok >= 5){
            jatekVege();
        }
        if (Gyoztes >= 3 || Vesztes >= 3){
            jatekVege();
        }

    }

    private void coiflipanim(){
        if (GepDobas.equals("Fej")){
            isHead = true;
            Kep.setImageResource(R.drawable.tails);
        }
        else{
            isHead = false;
            Kep.setImageResource(R.drawable.heads);
        }
        ObjectAnimator elso = ObjectAnimator.ofFloat(Kep, "rotationY", 0f, 90f);
        elso.setDuration(500);
        ObjectAnimator masodik = ObjectAnimator.ofFloat(Kep, "rotationY", 90f, 180f);
        masodik.setDuration(500);

        elso.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isHead) {
                    Kep.setImageResource(R.drawable.heads);
                } else {
                    Kep.setImageResource(R.drawable.tails);
                }
                isHead = !isHead;

                masodik.start();
            }
        });
        elso.start();
    }

    private void jatekVege(){
        AlertDialog alert = new AlertDialog.Builder(this).create();
        alert.setTitle("Jatek vege!");
        alert.setMessage("Szertnél újat játszani?");
        alert.setButton(AlertDialog.BUTTON_POSITIVE, "Igen", (dialog, which) -> ujJatek());
        alert.setButton(AlertDialog.BUTTON_NEGATIVE, "Nem", (dialog, which) -> finish());
        alert.setCanceledOnTouchOutside(false);
        alert.show();
    }
    private void ujJatek(){
        Gyoztes = 0;
        Vesztes = 0;
        Dobasok = 0;
        eredmenykozles();
    }
}