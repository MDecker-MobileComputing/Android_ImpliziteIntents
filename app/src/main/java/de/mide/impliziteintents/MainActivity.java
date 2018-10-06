package de.mide.impliziteintents;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


/**
 * App demonstriert Verwendung von sog. <i>impliziten Intents</i>, mit
 * denen externe Apps geöffnet werden können.
 * <br><br>
 *
 * This project is licensed under the terms of the BSD 3-Clause License.
 */
public class MainActivity extends Activity
                          implements View.OnClickListener {

    /** Button um Browser zu öffnen. */
    protected Button _browserButton = null;

    /**
     * Lifecycle-Methode.
     * Lädt Layout-Datei und weist den Buttons die Activity-Instanz selbst
     * als Event-Handler-Objekt zu. Es werden auch die Member-Variablen
     * für die UI-Elemente befüllt.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _browserButton = findViewById(R.id.browserButton);
        _browserButton.setOnClickListener(this);
    }


    /**
     * Event-Handler für ALLE Buttons dieser Activity.
     * <br/><br/>
     *
     * Wenn für einen Button festgestellt wird, dass der zugehörige
     * implizite Intent auf dem Android-Gerät nicht verarbeitet werden
     * kann, dann wird der Button deaktiviert.
     *
     * @param view Button, der das Event ausgelöst hat.
     */
    @Override
    public void onClick(View view) {

        Intent intent = null;

        if (view == _browserButton) {

            intent = new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.parse("https://www.heise.de");
            intent.setData(uri);

        } else {

            showToast("INTERNER Fehler: Event-Handler von unerwartetem View-Objekt aufgerufen: " + view);
            return;
        }


        if ( wirdIntentUnterstuetzt(intent) )
            startActivity(intent);
        else
            view.setEnabled(false);
    }


    /**
     * Die Methode überprüft, ob es für den als Parameter übergebenen Intent eine
     * passende Zielkomponente auf dem Android-Gerät gibt.
     * Wenn es keine Komponente gibt, dann wird eine Fehlermeldung
     * als Toast ausgegeben.
     * <br/><br/>
     * Wenn diese Methode für einen Intent <tt>false</tt> liefert,
     * der Intent aber trotzdem mit <i>startActivity(intent)</i>
     * abgesetzt wird, dann crasht die App.
     * <br/><br/>
     * Die Verwendung der Methode {@link Intent#resolveActivity(PackageManager)}
     * wird auch auf
     * <a href="https://developer.android.com/guide/components/intents-common.html">dieser Seite</a>
     * im API-Guide beschrieben. Wenn es mehrere Apps gibt, die den Intent
     * verarbeiten/empfangen können, dann wird die mit der höchsten Priorität
     * zurückgeliefert.
     *
     * @param intent Intent-Objekt (impliziter Intent), für das überprüft werden soll,
     *               ob auf dem aktuellen Android-Gerät es mindestens eine App gibt,
     *               den den Intent verarbeiten kann.
     *
     * @return <tt>true</tt>, wenn es mindestens einen Receiver für
     *         den <i>intent</i> gibt.
     */
    protected boolean wirdIntentUnterstuetzt(Intent intent) {

        PackageManager packageManager = this.getPackageManager();

        ComponentName componentName = intent.resolveActivity(packageManager);

        if (componentName == null) {
            showToast("Keine Komponente gefunden für: " + intent);
            return false;
        } else
            return true;
    }


    /**
     * Convenience-Methode, um Nachricht <i>nachricht</i> mit
     * einem Toast-Objekt darzustellen.
     *
     * @param nachricht Auszugebende Nachricht
     */
    protected void showToast(String nachricht) {
        Toast.makeText(this, nachricht, Toast.LENGTH_LONG).show();
    }

}
